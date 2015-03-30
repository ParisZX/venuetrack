package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.googlecode.objectify.ObjectifyService.*;

public class OfyService {
    static {
        factory().register(VtVenue.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}