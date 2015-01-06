package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

public class Location {

	public double lat, lng;
	public String address, postalCode, cc, city, state, country;

    public Location() {} // There must be a no-arg constructor

    public String print() {

    	return "Latitude: " + lat + "\nLongitude: " + lng + "\n" + address + ", " + city + "\n" + postalCode + ", " + state + ", " + country;

    }

}