import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class WordAnalyser {

    public static void main(String[] args) {
        try {

            //input path
            String path = args[0];

            //new NgramCounter to find the number of ngrams
            NgramCount nc = new NgramCount(path);

            //obtain the hashmap from ngram counter
            LinkedHashMap<String, Integer> hm = nc.getHashMap();

            //sort the hashmap
            hm = sortHashMap(hm);

            //write the contents of the hashmap as a csv
            writeFile(hm, args[1]);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file> <output file>");
        }
    }

    /*
     * Method which sorts the hashmap using the Stream API
    */
    public static LinkedHashMap<String, Integer> sortHashMap(LinkedHashMap<String, Integer> hm) {
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
