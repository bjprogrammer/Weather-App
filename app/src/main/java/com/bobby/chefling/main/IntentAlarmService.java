package com.bobby.chefling.main;

import android.app.AlarmManager;
import android.app.IntentService;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;


import com.bobby.chefling.model.DatabaseCreator;
import com.bobby.chefling.model.WeatherDao;
import com.bobby.chefling.model.WeatherResponse;
import com.bobby.chefling.network.NetworkError;
import com.bobby.chefling.network.Service;
import com.bobby.chefling.utils.Constants;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public class IntentAlarmService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentAlarmService(String name) {
        super(name);
    }


    public IntentAlarmService() {
        super("Processing");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WeatherDao weatherDao = DatabaseCreator.getAppDatabase(this).getWeatherDAO();
        new Service().fetchData(new Service.ImagesCallback() {
            @Override
            public void onSuccess(WeatherResponse response)  { }

            @Override
            public void onError(final NetworkError networkError) { }

            @Override
            public void getDisposable(CompositeDisposable d) { }
        }, Constants.API_KEY,intent.getStringExtra(Constants.CITY), weatherDao);

        scheduleAlarm(intent.getStringExtra(Constants.CITY));
    }


    public void scheduleAlarm(String cityName) {
        Context ctxt=this;
        AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);

        Intent i=new Intent(ctxt, IntentAlarmService.class);
        i.putExtra(Constants.CITY,cityName);
        PendingIntent pi=PendingIntent.getService(ctxt, 0, i, 0);

        Intent i2=new Intent(ctxt, MainActivity.class);
        PendingIntent pi2=PendingIntent.getActivity(ctxt, 0, i2, PendingIntent.FLAG_CANCEL_CURRENT);

        try {
            mgr.cancel(PendingIntent.getService(ctxt, 0, i, 0));
        }catch (Exception e){}

        AlarmManager.AlarmClockInfo ac=
                new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+300000, pi2);

        mgr.setAlarmClock(ac, pi);
    }
}
