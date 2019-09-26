package com.bobby.chefling.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ForecastWeatherResponse {


    public List<ForecastList> getWeather() {
        return weather;
    }

    public void setWeather(List<ForecastList> weather) {
        this.weather = weather;
    }

    @SerializedName("list")
    private List<ForecastList> weather;

    @SerializedName("city")
    private City cityData;

    public class City{

        @SerializedName("name")
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }


    public class  ForecastList {

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public WeatherData getWeather() {
            return weather;
        }

        public void setWeather(WeatherData weather) {
            this.weather = weather;
        }

        @SerializedName("dt")
        private Long timestamp;

        @SerializedName("dt_txt")
        private String date;

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


        public class Weather {
            public String getWeatherDescription() {
                return weatherDescription;
            }

            public void setWeatherDescription(String weatherDescription) {
                this.weatherDescription = weatherDescription;
            }

            public String getWeatherIcon() {
                return weatherIcon;
            }

            public void setWeatherIcon(String weatherIcon) {
                this.weatherIcon = weatherIcon;
            }

            @SerializedName("description")
            private String weatherDescription;

            @SerializedName("icon")
            private String weatherIcon;


        }

    }
}
