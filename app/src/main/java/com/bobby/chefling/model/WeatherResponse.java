package com.bobby.chefling.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Entity(tableName = "weather")
public class WeatherResponse {

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public LinkedHashMap<String, ArrayList<String>> getForecast() {
        return forecast;
    }

    public void setForecast(LinkedHashMap<String, ArrayList<String>> forecast) {
        this.forecast = forecast;
    }

    @NonNull
    @PrimaryKey()
    private String city;

    @ColumnInfo(name = "Temp")
    private String currentTemp;

    @ColumnInfo(name = "Min")
    private String minTemp;

    @ColumnInfo(name = "Max")
    private String maxTemp;

    @ColumnInfo(name = "Description")
    private String weatherType;

    @ColumnInfo(name = "Image")
    private String weatherIcon;

   private LinkedHashMap<String, ArrayList<String>> forecast;
}
