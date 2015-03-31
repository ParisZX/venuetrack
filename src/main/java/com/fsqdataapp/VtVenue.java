package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
public class VtVenue {
    
    @Id public String id; // Can be Long -if null then autogenerates-, long, or String
    public String name;
    @Index public double lat, lng;
    public List<Category> categories = new ArrayList<Category>();
    public Location location = new Location();
    public Stats stats = new Stats();
    public String url;
    public float rating;
    public String ratingColor;
    public long ratingSignals;
    public String hours;
    public String photo;
    public String lastUpdated;

    public VtVenue() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        lastUpdated = dateFormat.format(date);
    }
    
    public String print() {

        String retString = "Venue\n===================================\n" + "id: " + id + "\nname: " + name + "\n";

        for (Category category : categories) {
           retString = retString + category.print()+"\n";
        }

        String photoPrint;

        retString = retString + location.print() + "\n" + stats.print() + "\n" + url + "\n" + ratingColor + "\n" + ratingSignals + "\n" + hours + "\n" + photo;

        return retString + "\nLast Updated: " + lastUpdated + "\n===================================\n";

    }

}
