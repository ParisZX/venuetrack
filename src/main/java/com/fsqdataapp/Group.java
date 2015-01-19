package com.fsqdataapp;

import java.util.*;

public class Group {

	public List<Item> items = new ArrayList<Item>();

	public Group() {}

	public String print() {

    	String retString = "";

    	for (Item item : items) {
 		   retString = retString + item.print()+"\n\n";
		}

		return retString;

    }

    public String printPhoto() {

    	String retString = "";

    	for (Item item : items) {
 		   retString = retString + item.printPhoto();
		}

		return retString;

    }	
}