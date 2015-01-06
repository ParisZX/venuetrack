package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

@Entity
public class Venue {
    
    public String id; // Can be Long -if null then autogenerates-, long, or String
    public String name;
    public List<Category> categories = new ArrayList<Category>();
    public Location location = new Location();
    public Stats stats = new Stats();
    public String url;
    public float rating;
    public String ratingColor;
    public long ratingSignals;
    public Hours hours = new Hours();
    public Photos photos = new Photos();


    public Venue() {} // There must be a no-arg constructor

    public Venue(String id, String name) {
    	this.id = id;
    	this.name = name;
    }

    public String print() {

        String retString = "Venue\n===================================\n" + "id: " + id + "\nname: " + name + "\n";

        for (Category category : categories) {
           retString = retString + category.print()+"\n";
        }

        retString = retString + location.print() + "\n" + stats.print() + "\n" + url + "\n" + ratingColor + "\n" + ratingSignals + "\n" + hours.print() + "\n" + photos.print();

        return retString+"\n===================================\n";

    }

}
