package com.mr.weathertm.Data;

import java.io.Serializable;

/**
 * Created by Jebu on 01/03/2016.
 */
public class CityEntry implements Serializable {
    public static String TABLE_NAME = "Cities";
    public static String CITY_NAME = "name";

    @Override
    public String toString() {
        return "CityEntry{" +
                "cities='" + TABLE_NAME + '\'' +
                ", objectId='" + CITY_NAME + '}';
    }

    public static String getCityName(){
        return CITY_NAME;
    }

    public static void setCityName(String city){
        CITY_NAME = city;
    }
}
