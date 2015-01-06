package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

public class Icon {

	public String prefix, suffix;

	public Icon() {}
	
	public String print() {

    	return "<img src=" + prefix + suffix +"/>";

    }

}