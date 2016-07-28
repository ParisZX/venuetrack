package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.io.*;
import java.util.*;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import javax.inject.Named;

@Api(name = "venuetrackEndpoints",
    version = "v1")
public class Endpoints {

  public Venue getVenue(@Named("id") String id) throws NotFoundException {
    try {
      return ofy().load().type(Venue.class).id(id).now();
    } catch (IndexOutOfBoundsException e) {
      throw new NotFoundException("Venue not found with an id: " + id);
    }
  }

  public List<Venue> listVenues() {
    return ofy().load().type(Venue.class).list();
  }

}
