package com.sayedmetal.afs.sayedmetaltask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ahmed on 2/19/2018.
 */

public class LocationsReader {
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

    public List<MyItem> read(InputStream inputStream) throws JSONException {
        List<MyItem> items = new ArrayList<MyItem>();
        //read the entire input stream as a string , the delimiter we use matches the entire data
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        //initialize a json array object from a string
        JSONArray array = new JSONArray(json);
        //foreach location construct a MyItem object
        for (int i = 0; i < array.length(); i++) {
            String title = null;
            String snippet = null;
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            if (!object.isNull("title")) {
                title = object.getString("title");
            }
            items.add(new MyItem(lat, lng, title, snippet));
        }
        return items;
    }
}
