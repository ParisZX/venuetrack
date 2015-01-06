package com.fsqdataapp;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class TargetServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";

    public void doPost(HttpServletRequest request,
		       HttpServletResponse response) throws ServletException,
		       IOException {
	response.setContentType(CONTENT_TYPE);
	PrintWriter out = response.getWriter();
	out.print("<h2>Target's output</h2><p /><pre><code>");

	Enumeration enumer = request.getParameterNames();
	while (enumer.hasMoreElements()){
	    String param = (String) enumer.nextElement();
	    String value = request.getParameter(param);
	    out.println("param=" + param + " value=" + value);
	}
	out.print("</code></pre>");
    }
}
