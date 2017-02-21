import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;

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

            //Generate graph that shows Zipf's law
            ArrayList<Integer> al = new ArrayList<Integer>(hm.values());
            GraphPlotter gp = new GraphPlotter(al, path);

            //pack all the graph components together
            gp.pack();

            //display the graph on the centre of the screen
            RefineryUtilities.centerFrameOnScreen(gp);
            gp.setVisible(true);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file>");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid use: Java WordAnalyser <input file>");
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
}

