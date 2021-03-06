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
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);


        File dataDir = mainDirectory;
        File outFile = new File("../Assignment-1/output");

        FileReader wordCounter = new FileReader();
        System.out.println("Hello");
        try{

            File hamDir = new File(dataDir+"/train/ham");
            File ham2Dir = new File(dataDir+"/train/ham2");
            File spamDir = new File(dataDir+"/train/spam");





            //File hamOut = new File("../Assignment-1/output1");
            File ham2Out = new File("../Assignment-1/output2");
            File spamOut = new File("../Assignment-1/output3");

            System.out.println("PARSING "+hamDir);
            wordCounter.parseFile(hamDir);
          //  wordCounter.outputWordCount(2, hamOut);

            System.out.println("PARSING "+ham2Dir);
            wordCounter.parseFile(ham2Dir);
            wordCounter.outputWordCount(2, ham2Out);

            System.out.println("PARSING "+spamDir);
            wordCounter.parseFile(spamDir);
            wordCounter.outputWordCount(2, spamOut);







        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        primaryStage.show();
    }


    public static void main(String[] args) {




        /*
        if(args.length < 2){
            System.err.println("Usage: java WordCounter <inputDir> <outfile>");
            System.exit(0);
        }*/




        launch(args);
    }


}
