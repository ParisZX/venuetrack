package com.fsqdataapp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;


// Extend HttpServlet class
public class SourceServlet extends HttpServlet {
 
  private String message;

  public void init() throws ServletException
  {
      // Do required initialization
      message = "Hello World";
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
  	// Set response content type
  	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	
	// Initialize conn with Foursquare
	URL url = new URL("https://api.foursquare.com/v2/venues/explore?ll=40.6211925,22.9460273&limit=2&venuePhotos=1&oauth_token=C0G5VFZ3V44UFHAIMSDW20ER0CEPWEBTYCJWCCV0M0FO0CHO&v=20141224");
	URLConnection urlConn = url.openConnection();
	
	// Get the response (JSON)
	Object content = urlConn.getContent();
  	String contentType = urlConn.getContentType();
  	

  	// Actual logic goes here.
  	
  	// reads the CGI response and print it inside the servlet content
	BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));
	PrintWriter servletOutput = response.getWriter();
	
	// Try the JSON/GSON convertion
	Gson gson = new Gson();

	// convert the json string back to object
	JSONObject obj = gson.fromJson(cgiOutput, JSONObject.class);
	Venue firstVenue = obj.response.groups.get(0).items.get(0).venue;
	 
	servletOutput.println(obj.print());
	servletOutput.close();
	cgiOutput.close();

  }
  
  public void destroy()
  {
      // do nothing.
  }
}
