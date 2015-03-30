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
public class SourceServlet extends HttpServlet {
 
  private String message;

  public void init() throws ServletException {
      // Do required initialization
      message = "Hello World";
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	
  	// Initialize conn with Foursquare
	URL url = new URL("https://api.foursquare.com/v2/venues/explore?ll=40.6211925,22.9460273&limit=2&venuePhotos=1&oauth_token=C0G5VFZ3V44UFHAIMSDW20ER0CEPWEBTYCJWCCV0M0FO0CHO&v=20141224");
	URLConnection urlConn = url.openConnection();

	// The venues data is the response wanted
  	// First, set response content type. In this case we know it's JSON
  	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	
	// Gets the response (JSON)
	Object content = urlConn.getContent();
  	String contentType = urlConn.getContentType();
  	
  	// Reads the response
	BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

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
	Venue fsqVenue = obj.response.groups.get(0).items.get(0).venue;
   	VtVenue vtVenue = new VtVenue();

	vtVenue = FsqToVt(fsqVenue);

    ofy().save().entity(vtVenue).now();

	// print it inside the servlet content
	PrintWriter servletOutput = response.getWriter();
	servletOutput.println(vtVenue.print());

	// Close the output session
	servletOutput.close();
	cgiOutput.close();

  }
  
  public VtVenue FsqToVt(Venue input) {

    String photoUrl = input.photos.groups.get(0).items.get(0).prefix + input.photos.groups.get(0).items.get(0).suffix;

  	VtVenue output = new VtVenue(photoUrl);

  	output.id = input.id;
    output.name = input.name;
    output.categories = input.categories;
    output.location = input.location;
    output.stats = input.stats;
    output.url = input.url;
    output.rating = input.rating;
    output.ratingColor = input.ratingColor;
    output.ratingSignals = input.ratingSignals;
    output.hours = input.hours;
    
    return output;
  }

  public void destroy()
  {
      // do nothing.
  }
}
