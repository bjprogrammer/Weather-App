package com.bobby.chefling.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WeatherDao {
    @Insert
    public void insert(WeatherResponse weatherResponse);

    @Query("DELETE FROM weather")
    public void delete();

//    @Query("SELECT * FROM weather")
//    public WeatherResponse getContacts();

    @Query("SELECT * FROM weather")
    public WeatherResponse getWeatherOfCity();
}

