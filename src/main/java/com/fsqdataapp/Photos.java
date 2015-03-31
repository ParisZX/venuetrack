package com.fsqdataapp;

import java.util.*;

public class Photos {

	public List<PhotoGroup> groups = new ArrayList<PhotoGroup>();

    public Photos() {}
    
    public String print() {

    	String retString = "";

    	for (PhotoGroup group : groups) {
 		   retString = retString + group.printPhoto();
		}

		return retString;

    }

}
