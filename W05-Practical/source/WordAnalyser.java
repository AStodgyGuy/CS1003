import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;


public class WordAnalyser {

    public static void main(String[] args) {
        try {

            //input path
            String path = args[0];

            //new NgramCounter to find the number of ngrams
            NgramCount nc = new NgramCount(path);

            //obtain the hashmap from ngram counter
            HashMap<String, Integer> hm = nc.getHashMap();

            //write the contents of the hashmap as a csv
            writeFile(hm, args[1]);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
        }
    }

    public static void writeFile(HashMap<String, Integer> hm, String exportPath) {

        try {

            FileWriter fw = new FileWriter(exportPath);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            //for every string in the hashmap out the string and its corresponding value
            for (String w : hm.keySet()) {
                pw.println("\"" + w + "\"," + hm.get(w));
            }

            //close the file
            pw.close();

        } catch (IOException e) {
            System.out.println("Unable to create new file");
        }
    }

}