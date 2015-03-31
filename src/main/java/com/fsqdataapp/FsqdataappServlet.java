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
    
    // Initialize conn with Foursquare
    Random rand = new Random();

    // Initialize the filters used in the Foursquare Endpoints request url
    double latitude = (rand.nextDouble() - 0.5) + 40.6372645; 
    double longitude = (rand.nextDouble()/5 - 0.1) + 22.9374991;
    int price = rand.nextInt(4); int limit = 1; int venuePhotos = 1;

    // Prepare the filters string
    String filters = "ll=" + latitude + "," + longitude + "&limit=" + limit + "&venuePhotos=" + venuePhotos;
    if (price>0)
       filters = filters + "&price=" + price;

    // Authentication Token needed by Foursquare
    String token = "&oauth_token=C0G5VFZ3V44UFHAIMSDW20ER0CEPWEBTYCJWCCV0M0FO0CHO&v=20141224";

    URL url = new URL("https://api.foursquare.com/v2/venues/explore?" + filters + token);
    URLConnection urlConn = url.openConnection();

    // The venues data is the response wanted
    // First, set response content type. In this case we know it's JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    // Reads the response and prepares to write output
    BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));
    PrintWriter servletOutput = response.getWriter();

    // print the point we ping this time
    servletOutput.println("Ping latitude: " + latitude + " and longitude: " + longitude + "\n");

    // Try the JSON/GSON convertion
    Gson gson = new Gson();

    // Convert the json string to object. The classes are made specifically so we keep only
    // the info we need.
    JSONObject obj = gson.fromJson(cgiOutput, JSONObject.class);
    
    /* 
    The Venue class as defined from Foursquare has a problem: there is an unbounded recursion with the classes Group and Items.
    This means that Objectify throws a StackoverflowError if the Venue class is used for saving the data. So the solution I used
    is that I created a seperate class VtVenue, which is actually the venue from Venuetrack's perspective, without using embeded
    Group and Items classes for the photo URL. Instead, I just create the String with the URL and pass it on to the VtVenue 
    object as a String. That's what FsqToVt does. 
    */
    List<Item> items = new ArrayList<Item>();
    items = obj.response.groups.get(0).items;

    for (Item item : items) {
      
      Venue fsqVenue = item.venue;
      VtVenue vtVenue = new VtVenue();
      vtVenue = FsqToVt(fsqVenue);

      // save the final venuetrack venue
      ofy().save().entity(vtVenue).now();

      // print it inside the servlet content
      servletOutput.println(vtVenue.print());

      // let's get the tips for each venue. First, prepare the request url for foursquare
      URL tipsUrl = new URL("https://api.foursquare.com/v2/venues/" + vtVenue.id + "/tips?sort=recent&limit=100"+token);
      URLConnection tipsUrlConn = tipsUrl.openConnection();
      
      // then, we read the contents of the output
      BufferedReader tipsOutput = new BufferedReader(new InputStreamReader(tipsUrlConn.getInputStream(),"UTF-8"));
      
      // and finally, using gson we make our raw data into objects
      JSONObject newObj = gson.fromJson(tipsOutput, JSONObject.class);
      List<Tip> tips = new ArrayList<Tip>();
      tips = newObj.response.tips.items;
      
      for (Tip tip : tips) {
        // save the tip
        ofy().save().entity(tip).now();
        servletOutput.println(tip.print());  
      }
    }

    // Close the output session
    servletOutput.close();
    cgiOutput.close();
  }
  
  public VtVenue FsqToVt(Venue input) {
    
    VtVenue output = new VtVenue();

    output.id = input.id;
    output.name = input.name;
    output.categories = input.categories;
    output.lat = input.location.lat;
    output.lng = input.location.lng;

    output.location = input.location;
    output.stats = input.stats;
    output.url = input.url;
    output.rating = input.rating;
    output.ratingColor = input.ratingColor;
    output.ratingSignals = input.ratingSignals;

    String hours = input.hours.status;
    output.hours = hours;

    String photoUrl = input.photos.groups.get(0).items.get(0).prefix + "original" + input.photos.groups.get(0).items.get(0).suffix;
    output.photo = photoUrl;

    return output;
  }

  public void destroy()
  {
      // do nothing.
  }
}
