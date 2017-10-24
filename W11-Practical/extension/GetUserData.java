/**
 * This class retrieves all the statuses from a specific user.
 * The code is adapted from the example class provided by Twitter4J:
 * https://github.com/yusuke/twitter4j/blob/master/twitter4j-examples/src/main/java/twitter4j/examples/timeline/GetUserTimeline.java
 */

import twitter4j.HashtagEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.BufferOverflowException;
import java.util.List;
import java.util.Scanner;

public class GetUserData {

    /**
     * Generated API keys for this practical
     */
    private final String OAUTH_CONSUMER_KEY = "8Mwc7TlDs5mRyqThDUODl21dm";
    private final String OAUTH_CONSUMER_SECRET = "IeBgM3YCCUT6F04Z6IVNrJzc2ZcKnbtUVoiUxfOdsoGrMY1CtN";
    private final String OAUTH_ACCESS_TOKEN = "2389360646-VeheiVToNTfVTvigtWvJIQXu4f5fIHubPYzFVP9";
    private final String OAUTH_ACCESS_TOKEN_SECRET = "l6wE6hP8t2JQ1C9fy1XP1RQbU5CIIWcp73v1oKGPHyhHq";

    private ConfigurationBuilder cb;
    private String user;
    private String outputPath = "output.txt";

    /**
     * Constructor
     */
    public GetUserData(String user) throws IOException, TwitterException, InvalidOptionException {
        this.user = user;
        run();        
    }

    /**
     * This method initialises runs methods which outputs data to output.txt
     */
    private void run() throws IOException, TwitterException, InvalidOptionException {
        Scanner sc = new Scanner(System.in);
        setConfiguration();
        System.out.print("Type 1 to obtain frequency of words or type 2 to obtain frequency of hashtags: ");
        String userInput = sc.nextLine();

        //user wants word frequency
        if (userInput.equals("1")) {
            System.out.print("Enter the number of tweets you want to search: ");
            try {
                userInput = sc.nextLine();
                //check for negative
                if (Integer.parseInt(userInput) > 0) {
                    outputUserData(Integer.parseInt(userInput));
                } else {
                    throw new InvalidOptionException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidOptionException();
            }

        //user wants hashtag frequnecy
        } else if (userInput.equals("2")) {
            System.out.print("Enter the number of tweets you want to search: ");
            try {
                userInput = sc.nextLine();
                //check for negative
                if (Integer.parseInt(userInput) > 0) {
                    outputUserHashtags(Integer.parseInt(userInput));
                } else {
                    throw new InvalidOptionException();
                }  
            } catch (NumberFormatException e) {
                throw new InvalidOptionException();
            }
            
        } else {
            throw new InvalidOptionException();
        }
    }

    /**
     * This method sets the configuration for retrieving data from twitter
     */
    private void setConfiguration() {
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET);
        cb.setOAuthAccessToken(OAUTH_ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
    }

    /**
     * This method goes through a twitter user's most recent 80 tweets 
     * and finds the hashtags used and outputs it to output.txt
     */
    private void outputUserHashtags(int noOfStatuses) throws IOException, TwitterException {

        List<Status> statuses;
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        //obtain all the statuses
        statuses = twitter.getUserTimeline(user, new Paging(1, noOfStatuses));

        //write all of the hashtags in a status out to a txt file
        PrintWriter pw = new PrintWriter(outputPath, "UTF-8");
        for (Status status : statuses) {
            HashtagEntity[] hashtags = status.getHashtagEntities();
            if (hashtags != null) {
                for (int i = 0; i < hashtags.length; i++) {
                    pw.println(hashtags[i].getText());
                }
            }
            
        }
        pw.close();
    }

    /**
     * This method goes through a twitter user's most recent 80 tweets and 
     * outputs everything to output.txt file
     */
    private void outputUserData(int noOfStatuses) throws IOException, TwitterException {

        List<Status> statuses;
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        //obtain all the statuses
        statuses = twitter.getUserTimeline(user, new Paging(1, noOfStatuses));

        //write all of the statuses out to a txt file
        PrintWriter pw = new PrintWriter(outputPath, "UTF-8");
        for (Status status : statuses) {
            pw.println(status.getText());            
        }
        pw.close();
    }

    /**
     * Method to retrieve the otuput path
     */
    public String getOutputPath() {
        return outputPath;
    }
}
