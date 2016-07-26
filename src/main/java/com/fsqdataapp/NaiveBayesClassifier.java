package com.fsqdataapp;

import com.googlecode.objectify.annotation.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;

@Entity
public class NaiveBayesClassifier {

  @Id public String id;

  int posCount = 0;

  int negCount = 0;

  HashMap<String,Integer> posHash = new HashMap<String,Integer>();

  HashMap<String,Integer> negHash = new HashMap<String,Integer>();

  HashSet<String> voc = new HashSet<String>();

  public NaiveBayesClassifier() {  }

  public NaiveBayesClassifier(String givenId) {
    id = givenId;
  }

  public void addExample(String klass, List<String> words) {
   if(klass.equalsIgnoreCase("pos")) {
       posCount++;
   } else {
       negCount++;
   }
   Iterator<String> iter = words.iterator();
   while(iter.hasNext()) {
       String word = iter.next();
       // get the vocabulary
       voc.add(word);
       HashMap<String,Integer> hash;
       if(klass.equalsIgnoreCase("pos")) {
           hash = posHash;
       } else {
           hash = negHash;
       }
       if(hash.containsKey(word)) {
           int count = hash.get(word).intValue();
           hash.put(word, new Integer(++count));
       } else {
           hash.put(word, new Integer(1));
       }
   }
  }

  double sumOfHashMap(HashMap<String,Integer> hash) {
       Collection<Integer> values = hash.values();
       Iterator<Integer> iter = values.iterator();
       double sum = 0.0;
       while(iter.hasNext()) {
           sum += iter.next().intValue();
       }
       return sum;
  }

  double log2(double a) {
   return Math.log(a)/Math.log(2);
  }

  /**
  *  Put your code here for deciding the class of the input file.
  *  Currently, it just randomly chooses "pos" or "negative"
  */
  public String classify(List<String> words) {
   double p_p = (double)posCount/(double)(posCount+negCount);
   double p_n = (double)negCount/(double)(posCount+negCount);
   // Positive
   double posSum = log2(p_p);
   double negSum = log2(p_n);
   Iterator<String> iter = words.iterator();
   double posDen = sumOfHashMap(posHash);
   double negDen = sumOfHashMap(negHash);
   while(iter.hasNext()) {
       String word = iter.next();
       if(posHash.containsKey(word)) {
           int count = posHash.get(word).intValue();
           double p = (count + 1.0) / (posDen + voc.size());
           posSum += log2(p);
       } else {
           double p = (1.0) / (posDen + voc.size());
           posSum += log2(p);
       }
   }
   iter = words.iterator();
   while(iter.hasNext()) {
       String word = iter.next();
       if(negHash.containsKey(word)) {
           int count = negHash.get(word).intValue();
           double p = (count + 1.0) / (negDen + voc.size());
           negSum += log2(p);
       } else {
           double p = (1.0) / (negDen + voc.size());
           negSum += log2(p);
       }
   }
   if(negSum < posSum) {
     return "pos";
   } else {
     return "neg";
   }
  }

}
