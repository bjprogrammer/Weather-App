package com.bobby.chefling.main;

import com.bobby.chefling.model.WeatherResponse;

public class MainContract{

    interface MainView {
        void showWait();
        void scheduleAlarm();
        void onFailure(String appErrorMessage);
        void onSuccess(WeatherResponse response);
        void setData(WeatherResponse response);

    }

    interface MainPresenter{
         void getUpdatedWeather(String clientID, String cityName);
         void getWeatherReport(String cityName);
         void unSubscribe();
    }
}
