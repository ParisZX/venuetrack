package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

@Entity
public class VtVenue {
    
    @Id public String id; // Can be Long -if null then autogenerates-, long, or String
    public String name;
    public List<Category> categories = new ArrayList<Category>();
    public Location location = new Location();
    public Stats stats = new Stats();
    public String url;
    public float rating;
    public String ratingColor;
    public long ratingSignals;
    public Hours hours = new Hours();
    public String photo;

    public VtVenue() {} // There must be a no-arg constructor

    public VtVenue(String photo) {
        this.photo = photo;
    }

    public String print() {

        String retString = "Venue\n===================================\n" + "id: " + id + "\nname: " + name + "\n";

        for (Category category : categories) {
           retString = retString + category.print()+"\n";
        }

        String photoPrint;

        retString = retString + location.print() + "\n" + stats.print() + "\n" + url + "\n" + ratingColor + "\n" + ratingSignals + "\n" + hours.print() + "\n" + photo;

        return retString+"\n===================================\n";

    }

}
