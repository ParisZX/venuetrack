package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

public class Stats {

	public long checkinsCount;
	public long usersCount;
	public long tipCount; 

	public Stats() {}

	public String print() {
	
		String retString = "checkinsCount: " + checkinsCount + ", usersCount: " + usersCount + "\ntipCount: " + tipCount;
		return retString;
	
	}
}