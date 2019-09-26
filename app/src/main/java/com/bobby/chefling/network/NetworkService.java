package com.bobby.chefling.network;

import com.bobby.chefling.model.CurrentWeatherResponse;
import com.bobby.chefling.model.ForecastWeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

//ALL API calls endpoints
public interface NetworkService {

    @GET("data/2.5/weather")
    Observable<CurrentWeatherResponse> getCurrentWeather(@Query("appid") String appID, @Query("q") String cityName) ;

    @GET("data/2.5/forecast")
    Observable<ForecastWeatherResponse> getForecastWeather(@Query("appid") String appID, @Query("q") String cityName) ;
}

