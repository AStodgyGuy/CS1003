/**
 * This class takes the first argument as the twitter username the user is searching for and the second argument
 * is the output folder that hadoop needs to output its MapReduced result.
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.LocalJobRunner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import twitter4j.TwitterException;


public class W11PracticalExt {

	public static void main(String[] args) throws IOException {

		// This produces an output file in which each line contains a separate word followed by
		// the total number of occurrences of that word in all the input files.
		
		if(args.length < 2) {
			System.out.println("Usage: java -cp " + "\"lib/*:bin\"" + " W11PracticalExt <twitter_username> <output_path>");
			System.exit(1);
		}

		try {
			String twitter_username = args[0];
			String output_path = args[1];

			GetUserData gud = new GetUserData(twitter_username);
			String input_path = gud.getOutputPath();
			
			// Setup new Job and Configuration 
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "W11PracticalExt");
			
			// Specify input and output paths
			FileInputFormat.setInputPaths(job, new Path(input_path));
			FileOutputFormat.setOutputPath(job, new Path(output_path));

			// Set our own ScanWordsMapper as the mapper
			job.setMapperClass(MyMapper.class);

			// Specify output types produced by mapper (words with count of 1)
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(LongWritable.class);

			// The output of the reducer is a map from unique words to their total counts.
			job.setReducerClass(MyReducer.class);

			// Specify the output types produced by reducer (words with total counts)
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(LongWritable.class);
			job.waitForCompletion(true);

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (TwitterException e) {
			System.out.println(e.getMessage());
		} catch (InvalidOptionException e) {
			System.out.println(e.getMessage());
		}
	}
}
