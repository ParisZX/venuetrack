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

    // Creating the endpoints of the whole backend. First we need to set the response type as JSON
    // with UTF-8 encoding
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // Prepare the server output writer
    PrintWriter servletOutput = response.getWriter();

    if (req.getParameter("what").equals("venue")) {

      String venueId = req.getParameter("id");

  	  Venue venue = ofy().load().type(Venue.class).id(venueId).now();

      // convert java object to JSON format,
      // and returned as JSON formatted string
      Gson gson = new Gson();
      String json = gson.toJson(venue);

      response.getWriter().println(json);
    }

    else if (req.getParameter("what").equals("venues")) {

      if (req.getParameter("filterBy") != null) {
        if (req.getParameter("filterBy").equals("location")) {
          String latUpLim = req.getParameter("latUpLim");
          String latLowLim = req.getParameter("latLowLim");
          String lngUpLim = req.getParameter("lngUpLim");
          String lngLowLim = req.getParameter("lngLowLim");

          if (req.getParameter("limit") == null)
                  servletOutput.println("You need to specify a limit");
          else {

            int limit = Integer.parseInt(req.getParameter("limit"));

            List<Venue> venues = ofy().load().type(Venue.class).filter("lat >",latLowLim).filter("lat <",latUpLim).filter("lng >",lngLowLim).filter("lng <",lngUpLim).limit(limit).list();

            // convert java object to JSON format,
            // and returned as JSON formatted string
            Gson gson = new Gson();
            String json = gson.toJson(venues);

            response.getWriter().println(json);

          }
        }
      }
      else {
        List<Venue> venues = ofy().load().type(Venue.class).limit(10).list();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        Gson gson = new Gson();
        String json = gson.toJson(venues);

        response.getWriter().println(json);

      }
    }
    else if (req.getParameter("what").equals("tips")) {
      if (req.getParameter("type").equals("full")) {
        List<Tip> tips = ofy().load().type(Tip.class).list();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        Gson gson = new Gson();
        String json = gson.toJson(tips);
        response.getWriter().println(json);
      }
      else if (req.getParameter("type").equals("condensed")) {
        List<Tip> tips = ofy().load().type(Tip.class).list();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        Gson gson = new Gson();
        String id = new String();
        String text = new String();

        for(Tip tip : tips) {
          id = gson.toJson(tip.id);
          response.getWriter().println(id);
          text = gson.toJson(tip.text);
          response.getWriter().println(text);
        }
      }
      else {
        response.getWriter().println("Error: Wrong parameters set!");
      }
    }
    else {
      System.out.println("nothing to do...");
    }


  }
}
