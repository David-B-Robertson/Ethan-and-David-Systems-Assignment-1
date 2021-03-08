package sample;

import javafx.stage.DirectoryChooser;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class FileReader {




    private Map<String, Integer> hamWordCounts;
    private Map<String, Integer> spamWordCounts;
    private Map<String, Double> spamProbabilty;
    private Map<String, Double> hamProbabilty;
    private Map<String, Double> probailityWordIsSpam;
    public FileReader() {

        hamWordCounts = new TreeMap<>();
        spamWordCounts = new TreeMap<>();
        spamProbabilty = new TreeMap<>();
        hamProbabilty = new TreeMap<>();
        probailityWordIsSpam = new TreeMap<>();
    }

    public void parseHam(File file) throws IOException {
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());
        System.out.println(file);

        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseHam(current);
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
                    countWordHam(token);
                    temp.add(token);
                }
            }


        }
    }

    public void parseSpam(File file) throws IOException {
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());
        System.out.println(file);

        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseSpam(current);
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
                    countWordSpam(token);
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

    private void countWordHam(String word){
        if(hamWordCounts.containsKey(word)){
            int previous = hamWordCounts.get(word);
            hamWordCounts.put(word, previous+1);
        }else{
            hamWordCounts.put(word, 1);
        }
    }

    private void countWordSpam(String word){
        if(spamWordCounts.containsKey(word)){
            int previous = spamWordCounts.get(word);
            spamWordCounts.put(word, previous+1);
        }else{
            spamWordCounts.put(word, 1);
        }
    }


    public void outputHamWordCount(int minCount, File output) throws IOException {
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total ham words:" + hamWordCounts.keySet().size());

        if (!output.exists()) {
            output.createNewFile();
            if (output.canWrite()) {

                PrintWriter fileOutput = new PrintWriter(output);

                Set<String> keys = hamWordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    int count = hamWordCounts.get(key);
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

    public void outputSpamWordCount(int minCount, File output) throws IOException {
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total spam words:" + spamWordCounts.keySet().size());

        if (!output.exists()) {
            output.createNewFile();
            if (output.canWrite()) {

                PrintWriter fileOutput = new PrintWriter(output);

                Set<String> keys = spamWordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    int count = spamWordCounts.get(key);
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

    public void spamProb() {
        Set<String> keys = spamWordCounts.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            Double count = 502.0;
            // testing minimum number of occurances

            int spamKey = spamWordCounts.get(key);
            double fileMath = spamKey / count;
            spamProbabilty.put(key, fileMath);



        }
    }

    public void hamProb(){
        Set<String> keys = hamWordCounts.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            Double count = 2754.0;
            // testing minimum number of occurances

            int hamKey = hamWordCounts.get(key);
            double fileMath = hamKey/count;
            hamProbabilty.put(key,fileMath);
        }


    }

    public void createWordSpam(){

        Set<String> hamkeys = hamWordCounts.keySet();
        Iterator<String> hamkeyIterator = hamkeys.iterator();

        while (hamkeyIterator.hasNext()) {
            String key = hamkeyIterator.next();


            Double spamKey = spamProbabilty.get(key);
            Double hamKey = hamProbabilty.get(key);
            double fileMath = spamKey/(spamKey +hamKey);
            probailityWordIsSpam.put(key,fileMath);
        }

        Set<String> spamkeys = spamWordCounts.keySet();
        Iterator<String> spamkeyIterator = spamkeys.iterator();

        while (spamkeyIterator.hasNext()) {
            String key = spamkeyIterator.next();


            Double spamKey = spamProbabilty.get(key);
            Double hamKey = hamProbabilty.get(key);
            double fileMath = spamKey/(spamKey +hamKey);
            probailityWordIsSpam.put(key,fileMath);
        }


    }
}