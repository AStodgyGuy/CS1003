import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class NgramCount {

    private HashMap<String, Integer> hm = new HashMap<String, Integer>();
    private int TRIGRAM_COUNT = 3;

    public NgramCount(String path) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String trigram_expression = "";
            String[] lineArray;
            int counter = 0;

            String line = br.readLine();
            line.toLowerCase(); //change the line to lower case letters            
            line.replaceAll("[a-z]", ""); //change all the non a-z characters to ""
            while (line != null) {
                lineArray = line.split(" "); //split up the line to individual words split by spaces
                for (int i = 0; i < lineArray.length; i++) {

                    //add the first word of the trigram expression into a string variable 
                    if (counter == 0) {
                        trigram_expression = trigram_expression + lineArray[i];
                    //add the second and third word with a space
                    } else {
                        trigram_expression = trigram_expression + " " + lineArray[i];
                    }

                    //once equal to the trigram count, check if the trigram already exists in the hasmap
                    if (counter == TRIGRAM_COUNT) {
                        Integer value = hm.get(trigram_expression);

                        //if it doesn't add it to the hashmap
                        if (value == null) {
                            hm.put(trigram_expression, 1);
                        //if it does, increment the hashmap value of that key by one
                        //hashmaps must overwrite
                        } else {
                            hm.put(trigram_expression, hm.get(trigram_expression) + 1);
                        }

                        //reset the trigram expression and counter
                        trigram_expression = "";
                        counter = 0;
                    }
                }

                //read the next line
                br.readLine();
            }

        } catch (IOException e) {
            System.out.println("File not found error!");
        }
    }

    public HashMap<String, Integer> getHashMap() {
        return hm;
    } 
}