package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

public class EndpointsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
    
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String testId = req.getParameter("id");

	  List<VtVenue> venues = ofy().load().type(VtVenue.class).filter("lat >", 0).list();

    //for (VtVenue venue : venues) {
      
      // convert java object to JSON format,
      // and returned as JSON formatted string
      Gson gson = new Gson();
      String json = gson.toJson(venues);
 	
      response.getWriter().println(json);	
    

    System.out.println(venues.size());    	
    
  }
}