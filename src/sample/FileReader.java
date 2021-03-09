package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.*;

/**
 *This is for reading a file and mapping elements of that file.
 * <p>
 * Contains methods for parsing ham and spam training files to gather data on
 * ham and spam frequencies.
 * <p>
 * Contains methods for calculating the chance that a file is a spam file
 */
public class FileReader {
    private Map<String, Integer> trainHamFreqs;
    private Map<String, Integer> trainSpamFreqs;
    private Map<String, Double> spamProbabilty;
    private Map<String, Double> hamProbabilty;
    private static Map<String, Double> probabilityFileIsSpam;

    static double fancyN = 0.0;
    public static double numTruePositives;
    public static double numTrueNegatives;
    public static double numFalsePositives;
    public static double numberofFiles;

    public  double Accuracy;
    public  double Precision;
    public ObservableList<TestFile> fileClasses = FXCollections.observableArrayList();

    public ArrayList<String> blackList = new ArrayList<>();

    /**
     * Initialized treemaps as well as populating blacklist
     */
    public FileReader() {

        trainHamFreqs = new TreeMap<>();
        trainSpamFreqs = new TreeMap<>();
        spamProbabilty = new TreeMap<>();
        hamProbabilty = new TreeMap<>();
        probabilityFileIsSpam = new TreeMap<>();

        //to enhance the spam filter a blacklist of common words was created
        //these common words will not factor in to the spam probability calculations
        blackList.add("a");
        blackList.add("by");
        blackList.add("the");
        blackList.add("I");
        blackList.add("you");
        blackList.add("and");
        blackList.add("are");
        blackList.add("or");

    }

    /**
     * Recursively reads ham training files, calls validWord() to check if words are
     * valid, then creates a list of words and how many files those words occur in.
     * <p>
     * Populates a ham frequency map using the above information
     *
     * @param file file path of files being parsed
     * @throws IOException
     */
    public void parseHam(File file) throws IOException {
        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseHam(current);
            }
        } else {
            //creating temp list
            List<String> temp = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                if (isValidWord(token) && !temp.contains(token) && !blackList.contains(token)){
                    countWordHam(token);
                    temp.add(token);
                }
            }
        }
    }

    /**
     * Recursively reads spam training files, calls validWord() to check if words are
     * valid, then creates a list of words and how many files those words occur in.
     * <p>
     * Populates a spam frequency map using the above information
     *
     * @param file file path of files being parsed
     * @throws IOException
     */
    public void parseSpam(File file) throws IOException {
        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseSpam(current);
            }
        } else {
            //creating temp list
            List<String> temp = new ArrayList<>();
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                if (isValidWord(token) && !temp.contains(token) && !blackList.contains(token)){
                    countWordSpam(token);
                    temp.add(token);
                }
            }
        }
    }

    /**
     * checks if a word is valid using regular expressions to
     * check if the string contains only letters a-z or A-Z
     * @param word the word that is being checked for validity
     * @return returns a word that has been verified to be valid
     */
    private static boolean isValidWord(String word) {
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);
    }

    /**
     * Populates trainHamFreq map
     * <p>
     * if the word taken as an argument is found then +1 is added to that word's
     * frequency. Else if that word is already in the list, it is started at
     * a frequency of 1
     * @param word a valid word that is having its frequency recorded
     */
    private void countWordHam(String word){
        if(trainHamFreqs.containsKey(word)){
            int previous = trainHamFreqs.get(word);
            trainHamFreqs.put(word, previous+1);
        }else{
            trainHamFreqs.put(word, 1);
        }
    }

    /**
     * Populates trainSpamFreq map
     * <p>
     * if the word taken as an argument is found then +1 is added to that word's
     * frequency. Else if that word is already in the list, it is started at
     * a frequency of 1
     * @param word a valid word that is having its frequency recorded
     */
    private void countWordSpam(String word){
        if(trainSpamFreqs.containsKey(word)){
            int previous = trainSpamFreqs.get(word);
            trainSpamFreqs.put(word, previous+1);
        }else{
            trainSpamFreqs.put(word, 1);
        }
    }

    /**
     * Method to determine the probability that a specific word
     * appears in a spam file.
     * <p>
     * For each word in trainSpamFreqs the probability of that word being in a spam
     * file is calculated using the formula
     * (Number of spam files word appears in)/(total number of spam files)
     */
    public void spamProb() {
        Set<String> keys = trainSpamFreqs.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            Double count = 502.0;
            // testing minimum number of occurances

            int spamKey = trainSpamFreqs.get(key);
            double fileMath = spamKey / count;
            spamProbabilty.put(key, fileMath);
        }
    }

    /**
     * Method to determine the probability that a specific word
     * appears in a ham file.
     * <p>
     * For each word in trainHamFreqs the probability of that word being in a ham
     * file is calculated using the formula
     * (Number of ham files word appears in)/(total number of ham files)
     */
    public void hamProb(){
        Set<String> keys = trainHamFreqs.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            Double count = 2754.0;
            // testing minimum number of occurances

            int hamKey = trainHamFreqs.get(key);
            double fileMath = hamKey/count;
            hamProbabilty.put(key,fileMath);
        }


    }


    /**
     * checks the probability that a file is spam, given that it
     * contains a specific word
     * <p>
     * Iterates over trainHamFreq. If given word is in both ham and spam freq maps, calculate the file it
     * is contained in is spam with formula
     * (probability word is spam)/((probability word is spam)+(probability word is ham))
     * then map to probabilityFileIsSpam
     * <p>
     * The same process is then repeated for trainSpamFreq
     */
    public void checkFileIsSpam(){
        Set<String> hamkeys = trainHamFreqs.keySet();
        Iterator<String> hamkeyIterator = hamkeys.iterator();

        while (hamkeyIterator.hasNext()) {
            String key = hamkeyIterator.next();

            if(trainSpamFreqs.containsKey(key) && trainHamFreqs.containsKey(key)){
                Double spamKey = spamProbabilty.get(key);
                Double hamKey = hamProbabilty.get(key);

                double fileMath = spamKey/(spamKey +hamKey);
                probabilityFileIsSpam.put(key,fileMath);
            }
        }

        Set<String> spamkeys = trainSpamFreqs.keySet();
        Iterator<String> spamkeyIterator = spamkeys.iterator();

        while (spamkeyIterator.hasNext()) {
            String key = spamkeyIterator.next();

            if(trainSpamFreqs.containsKey(key) && trainHamFreqs.containsKey(key)){
                Double spamKey = spamProbabilty.get(key);
                Double hamKey = hamProbabilty.get(key);

                double fileMath = spamKey/(spamKey +hamKey);
                probabilityFileIsSpam.put(key,fileMath);
            }
        }
    }

    /**
     * If given directory is valid, the probability of files in that directory being
     * spam is calculated and added to an observableList and returned
     * <p>
     * First a value, named fancyN, is calculated, which will be used to calculate the chance
     * of a file being spam.
     * <p>
     * calcFileIsSpam is called and uses this exponent to calculate whether the file is spam
     * <p>
     * using the calculation the accuracy and precision of the process is found
     * <p>
     * the name, probability of being spam, and true state of each file is then added to an observableList
     *
     * @param file path to file being parsed
     * @return observable list containing final data to be put in table
     * @throws IOException
     */
    public ObservableList<TestFile>  parseTest(File file) throws IOException {
        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                //gets name of file being read
                parseTest(current);
            }
        } else {
            //creating temp list
            
            //exponent used to calculate whether or not a file is spam
            fancyN = 0.0;
            List<String> temp = new ArrayList<>();

            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();

                if (isValidWord(token) && !temp.contains(token) && !blackList.contains(token)){
                    calcFileIsSpam(token);
                    temp.add(token);
                }
            }
        }

        double fileMath = 1/(1+ Math.pow(Math.E, fancyN));

        numberofFiles +=1;

        if(fileMath < 0.5 && file.getParentFile().getName().equals("ham")){
            numTrueNegatives += 1;
        }

        if(fileMath > 0.5 && file.getParentFile().getName().equals("spam")){
            numTruePositives +=1;
        }

        if(fileMath < 0.5 && file.getParentFile().getName().equals("spam")){
            numFalsePositives +=1;
        }
        if(numberofFiles != 0 && numFalsePositives != 0 && numTruePositives != 0) {
            Accuracy = (numTrueNegatives + numTruePositives) / numberofFiles;
            Precision = numTruePositives / (numFalsePositives + numTruePositives);
        }
        if(fileClasses.size() <2799) {
            fileClasses.add(new TestFile(file.getName(), fileMath, file.getParentFile().getName()));
        }
        return fileClasses;
    }

    /**
     * performs a calculation to determine whether a file is spam, using the
     * exponent found in parseTest()
     * @param word the current word being parsed by parseTest()termterte
     */
    private static void calcFileIsSpam(String word){
        if(probabilityFileIsSpam.containsKey(word)){
            fancyN += Math.log(1-probabilityFileIsSpam.get(word)) - Math.log(probabilityFileIsSpam.get(word));
        }
    }
}