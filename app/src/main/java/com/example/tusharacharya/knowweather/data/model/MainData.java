package com.example.tusharacharya.knowweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class MainData {
    double temp;
    double pressure;
    double humidity;

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    @SerializedName("temp_min")
    double tempMin;

    @SerializedName("temp_max")
    double tempMax;

    @Override
    public String toString() {
        return "MainData{" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                '}';
    }
}
