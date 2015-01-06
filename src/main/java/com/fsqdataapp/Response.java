package com.fsqdataapp;

import java.util.*;

public class Response {

	public List<Group> groups = new ArrayList<Group>();

    public Response() {}
    
    public String print() {

    	String retString = "";

    	for (Group group : groups) {
 		   retString = retString + group.print()+"\n\n";
		}

		return retString;

    }

}
