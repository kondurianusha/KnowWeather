package com.example.tusharacharya.knowweather.data.model;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class Weather {
    long id;
    String main;
    String description;
    String icon;

    public long getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

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
