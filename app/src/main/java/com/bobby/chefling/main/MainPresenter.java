package com.bobby.chefling.main;

import android.os.Handler;

import com.bobby.chefling.model.WeatherDao;
import com.bobby.chefling.model.WeatherResponse;
import com.bobby.chefling.network.NetworkError;
import com.bobby.chefling.network.Service;
import com.bobby.chefling.utils.Constants;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.MainPresenter{

    private MainContract.MainView view;
    private CompositeDisposable disposable;
    private WeatherDao weatherDao;

    public MainPresenter(MainContract.MainView view, WeatherDao weatherDao) {
        this.view=view;
        this.weatherDao=weatherDao;
    }

    //Returns suggestion list whose name are similar to query passed as an argument
    public void getUpdatedWeather(String clientID, String cityName) {
        view.showWait();
        view.scheduleAlarm();

        new Service().fetchData(new Service.ImagesCallback() {
            @Override
            public void onSuccess(WeatherResponse response)  {

                if(response!=null) {
                    view.onSuccess(response);
                }

            }

            @Override
            public void onError(final NetworkError networkError) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            view.onFailure(networkError.getAppErrorMessage());
                    }
                }, 300);
            }

            @Override
            public void getDisposable(CompositeDisposable d) {
                disposable=d;
            }
        }, clientID,cityName, weatherDao);
    }

    @Override
    public void getWeatherReport(String cityName) {
         Executor executor = Executors.newFixedThreadPool(2);
         executor.execute(new Runnable() {
             @Override
             public void run() {
                 WeatherResponse response=weatherDao.getWeatherOfCity();
                 if(response!=null){
                     view.setData(response);
                 }
                 else {
                     getUpdatedWeather(Constants.API_KEY,cityName);
                 }

             }
         });
    }


    public  void unSubscribe(){
        view=null;
        if(disposable!=null){
            disposable.dispose();
        }
    }
}
