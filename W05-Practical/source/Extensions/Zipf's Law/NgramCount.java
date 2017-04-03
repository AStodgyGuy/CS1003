import java.util.LinkedHashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class NgramCount {

    private LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>();

    public NgramCount(String path) {

        String trigramExpression = "";

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
                            trigramExpression = lineArray[i].toString();
                            addToHashmap(hm, trigramExpression);
                        }
                    }
                }
                //read the next line
                line = br.readLine();
            }

            //close the BufferedReader
            br.close();

            //add the last ngram
            addToHashmap(hm, trigramExpression);

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
        Integer value = hm.get(trigramExpression);
        if (value == null) {
            hm.put(trigramExpression, 1);
        } else {
            hm.put(trigramExpression, ++value);
        }
    }
}
