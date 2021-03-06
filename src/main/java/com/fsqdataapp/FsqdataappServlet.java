package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;

// Extend HttpServlet class
public class FsqdataappServlet extends HttpServlet {

  private String message;

  public void init() throws ServletException {
      // Do required initialization
      message = "Hello World";
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // First, set response content type. In this case we know it's JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // Prepare the server output writer
    PrintWriter servletOutput = response.getWriter();

    // Create a random number generator, which we will need for calculating the point we are goin to ping
    Random rand = new Random();

    // Initialize the filters used in the Foursquare Endpoints request url
    double latitude = (rand.nextDouble() - 0.5) + 40.6372645;
    double longitude = (rand.nextDouble()/5 - 0.1) + 22.9374991;
    double MAX_LAT = 40.695301; double MIN_LAT = 40.505562;
    double MAX_LNG = 23.041819; double MIN_LNG = 22.776719;
    int price = rand.nextInt(4); int limit = 50; int venuePhotos = 1;

    // Define what type of search is wanted. "explore" is better for top results in given area, "search" is more general.
    String typeOfSearch = "explore";

    // Prepare the filters string
    String filters = "ll=" + latitude + "," + longitude + "&limit=" + limit + "&venuePhotos=" + venuePhotos;
    if (price>0)
       filters = filters + "&price=" + price;

    // Authentication Token needed by Foursquare
    String token = "&oauth_token=C0G5VFZ3V44UFHAIMSDW20ER0CEPWEBTYCJWCCV0M0FO0CHO&v=20141224";

    URL url = new URL("https://api.foursquare.com/v2/venues/" + typeOfSearch + "?" + filters + token);

    // Print the link both at console & the screen
    servletOutput.println("https://api.foursquare.com/v2/venues/" + typeOfSearch + "?" + filters + token + "\n");
    System.out.println("https://api.foursquare.com/v2/venues/" + typeOfSearch + "?" + filters + token + "\n");

    try {

      // Initialize conn with Foursquare
      URLConnection urlConn = url.openConnection();

      // Reads the response and prepares to write output
      BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

      // print the point we ping this time
      servletOutput.println("Ping latitude: " + latitude + " and longitude: " + longitude + "\n");

      // Try the JSON/GSON convertion
      Gson gson = new Gson();

      // Convert the json string to object. The classes are made specifically so we keep only
      // the info we need.
      JSONObject obj = gson.fromJson(cgiOutput, JSONObject.class);

      /*
      The Venue class as defined from Foursquare has a problem if used with Objectify: there is an unbounded recursion with the
      classes Group and Items. This means that Objectify throws a StackoverflowError if the Group and Items classes are used for
      saving both the venues and the photos of the venues. So the solution I used is that I created two new "renamed" PhotoGroup
      and Photo classes, to overcome the issue. The gson deserializer doesn't care for the different class names if the variables
      are still correctly named as "groups" and "items".
      */

      List<Item> items = new ArrayList<Item>();
      items = obj.response.groups.get(0).items;

      // check if the list is empty
      if (items.isEmpty() == true)
        servletOutput.println("nothing there...");

      // if not, do stuff
      else {

        for (Item item : items) {

          Venue venue = item.venue;

          venue.lat = venue.location.lat; venue.lng = venue.location.lng;
          if (venue.lat < MAX_LAT && venue.lat > MIN_LAT && venue.lng < MAX_LNG && venue.lng > MIN_LNG) {

            // let's get the tips for each venue. First, prepare the request url for foursquare...
            URL tipsUrl = new URL("https://api.foursquare.com/v2/venues/" + venue.id + "/tips?sort=recent&limit=100"+token);
            URLConnection tipsUrlConn = tipsUrl.openConnection();

            // ...then, we read the contents of the Foursquare response...
            BufferedReader tipsOutput = new BufferedReader(new InputStreamReader(tipsUrlConn.getInputStream(),"UTF-8"));

            // ...and finally, using gson we make our raw data into objects
            JSONObject newObj = gson.fromJson(tipsOutput, JSONObject.class);

            List<Tip> tips = new ArrayList<Tip>();
            tips = newObj.response.tips.items;

            double count = 0; double rating = 0;
            NaiveBayesClassifier classifier = ofy().load().type(NaiveBayesClassifier.class).id("final").now();

            for (Tip tip : tips) {

              // define the foreign key for the tip a.k.a. where the tip belongs
              tip.venueId = venue.id;

              // save the tip...
              ofy().save().entity(tip).now();

              // ...and print the output (optionally, for testing)
              // servletOutput.println(tip.print());

              if(!isNoise(tip.text)) {
                String guess = classifier.classify(segmentWords(tip.text));

                if (guess == "pos") {
                  rating++;
                }

                servletOutput.println("[INFO]\t\tTip: " + tip.text + "\t\t polarity: " + guess);
                count++;
              }
            }

            if (count>0) {

              rating = rating/count;
              servletOutput.println("[INFO]\t Rating: " + venue.rating + " and Venuetrack Rating: " + rating);

              if (rating > 0.5) {
                venue.venuetrackRating = "pos";
              }
              else {
                venue.venuetrackRating = "neg";
              }
            }
            else {
              venue.venuetrackRating = "zeroTips";
              servletOutput.println("[INFO]\t zero useful tips");

            }
            
            // save the final venuetrack venue...
            ofy().save().entity(venue).now();

            // ...and print the output (optionally, for testing)
            servletOutput.println(venue.print());

          }
        }
      }

      // Close the output session
      servletOutput.close();
      cgiOutput.close();
    }
    catch (SocketTimeoutException e) {
      servletOutput.println("Timeout...");
    }

  }

  public void destroy()
  {
      // do nothing.
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

    // Character '
    s = s.replace("\\u0027","");

    // Character &
    s = s.replace("\\u0026","");

    // Emojis
    s = s.replaceAll(":\\)","☺").replaceAll(":-\\)","☺").replaceAll(":D","☺").replaceAll(":-D","☺").replaceAll(":P","☺").replaceAll(":-P","☺").replaceAll(";\\)","☺").replaceAll(";-\\)","☺");
    s = s.replaceAll(";\\(","☹").replaceAll(";-\\(","☹").replaceAll(":\\(","☹").replaceAll(":-\\(","☹").replaceAll(":/","☹").replaceAll(":-/","☹");

    // remove multiple occurances of the same character (ooooo's and aaaaa's, but no less that 3, so that we won't mess with words like good, book etc)
    s = s.replaceAll("(.)\\1\\1+","$1");

    // Greek spelling
    // s = s.replaceAll("(ει|η|οι|υι|υ)","ι");
    // s = s.replaceAll("ω","ο");
    // s = s.replaceAll("αι","ε");

    String[] words = s.replaceAll("^[~^,.:!();\'\"\\s]+", "").split("[~^,.:!();\'\"\\s]+");

    int i = 0;
    for (String word: words) {

      // Stemming for greek words
      word = word.replaceAll("(η|ησ|ην|ον|ου|ο|οσ|ικο|ιο|ηση|αμε|ει|εις|ιζει|ασ|μενοσ|μενη|μενεσ|σ|αση)$","");

      // Stemming for english
      word = word.replaceAll("(ious|ely|es|ice|ful|fully)$","");

      words[i] = word;
      i++;

    }

    return words;
  }

}
