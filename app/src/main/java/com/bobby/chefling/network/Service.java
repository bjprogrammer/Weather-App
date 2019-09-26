package com.bobby.chefling.network;


import com.bobby.chefling.model.CurrentWeatherResponse;
import com.bobby.chefling.model.ForecastWeatherResponse;
import com.bobby.chefling.model.WeatherDao;
import com.bobby.chefling.model.WeatherResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;


//Networking using RxJava
public class Service {
    public  static CompositeDisposable disposable;
    private NetworkService networkService;
    public Service(){
        networkService=NetworkAPI.getClient().create(NetworkService.class);
        if(disposable==null){
            disposable=new CompositeDisposable();
        }
    }

    public void fetchData(final ImagesCallback callback, String appID, String cityName, WeatherDao weatherDao) {
        Observable currentObservable = getCurrentWeather(appID,cityName);
        Observable forecastObservable = getForecastWeather(appID, cityName+",in");

         Observable.zip(currentObservable, forecastObservable, new BiFunction<CurrentWeatherResponse, ForecastWeatherResponse,WeatherResponse>() {

            @Override
            public WeatherResponse apply(CurrentWeatherResponse currentWeatherResponse, ForecastWeatherResponse forecastWeatherResponse) throws Exception {

                String city=currentWeatherResponse.getCity();
                String currenTemp= tempConversion(currentWeatherResponse.getWeather().getCurrentTemperature());
                String minTemp= tempConversion(currentWeatherResponse.getWeather().getMinTemperature());
                String maxTemp= tempConversion(currentWeatherResponse.getWeather().getMaxTemperature());

                CurrentWeatherResponse.Weather weather = currentWeatherResponse.getWeatherDescription().get(0);
                String weatherType=weather.getDescription();
                String weatherIcon="https://openweathermap.org/img/w/"+weather.getWeatherIcon() +".png";

                List<ForecastWeatherResponse.ForecastList> arrayList=forecastWeatherResponse.getWeather();
                GregorianCalendar currentTimestamp =new GregorianCalendar();
                currentTimestamp.setTimeInMillis(System.currentTimeMillis());
                GregorianCalendar arrayTimestamp = new GregorianCalendar();


                LinkedHashMap<String,ArrayList<String>> forecast = new LinkedHashMap<>();
                for(int i=0; i<arrayList.size();i++){
                        arrayTimestamp.setTimeInMillis(Long.parseLong(arrayList.get(i).getTimestamp().toString()+"000"));

                        if(arrayTimestamp.get(Calendar.DAY_OF_YEAR) == currentTimestamp.get(Calendar.DAY_OF_YEAR)){
                            continue;
                        }
                        else
                        {
                            if(forecast.get(getDay(arrayTimestamp.get(Calendar.DAY_OF_WEEK)))==null) {
                                ArrayList<String> weatherData=new ArrayList<>();
                                weatherData.add(0,tempConversion(arrayList.get(i).getWeather().getMinTemperature()) );
                                weatherData.add(1,tempConversion(arrayList.get(i).getWeather().getMaxTemperature()) );
                                weatherData.add(2,"https://openweathermap.org/img/w/"+arrayList.get(i).getWeatherDescription().get(0).getWeatherIcon()+".png");
                                forecast.put(getDay(arrayTimestamp.get(Calendar.DAY_OF_WEEK)), weatherData);
                            }
                            else
                            {
                                continue;
                            }

                        }
                }


                WeatherResponse response=new WeatherResponse();
                response.setCity(city);
                response.setMaxTemp(maxTemp);
                response.setMinTemp(minTemp);
                response.setWeatherIcon(weatherIcon);
                response.setWeatherType(weatherType);
                response.setForecast(forecast);
                response.setCurrentTemp(currenTemp);

                Executor executor = Executors.newFixedThreadPool(2);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        weatherDao.delete();
                        weatherDao.insert(response);
                    }
                });

                return  response;
            }
        }) .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Observer<WeatherResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
                callback.getDisposable(disposable);
            }

            @Override
            public void onNext(WeatherResponse results) {
                callback.onSuccess(results);
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(new NetworkError(e));
            }

            @Override
            public void onComplete() { }
        });
    }


    private Observable getCurrentWeather( String appID, String cityName){
        return NetworkAPI.getClient().create(NetworkService.class).getCurrentWeather(appID,cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable getForecastWeather( String appID, String cityName){
        return NetworkAPI.getClient().create(NetworkService.class).getForecastWeather(appID,cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface ImagesCallback{
        void onSuccess(WeatherResponse response);
        void onError(NetworkError networkError);
        void getDisposable(CompositeDisposable disposable);
    }

    private String getDay(int day){
        switch (day){
            case Calendar.MONDAY:
                return "MON";

            case Calendar.TUESDAY:
                return "TUE";

            case Calendar.WEDNESDAY:
                return "WED";

            case Calendar.THURSDAY:
                return "THU";

            case Calendar.FRIDAY:
                return "FRI";

            case Calendar.SATURDAY:
                return "SAT";

            case Calendar.SUNDAY:
                return "SUN";


            default:
                return null;
        }
    }


    private String tempConversion(String temperature){
        double tempInCel=((Double.parseDouble(temperature))-273.15);
        return  String.valueOf(Math.round(tempInCel));
    }
}

