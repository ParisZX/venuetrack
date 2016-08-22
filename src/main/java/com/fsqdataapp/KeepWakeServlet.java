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
public class KeepWakeServlet extends HttpServlet {

  private String message;

  public void init() throws ServletException {
      // Do required initialization
      message = "Hello World";
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Creating the endpoints of the whole backend. First we need to set the response type as JSON
    // with UTF-8 encoding
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // Prepare the server output writer
    PrintWriter servletOutput = response.getWriter();

    URL url = new URL("https://venuetrack.appspot.com/_ah/api/venuetrackEndpoints/v1/venue");

    try {

      // Initialize conn with Foursquare
      URLConnection urlConn = url.openConnection();

      // Reads the response and prepares to write output
      BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

      servletOutput.println("done!");

      // Close the output session
      servletOutput.close();
      cgiOutput.close();

    }
    catch (SocketTimeoutException e) {
      System.out.println("Timeout...");
    }

  }

  public void destroy()
  {
      // do nothing.
  }

}
