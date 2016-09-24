package com.example.tusharacharya.knowweather.data.model;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class Coordinate {
    double lon;
    double lat;

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
