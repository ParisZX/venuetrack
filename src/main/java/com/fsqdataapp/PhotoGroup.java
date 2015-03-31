package com.fsqdataapp;

import java.util.*;

public class PhotoGroup {

	// need to all them items, even tho they are photos, for gson to be able to deserialize
	public List<Photo> items = new ArrayList<Photo>();

	public PhotoGroup() {}

    public String printPhoto() {

    	String retString = "";

    	for (Photo item : items) {
 		   retString = retString + item.printPhoto();
		}

		return retString;

    }	
}