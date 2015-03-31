package com.fsqdataapp;

import java.util.*;

public class Photo {

	public String prefix, suffix;
	public long width, height;

	public Photo() {}

    public String printPhoto() {

    	String link = prefix + "original" + suffix;
    	String retString = "Photo link: " + link + "\nwidth:" + width + ", height: " + height;

    	return retString;

    }
	
}

