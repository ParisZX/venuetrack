package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
public class Venue {

    @Id public String id; // Can be Long -if null then autogenerates-, long, or String
    public String name;
    @Index public double lat;
    @Index public double lng;
    public List<Category> categories = new ArrayList<Category>();
    public Location location = new Location();
    public transient Stats stats = new Stats();
    public transient String url;
    public transient float rating;
    public transient String ratingColor;
    public transient long ratingSignals;
    public transient Hours hours = new Hours();
    public transient Photos photos = new Photos();
    public transient String lastUpdated;

    public Venue() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        lastUpdated = dateFormat.format(date);
        lat = location.lat; lng = location.lng;

    }

    public String print() {

        String retString = "Venue\n===================================\n" + "id: " + id + "\nname: " + name + "\n";

        for (Category category : categories) {
           retString = retString + category.print()+"\n";
        }

        retString = retString + location.print() + "\n" + stats.print() + "\n" + url + "\n" + ratingColor + "\n" + ratingSignals + "\n" + hours.print() + "\n" + photos.print();

        return retString + "\nLast Updated: " + lastUpdated + "\nlat: " + lat + " lng: " + lng + "\n===================================\n";

    }

}
