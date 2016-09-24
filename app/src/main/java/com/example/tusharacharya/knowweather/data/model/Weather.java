package com.example.tusharacharya.knowweather.data.model;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class Weather {
    long id;
    String main;
    String description;
    String icon;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
