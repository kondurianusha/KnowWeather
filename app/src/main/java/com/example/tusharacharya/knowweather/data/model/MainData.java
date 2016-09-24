package com.example.tusharacharya.knowweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tusharacharya on 24/09/16.
 */
public class MainData {
    double temp;
    double pressure;
    double humidity;

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
