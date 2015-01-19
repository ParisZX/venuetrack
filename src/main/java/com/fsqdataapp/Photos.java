package com.fsqdataapp;

import java.util.*;

public class Photos {

	public List<Group> groups = new ArrayList<Group>();

    public Photos() {}
    
    public String print() {

    	String retString = "";

    	for (Group group : groups) {
 		   retString = retString + group.printPhoto();
		}

		return retString;

    }

}
