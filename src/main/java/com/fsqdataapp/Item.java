package com.fsqdataapp;

import java.util.*;

public class Item {

	public Venue venue = new Venue();
	public String prefix, suffix;
	public long width, height;

	public Item() {}

	public String print() {

		return venue.print();

    }

    public String printPhoto() {

    	String link = prefix + "original" + suffix;
    	String retString = "Photo link: " + link + "\nwidth:" + width + ", height: " + height;

    	return retString;

    }
	
}

