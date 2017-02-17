import java.util.ArrayList;
import java.util.HashMap;

public class WordAnalyser {

    public static void main(String[] args) {
        try {

            //input path
            String path = args[0];

            //new NgramCounter to find the number of ngrams
            NgramCounter nc = new NgramCounter(path);

            //obtain the hashmap from ngram counter
            HashMap hm = nc.getHashmap();

        }
        //pass in the file
        //run the ngram counter
        //output the file

    }
}