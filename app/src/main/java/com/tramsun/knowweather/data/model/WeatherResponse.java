package com.tramsun.knowweather.data.model;


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

    public Coordinate getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public MainData getMain() {
        return main;
    }

    public int getCod() {
        return cod;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

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
