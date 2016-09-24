package com.example.tusharacharya.knowweather.data.model;


import java.util.List;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class WeatherResponse {
    Coordinate coord;
    List<Weather> weather;
    MainData main;

    int cod;
    String name;
    long id;

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", main=" + main +
                ", cod=" + cod +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
