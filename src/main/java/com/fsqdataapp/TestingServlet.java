package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestingServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    if (req.getParameter("testing") == null) {

      resp.setContentType("text/plain");
      resp.getWriter().println("Hello, this is a testing servlet. \n\n");
      
      ofy().save().entity(new Venue("123123", "red")).now();
	    Venue c = ofy().load().type(Venue.class).id("123123").now();
	    resp.setContentType("text/plain");
      resp.getWriter().println(c.id);
    } 
    else {

		Venue d = ofy().load().type(Venue.class).id("123123").now();
		resp.setContentType("text/plain");
    	resp.getWriter().println(d.id);	

		//ofy().delete().entity(c);
		      	  
    }
  }
}