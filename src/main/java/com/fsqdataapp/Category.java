package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;

public class Category {

	public transient String id;
  public String name;
  public transient String pluralName;
  public transient String shortName;
	public Icon icon = new Icon();
	public transient boolean primary;

    public Category() {} // There must be a no-arg constructor

    public String print() {

    	return name + "\n" + icon.print();

    }

}
