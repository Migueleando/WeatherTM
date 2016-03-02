package com.mr.weathertm.Data;

import android.content.Context;
import com.parse.ParseObject;

/**
 * Created by Jebu on 01/03/2016.
 */
public class DataSource {

    private Context mContext;

    public DataSource(Context context) {
        mContext = context;
    }

    public static void insertCity() {
        ParseObject cities = new ParseObject("Cities");
        cities.put("name", CityEntry.getCityName());
        cities.saveInBackground();
    }
}
