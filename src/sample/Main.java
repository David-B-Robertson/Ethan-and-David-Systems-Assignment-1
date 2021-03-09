package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *WRITTEN BY:
 * Ethan Randle-Bragg (100742591)
 * David Robertson (100751769)
 */

/**
 *Prompts user to choose a directory, once user has chosen the data folder methods from
 * FileReader.java to create spam training maps, then to calculate the probability of
 * an email being spam. This output is displayed in a table, along with the accuracy and
 * precision of the calculation and several images
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);


        File dataDir = mainDirectory;


        FileReader wordCounter = new FileReader();

        try{

            File hamDir = new File(dataDir+"/train/ham");
            File ham2Dir = new File(dataDir+"/train/ham2");
            File spamDir = new File(dataDir+"/train/spam");


            File testDir = new File(dataDir+"/test");


            //parse ham1 & 2
            wordCounter.parseHam(hamDir);
            wordCounter.parseHam(ham2Dir);


            //parse spam
            wordCounter.parseSpam(spamDir);


            //probability counting
            wordCounter.spamProb();
            wordCounter.hamProb();

            wordCounter.checkFileIsSpam();


            TableColumn Filename = new TableColumn("File");
            TableColumn ActualClass = new TableColumn("Actual Class");
            TableColumn SpamProbability = new TableColumn("Spam Probability");

            Filename.setMinWidth(300);
            ActualClass.setMinWidth(100);
            SpamProbability.setMinWidth(300);

            TableView Table = new TableView();
            Table.setEditable(true);


            GridPane gridPane = new GridPane();
            gridPane.setVgap(1);
            gridPane.setHgap(1);
            ObservableList<TestFile> data = wordCounter.parseTest(testDir);


            Filename.setCellValueFactory(new PropertyValueFactory<TestFile, String>("Filename"));
            ActualClass.setCellValueFactory(new PropertyValueFactory<TestFile, String>("ActualClass"));
            SpamProbability.setCellValueFactory(new PropertyValueFactory<TestFile, Double>("SpamProbRounded"));

            Table.setItems(data);

            Table.getColumns().addAll(Filename, ActualClass, SpamProbability);

            gridPane.add(Table,0,0);
            Label accLabel = new Label("Accuracy: ");
            //accLabel.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
            String acc = Double.toString(wordCounter.Accuracy);
            TextField acctext = new  TextField(acc);
            //acctext.setFont(Font.font(java.awt.Font.SANS_SERIF, 14));

            Label precLabel = new Label("Precision: ");
            //precLabel.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
            String prec = Double.toString(wordCounter.Precision);
            TextField prectext = new  TextField(prec);
            //prectext.setFont(Font.font(java.awt.Font.SANS_SERIF, 14));

            //Creating an image
            Image image = new Image(new FileInputStream("../SystemsAssig1/antispam.png"));
            Image image2 = new Image(new FileInputStream("../SystemsAssig1/pngwing.com.png"));
            Image image3 = new Image(new FileInputStream("../SystemsAssig1/wow.png"));

            //Setting the image view
            ImageView imageView = new ImageView(image);
            ImageView imageView2 = new ImageView(image2);
            ImageView imageView3 = new ImageView(image3);

            HBox hb = new HBox();
            hb.getChildren().addAll(accLabel,acctext ,precLabel,prectext);
            hb.setSpacing(50);
            gridPane.add(hb,0,1);

            HBox hb2 = new HBox();
            hb2.getChildren().addAll(imageView, imageView2, imageView3);
            hb2.setSpacing(50);

            gridPane.add(hb2, 0, 2);

            Group root = new Group(gridPane);
            Scene scene = new Scene(root, 1100, 800, Color.BEIGE);
            primaryStage.setTitle("100% Best Most Efficient Spam Filter™");
            primaryStage.setScene(scene);
            primaryStage.show();


        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }



    }

    /**
     * Launches the program
     */
    public static void main(String[] args) {
        launch(args);
    }


}