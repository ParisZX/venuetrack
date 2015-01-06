package com.fsqdataapp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class oldSourceServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";

    public void doGet(HttpServletRequest request, 
		      HttpServletResponse response) throws ServletException, 
		      IOException {
	response.setContentType(CONTENT_TYPE);

	URL		 url;
	URLConnection    urlConn;
	DataOutputStream cgiInput;

	// URL of target page script.
	url = new URL("https://api.foursquare.com/v2/venues/explore?ll=40.7,-74&limit=1&oauth_token=C0G5VFZ3V44UFHAIMSDW20ER0CEPWEBTYCJWCCV0M0FO0CHO&v=20141224");
	urlConn = url.openConnection();

	// urlConn.setDoInput(true);
	urlConn.setDoOutput(true);
	urlConn.setUseCaches(false);
	urlConn.setRequestProperty("Content-Type", 
				   "application/json");

	// Send POST output.
	// cgiInput = new DataOutputStream(urlConn.getOutputStream());

	// reads the CGI response and print it inside the servlet content
	BufferedReader cgiOutput = 
	    new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	PrintWriter    servletOutput = response.getWriter();
	String json = request.getParameter("meta");        
	servletOutput.print("<html><body><h1>This is the Source Servlet</h1><p />");
	String line = null;
	while (null != (line = cgiOutput.readLine())){
	    servletOutput.println(line);
	}
	cgiOutput.close();
	servletOutput.println("<br/><br/>");
	servletOutput.println(json);
	servletOutput.print("</body></html>");
	servletOutput.close();
    }
}
