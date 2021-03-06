package sample;

import javafx.stage.DirectoryChooser;
import java.io.*;
import java.util.*;

public class FileReader {



    private Map<String, Integer> wordCounts;
    private Map<String, Integer> hamWordCounts;
    private Map<String, Integer> spamWordCounts;

    public FileReader() {
        wordCounts = new TreeMap<>();
        hamWordCounts = new TreeMap<>();
        spamWordCounts = new TreeMap<>();
    }

    public void parseFile(File file) throws IOException {
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());
        System.out.println(file);

        File hamDir = new File("../train/ham/..");
        File ham2Dir = new File("../train/ham2/..");
        File spamDir = new File("../train/spam/..");

        if(file == hamDir || file == ham2Dir){
          wordCounts = hamWordCounts;
        } else if(file == spamDir){
            wordCounts = spamWordCounts;
        }

        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseFile(current);
                //System.out.println(current.getName());
            }
        } else {

            //creating temp list
            List<String> temp = new ArrayList<>();

            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                //String fileName = file.getName();

                if (isValidWord(token) && !temp.contains(token)){
                    countWord(token);
                    temp.add(token);
                }
            }
        }

    }

    private boolean isValidWord(String word) {
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);

    }

    private void countWord(String word){
        if(wordCounts.containsKey(word)){
            int previous = wordCounts.get(word);
            wordCounts.put(word, previous+1);
        }else{
            wordCounts.put(word, 1);
        }
    }


    public void outputWordCount(int minCount, File output) throws IOException {
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total words:" + wordCounts.keySet().size());

        File hamOut = new File("../Assignment-1/output1");
        File ham2Out = new File("../Assignment-1/output2");
        File spamOut = new File("../Assignment-1/output3");


        if (!output.exists()) {
            output.createNewFile();
            if (output.canWrite()) {

                PrintWriter fileOutput = new PrintWriter(output);

                System.out.println("OUTPUT ="+output);
                System.out.println("SPAMOUT ="+spamOut);

                if(output == spamOut){
                    System.out.println("TEST");
                    wordCounts = spamWordCounts;
                }
                Set<String> keys = wordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    int count = wordCounts.get(key);
                    // testing minimum number of occurances
                    if(count>=minCount){
                        fileOutput.println(key + ": " + count);
                    }
                }

                fileOutput.close();
            }
        } else {
            System.out.println("Error: the output file already exists: " + output.getAbsolutePath());
        }


    }
}
