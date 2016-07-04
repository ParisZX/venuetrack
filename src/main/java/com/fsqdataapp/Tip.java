package com.fsqdataapp;

import java.util.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Tip {

	@Id public String id;
	@Index public long createdAt;
  @Index public String venueId;
	public String text;
	public String type;
	public String canonicalUrl;
	public Like likes;
	public boolean like;
	public User user;

	public Tip() {}

	public String print() {
    	String retString = "id = " + id + "\ncreatedAt = " + createdAt + "\ntext = " + text + "\ntype = " + type +
    	"\ncanonicalUrl = " + canonicalUrl + "\nlike = " + like + "\nuser details = " + user.id + " " + user.firstName +
    	" " + user.lastName + " " + user.gender + "\nlikes = " + likes.count;
		return retString;
    }

}
