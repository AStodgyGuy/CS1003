import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map.Entry;


public class WordAnalyser {

    public static void main(String[] args) {
        try {

            //input path
            String path = args[0];

            //new NgramCounter to find the number of ngrams
            NgramCount nc = new NgramCount(path);

            //obtain the hashmap from ngram counter
            HashMap<String, Integer> hm = nc.getHashMap();

            //sort the hashmap
            //LinkedHashMap<String, Integer> sortedHashMap = sortHashMap(hm);
            LinkedHashMap<String, Integer> sortedHashMap = sortHashMap(hm);

            //write the contents of the hashmap as a csv
            writeFile(sortedHashMap, args[1]);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
        }
    }

    public static LinkedHashMap<String, Integer> sortHashMap(HashMap<String, Integer> hm) {
        return hm.entrySet()
             .stream()
             .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()))
             .collect(Collectors.toMap(
               Map.Entry::getKey,
               Map.Entry::getValue,
               (e1, e2) -> e1,
               LinkedHashMap::new
             ));
    }

    /*
     * Method which writes the contents of the hashmap to a user specified file
    */
    public static void writeFile(LinkedHashMap<String, Integer> sortedHashMap, String exportPath) {
        try {

            FileWriter fw = new FileWriter(exportPath);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            //for every string in the hashmap out the string and its corresponding value
            for (String w : sortedHashMap.keySet()) {
                pw.println("\"" + w + "\"," + sortedHashMap.get(w));
            }

            //close the file
            pw.close();

        } catch (IOException e) {
            System.out.println("Unable to create new file");
        }
    }

}