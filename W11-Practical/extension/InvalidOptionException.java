
/**
 * This class is an exception class that is thrown when a user makes an invalid 
 * choice
 */

public class InvalidOptionException extends Exception {

    //error message
    private String message = "Invalid choice made";

    /**
     * Constructor
     */
    public InvalidOptionException() {}


    /**
     * Method to return the message
     */
    public String getMessage() {
        return message;
    }
}