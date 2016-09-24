package com.example.tusharacharya.knowweather.data.model;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class FirebaseWeather {

    long locationId;
    String name;
    double lon;
    double lat;
    double temp;
    double pressure;

    public FirebaseWeather() {
    }

    public FirebaseWeather(long locationId, String name, double lon, double lat, double temp, double pressure) {
        this.locationId = locationId;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.temp = temp;
        this.pressure = pressure;
    }

    public FirebaseWeather(WeatherResponse wr) {
        this(wr.id,wr.name,wr.coord.lon, wr.coord.lat, wr.main.temp, wr.main.pressure);
    }

    public long getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    public String toString() {
        return "FirebaseWeather{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", temp=" + temp +
                ", pressure=" + pressure +
                '}';
    }
}
