package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

public class Category {

	public String id, name, pluralName, shortName;
	public Icon icon = new Icon();
	public boolean primary;

    public Category() {} // There must be a no-arg constructor

    public String print() {

    	return name + "\n" + icon.print();

    }

}