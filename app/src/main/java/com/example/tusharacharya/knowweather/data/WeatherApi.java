package com.example.tusharacharya.knowweather.data;


import com.example.tusharacharya.knowweather.data.model.WeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tusharacharya on 24/09/16.
 */
public interface WeatherApi {

    @GET("/data/2.5/weather")
    Observable<WeatherResponse> getWeatherForCity(@Query("APPID") String appId, @Query("q") String cityName);

    @GET("/data/2.5/weather")
    Observable<WeatherResponse> getWeatherForCityWithId(@Query("APPID") String appId, @Query("id") long cityId);
}
