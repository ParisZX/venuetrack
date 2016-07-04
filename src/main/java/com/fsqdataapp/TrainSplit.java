package com.fsqdataapp;

import com.googlecode.objectify.*;

import static com.fsqdataapp.OfyService.ofy;

import java.util.*;
import java.util.regex.*;
import java.io.*;

// The TrainSplit class is just a data structure
// that keeps the train (90%) and test (10%) datasets
// for each of the folds

public class TrainSplit {
  // training files for this split
  List<File> train = new ArrayList<File>();
  // test files for this split
  List<File> test = new ArrayList<File>();
}
