package sample;

import java.text.DecimalFormat;

/**
 * This was provided as a structure for a file to test the
 * spam filter on.
 * <p>
 * The class must be instantiated with a filename,
 * spam probability, and whether or not the file is spam
 * @param filename the name of the file that is being read
 * @param SpamProbability the probability that the file is spam
 * @param ActualClass whether or not the file actually is spam based off the folder it is in
 */
public class TestFile {
    public String Filename;
    public double SpamProbability;
    public String ActualClass;

    public TestFile(String filename, double spamProbability, String actualClass) {
        this.Filename = filename;
        this.SpamProbability = spamProbability;
        this.ActualClass = actualClass;
    }

    public String getFilename(){
        return this.Filename;
    }

    public double getSpamProbability(){
        return this.SpamProbability;
    }

    public String getSpamProbRounded(){
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.SpamProbability);
    }

    public String getActualClass(){
        return this.ActualClass;
    }

    public void setFilename(String value) {
        this.Filename = value;
    }

    public void setSpamProbability(double val) {
        this.SpamProbability = val;
    }

    public void setActualClass(String value) {
        this.ActualClass = value; }
}