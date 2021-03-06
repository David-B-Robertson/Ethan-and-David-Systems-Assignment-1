package sample;

import javafx.stage.DirectoryChooser;
import java.io.*;
import java.util.*;

public class FileReader {



    private Map<String, String> wordCounts;

    public FileReader() {
        wordCounts = new TreeMap<>();
    }

    public void parseFile(File file) throws IOException {
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());

        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseFile(current);
                //System.out.println(current.getName());
            }
        } else {
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                String fileName = file.getName();
                if (isValidWord(token)) {
                    countWord(token, fileName);
                }
            }
        }

    }

    private boolean isValidWord(String word) {
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);

    }

    private void countWord(String word, String fileName) {
        if (!wordCounts.containsKey(word)) {
            wordCounts.put(word, fileName);
        }
    }


    public void outputWordCount(int minCount, File output) throws IOException {
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total words:" + wordCounts.keySet().size());

        if (!output.exists()) {
            output.createNewFile();
            if (output.canWrite()) {
                PrintWriter fileOutput = new PrintWriter(output);

                Set<String> keys = wordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    String count = wordCounts.get(key);
                    // testing minimum number of occurances

                        fileOutput.println(key + ": " + count);

                }

                fileOutput.close();
            }
        } else {
            System.out.println("Error: the output file already exists: " + output.getAbsolutePath());
        }


    }
}
