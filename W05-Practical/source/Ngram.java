import java.util.ArrayList;

public class Ngram {

    private final int FIRST_STRING = 0;
    private final int SECOND_STRING = 1;
    private final int THIRD_STRING = 2;

    private String identifier;

    public Ngram(ArrayList<String> al) {
        this.identifier = al.get(FIRST_STRING) + "," + al.get(SECOND_STRING) + "," + al.get(THIRD_STRING);
    }

    public String getIdentifier() {
        return identifier;
    }
}