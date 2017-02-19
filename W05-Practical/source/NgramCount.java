import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class NgramCount {

    private final int TRIGRAM_SIZE = 3;

    private Ngram anNgram;
    private HashMap<String, Integer> hm = new HashMap<String, Integer>();
    private ArrayList<String> al = new ArrayList<String>();

    public NgramCount(String path) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String[] lineArray;

            String line = br.readLine();
            while (line != null) {
                line = line.toLowerCase(); //change the line to lower case letters
                line = line.replaceAll("[^a-z ]", ""); //change all the non a-z characters to ""
                lineArray = line.split(" "); //split up the line to individual words split by spaces
                //check the line to make sure its not empty
                if (!line.isEmpty()) {
                    for (int i = 0; i < lineArray.length; i++) {
                        //check if the arraylist size is the size of the required trigram
                        if (!(lineArray[i].equals(" ") || lineArray[i].equals(""))) {
                            if (al.size() == TRIGRAM_SIZE) {
                                anNgram = new Ngram(al); //create a new trigram
                                addToHashmap(hm, anNgram); //add it to the hashmap
                                al.remove(0);   //remove the first element of the arraylist to make another trigram
                                al.add(lineArray[i]);   //add current string in array to the arraylist
                            } else {
                                al.add(lineArray[i]);
                            }
                        }
                    }
                }
                //read the next line
                line = br.readLine();
            }
            
            //close the BufferedReader
            br.close(); 

            //printing out last trigram
            if (al.size() == TRIGRAM_SIZE) {
                anNgram = new Ngram(al);
                addToHashmap(hm, anNgram);
            }
              
        } catch (IOException e) {
            System.out.println("File not found error!");
        }
    }

    public HashMap<String, Integer> getHashMap() {
        return hm;
    }

    /*
     * Method which adds the ngram identifier to a hashmap to check for occurences
    */
    private void addToHashmap(HashMap<String, Integer> hm, Ngram anNgram) {

        Integer value = hm.get(anNgram.getIdentifier());
        //if it doesn't add it to the hashmap
        if (value == null) {
            hm.put(anNgram.getIdentifier(), 1);
        //if it does, increment the hashmap value of that key by one
        //hashmaps must overwrite
        } else {
            hm.put(anNgram.getIdentifier(), hm.get(anNgram.getIdentifier()) + 1);
        }
    }

    /*
     * class for NGRAMS
    */
    class Ngram {

    private final int FIRST_STRING = 0;
    private final int SECOND_STRING = 1;
    private final int THIRD_STRING = 2;

    private String identifier;

    public Ngram(ArrayList<String> al) {
        this.identifier = al.get(FIRST_STRING) + " " + al.get(SECOND_STRING) + " " + al.get(THIRD_STRING);
    }

    public String getIdentifier() {
        return identifier;
    }
}
}
