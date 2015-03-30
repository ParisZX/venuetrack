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
  public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
    
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    VtVenue test = ofy().load().type(VtVenue.class).id("4bd9a4d1d2cbc928b330d1ad").now();
    response.getWriter().println(test.print());	
    
  }
}