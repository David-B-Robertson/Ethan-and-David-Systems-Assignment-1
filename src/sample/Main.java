package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("100% Best Most Efficient Spam Filter™");
        primaryStage.setScene(new Scene(root, 400, 475));

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);


        File dataDir = mainDirectory;


        FileReader wordCounter = new FileReader();
        System.out.println("Hello");
        try{

            File hamDir = new File(dataDir+"/train/ham");
            File ham2Dir = new File(dataDir+"/train/ham2");
            File spamDir = new File(dataDir+"/train/spam");

            File ham2Out = new File("../SystemsAssig1/hamMap");
            File spamOut = new File("../SystemsAssig1/spamMap");

            System.out.println("PARSING "+hamDir);
            System.out.println("PARSING "+ham2Dir);

            //parse ham1 & 2
            wordCounter.parseHam(hamDir);
            wordCounter.parseHam(ham2Dir);

            //output ham
            wordCounter.outputHamWordCount(2, ham2Out);

            System.out.println("PARSING "+spamDir);

            //parse spam
            wordCounter.parseSpam(spamDir);

            //output spam
            wordCounter.outputSpamWordCount(2, spamOut);


            //probability counting
            wordCounter.spamProb();
            wordCounter.hamProb();


        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}