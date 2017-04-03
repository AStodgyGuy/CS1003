import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class NgramCount {

    private static final int TRIGRAM_SIZE = 3;

    private static final int FIRST_STRING = 0;
    private static final int SECOND_STRING = 1;
    private static final int THIRD_STRING = 2;

    private LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>();

    public NgramCount(String path) {

        String trigramExpression;
        ArrayList<String> al = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String[] lineArray;

            String line = br.readLine();
            while (line != null) {
                //change the line to lower case letters
                line = line.toLowerCase();

                //change all the non a-z characters to ""
                line = line.replaceAll("[^a-z ]", "");

                //split up the line to individual words split by spaces
                lineArray = line.split(" ");

                //check the line to make sure its not empty
                if (!line.isEmpty()) {
                    for (int i = 0; i < lineArray.length; i++) {
                        //check for empty spaces and blanks
                        if (!(lineArray[i].equals(" ") || lineArray[i].equals(""))) {
                            //check if the arraylist size is the size of the required trigram
                            if (al.size() == TRIGRAM_SIZE) {
                                trigramExpression = al.get(FIRST_STRING) + " " + al.get(SECOND_STRING) + " " + al.get(THIRD_STRING);
                                //add trigram expression to the hashmap
                                addToHashmap(hm, trigramExpression);
                                 //remove the first element of the arraylist to make another trigram since arrayLists dyamically update
                                al.remove(FIRST_STRING);
                                //add current string in array to the arraylist
                                al.add(lineArray[i]);
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

            //adding last trigram to the hashmap
            if (al.size() == TRIGRAM_SIZE) {
                trigramExpression = al.get(FIRST_STRING) + " " + al.get(SECOND_STRING) + " " + al.get(THIRD_STRING);
                addToHashmap(hm, trigramExpression);
            }

        } catch (IOException e) {
            System.out.println("File not found error!");
        }
    }

    /*
     * Method which returns the hashmap
    */
    public LinkedHashMap<String, Integer> getHashMap() {
        return hm;
    }

    /*
     * Method which checks if the expression already exists in the hashmap and either adds it or incrememnts it
    */
    private void addToHashmap(LinkedHashMap<String, Integer> hm, String trigramExpression) {
        //check if the trigram has an entry in the hashmap
        Integer value = hm.get(trigramExpression);
        if (value == null) {
            hm.put(trigramExpression, 1);
        } else {
            hm.put(trigramExpression, ++value);
        }
    }
}
