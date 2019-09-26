package com.bobby.chefling.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CurrentWeatherResponse {


    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weather) {
        this.weather = weather;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @SerializedName("name")
    private String city;


    @SerializedName("main")
    private WeatherData weather;

    public List<Weather> getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(List<Weather> weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    @SerializedName("weather")
    private List<Weather> weatherDescription;


    public class Weather{
        public String getDescription() {
            return description;
        }

        public void setDescription(String weatherDescription) {
            this.description = weatherDescription;
        }

        public String getWeatherIcon() {
            return weatherIcon;
        }

        public void setWeatherIcon(String weatherIcon) {
            this.weatherIcon = weatherIcon;
        }

        private String description;

        @SerializedName("icon")
        private String weatherIcon;


    }


    public class WeatherData{
        public String getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(String currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        public String getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(String minTemperature) {
            this.minTemperature = minTemperature;
        }

        public String getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(String maxTemperature) {
            this.maxTemperature = maxTemperature;
        }

        @SerializedName("temp")
        private String currentTemperature;

        @SerializedName("temp_min")
        private String minTemperature;

        @SerializedName("temp_max")
        private String maxTemperature;
    }
}
