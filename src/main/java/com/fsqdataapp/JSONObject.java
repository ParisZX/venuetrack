package com.fsqdataapp;

import java.util.*;

public class JSONObject {

    public Response response = new Response();

    public JSONObject() {}

    public String print() {

    	return "JSON Object\n===================================\n\n\n"+response.print();
    }

}
