package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import com.google.gson.*;

// Extend HttpServlet class
public class TrainServlet extends HttpServlet {

  private String message;
  public boolean FILTER_STOP_WORDS = true; // this gets set in main()
  public List<String> stopList = readFile(new File("data/words.stop"));
  public int numFolds = 10;


  public void init() throws ServletException {
      // Do required initialization
      message = "Hello World";
  }

  private List<String> readFile(File f) {
    try {
      StringBuilder contents = new StringBuilder();

      BufferedReader input = new BufferedReader(new FileReader(f));
      for(String line = input.readLine(); line != null; line = input.readLine()) {
        contents.append(line);
        contents.append("\n");
      }
      input.close();

      return segmentWords(contents.toString());

    }
    catch(IOException e) {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  private List<String> segmentWords(String s) {
    List<String> ret = new ArrayList<String>();

    // Print string before segmentation
    // System.out.println(s);

    // Break string into words
    for (String word: preprocessString(s)) {
      if(word.length() > 0) {
        ret.add(word);
      }
    }

    // Print string after segmentation
    // System.out.println(ret);

    return ret;
  }

  private String[] preprocessString(String s) {
    s = s.toLowerCase();

    // remove numbers
    // s = s.replaceAll("[0-9]","");

    // remove prices
    s = s.replaceAll("\\^(€+)","");

    // remove greek diacritics cause not everybody uses them :D
    s = s.replaceAll("ϋ","υ").replaceAll("ϊ","ι");
    s = s.replaceAll("ΰ","υ").replaceAll("ΐ","ι");
    s = s.replaceAll("ώ","ω").replaceAll("ύ","υ").replaceAll("ή","η").replaceAll("ί","ι").replaceAll("έ","ε").replaceAll("ό","ο").replaceAll("ά","α").replaceAll("ς","σ");

    // remove multiple occurances of the same character (ooooo's and aaaaa's, but no less that 3, so that we won't mess with words like good, book etc)
    s = s.replaceAll("(.)\\1\\1+","$1");

    return s.replaceAll("^[~^,.:!();\'\"\\s]+", "").split("[~^,.:!();\'\"\\s]+");
  }

  private List<TrainSplit> buildSplits(List<String> args) {

    // Get the directory with the dataset, in which the pos/neg directories are
    File trainDir = new File(args.get(0));

    // Create the splits
    List<TrainSplit> splits = new ArrayList<TrainSplit>();

    // System.out.println("[INFO]\tPerforming 10-fold cross-validation on data set: "+args.get(0));

    // A list with all the files for splitting
    List<File> files = new ArrayList<File>();

    for (File dir: trainDir.listFiles()) {
      List<File> dirList = Arrays.asList(dir.listFiles());

      for (File f: dirList) {
        files.add(f);
	    }
    }

    splits = getFolds(files);
    return splits;
  }

  public List<TrainSplit> getFolds(List<File> files) {

    List<TrainSplit> splits = new ArrayList<TrainSplit>();

    for (Integer fold=0; fold<numFolds; fold++ ) {

      TrainSplit split = new TrainSplit();

      for (File file: files) {
        int endOfName = file.getName().length();

        // Based on the names of the comments of the dataset used for training, the 5th character from the end is a 0-9 number
        // which can be used to grab one tenth of the comments to create the testset for each fold.
        if( file.getName().subSequence(endOfName-5,endOfName-4).equals(fold.toString()) ) {
          split.test.add(file);
        }
        else {
          split.train.add(file);
        }
      }

      splits.add(split);
    }

    return splits;
  }

  public List<String> filterStopWords(List<String> words) {
    List<String> filtered = new ArrayList<String>();

    for (String word :words) {
      if (!stopList.contains(word)) {
  	    filtered.add(word);
      }
    }
    return filtered;
  }

  public void printSplit(TrainSplit split) {
    // System.out.println("\t[INFO]\tSplit's train set length = " + split.train.size());
    for (File file: split.train) {
      // System.out.println(file.getName());
    }
    // System.out.println("\t[INFO]\tSplit's test set length = " + split.test.size());
    for (File file: split.test) {
      // System.out.println(file.getName());
      List<String> words = readFile(file);
      if (FILTER_STOP_WORDS) {
        words = filterStopWords(words);
      }
      // System.out.println(words);
    }
  }

  public boolean isNoise(String text) {

    // check for wifi password spamming
    if( text.contains("4sqwifi.com") || text.contains("via WiFi Sherlock") || text.contains("Wi-fi:") || text.contains("passw") || text.contains("Wi-Fi :") ) {
      return true;
    }

    // check for ads and offers
    if ( text.contains("www.") || text.contains(".com") || text.contains(".gr") ) {
      return true;
    }

    // remove cyrillic comments, the classifier cant use them
    for ( int i = 0; i < text.length(); i++ ) {
        if(Character.UnicodeBlock.of(text.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
            return true;
        }
    }

    return false;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    NaiveBayesClassifier classifier = new NaiveBayesClassifier();

    List<String> dataset = new ArrayList<String>();

    // First, set response content type. In this case we know it's JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // Prepare the server output writer
    PrintWriter servletOutput = response.getWriter();

    servletOutput.println("Hi there, I'm the Servlet that trains the classifier");

    // Define the dataset
    dataset.add("data/venuetrack-train");

    servletOutput.println("[INFO]\tStop words filtering = "+FILTER_STOP_WORDS);

    servletOutput.println("[INFO]\tDataset length: " + dataset.size());

    // Build the train splits for 10-fold cross-validation
    List<TrainSplit> splits = buildSplits(dataset);
    double avgAccuracy = 0.0;
    int fold = 0;

    for(TrainSplit split: splits) {

      servletOutput.println("[INFO]\tFold " + fold);

      // Use printSplit function for testing purposes only
      //printSplit(split);

      double accuracy = 0.0;

      for(File file: split.train) {
        String klass = file.getParentFile().getName();
        List<String> words = readFile(file);
        if (FILTER_STOP_WORDS) {
          words = filterStopWords(words);
        }
        classifier.addExample(klass,words);
      }

      for (File file : split.test) {
        String klass = file.getParentFile().getName();
        List<String> words = readFile(file);
        if (FILTER_STOP_WORDS) {
          words = filterStopWords(words);
        }
        String guess = classifier.classify(words);
        if(klass.equals(guess)) {
          accuracy++;
        }
      }
      accuracy = accuracy/split.test.size();
      avgAccuracy += accuracy;
      servletOutput.println("[INFO]\tFold " + fold + " Accuracy: " + accuracy);
      fold += 1;
    }
    avgAccuracy = avgAccuracy / numFolds;
    servletOutput.println("[INFO]\tAccuracy: " + avgAccuracy);

    List<Venue> venues = ofy().load().type(Venue.class).list();

    for (Venue venue: venues) {
      // servletOutput.println("[INFO]\tVenue: " + venue.name);

      List<Tip> tips = ofy().load().type(Tip.class).filter("venueId",venue.id).list();

      double count = 0; double rating = 0;

      for (Tip tip: tips) {

        if(!isNoise(tip.text)) {
          String guess = classifier.classify(segmentWords(tip.text));

          if (guess == "pos") {
            rating++;
          }

          // servletOutput.println("[INFO]\t\tTip: " + tip.text + "\t\t polarity: " + guess);
          count++;
        }
      }
      if (count>0) {
        rating = rating/count;

        // servletOutput.println("[INFO]\t Rating: " + venue.rating + " and Venuetrack Rating: " + rating);

        if (rating > 0.5) {
          venue.venuetrackRating = "pos";
        }
        else {
          venue.venuetrackRating = "neg";
        }
      }
      else {
        venue.venuetrackRating = "zeroTips";

        // servletOutput.println("[INFO]\t zero useful tips");

      }
      ofy().save().entity(venue).now();
    }

    // Close the output session
    servletOutput.close();

  }

  public void destroy() {
      // do nothing.
  }
}
