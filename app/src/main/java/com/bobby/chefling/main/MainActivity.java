package com.bobby.chefling.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bobby.chefling.R;
import com.bobby.chefling.databinding.ActivityMainBinding;
import com.bobby.chefling.entry.EntryActivity;
import com.bobby.chefling.model.DatabaseCreator;
import com.bobby.chefling.model.WeatherDao;
import com.bobby.chefling.model.WeatherResponse;
import com.bobby.chefling.utils.ConnectivityReceiver;
import com.bobby.chefling.utils.Constants;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements MainContract.MainView, ConnectivityReceiver.ConnectivityReceiverListener {
    private MainPresenter presenter;
    private boolean flag = false;
    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private IntentFilter intentFilter;
    private ConnectivityReceiver receiver;
    private String cityName;
    private ActivityMainBinding binding;
    private TextView city, currTemp,tempDesc,minTemp,maxTemp,day1,day2,day3,day4,day5,range1,range2,range3,range4,range5;
    private ImageView current,forecast1,forecast2,forecast3,forecast4,forecast5;
    private ImageButton next;
    private WeatherDao weatherDao;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderView();
        init();

        weatherDao = DatabaseCreator.getAppDatabase(this).getWeatherDAO();
        presenter = new MainPresenter(this,weatherDao);

        if(getIntent().getBooleanExtra(Constants.CITY_RESET,false) ) {
            presenter.getUpdatedWeather(Constants.API_KEY, cityName);
        }
        else{
            presenter.getWeatherReport(cityName);
        }
    }


    private void renderView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        city = binding.city;
        currTemp = binding.currentWeather;
        minTemp = binding.minWeather;
        maxTemp = binding.maxWeather;
        tempDesc = binding.weatherDescription;

        day1 = binding.day1;
        day2 = binding.day2;
        day3 = binding.day3;
        day4 = binding.day4;
        day5 = binding.day5;

        range1= binding.minMax1;
        range2 = binding.minMax2;
        range3 = binding.minMax3;
        range4 = binding.minMax4;
        range5 = binding.minMax5;

        forecast1 = binding.image1;
        forecast2 = binding.image2;
        forecast3 = binding.image3;
        forecast4 = binding.image4;
        forecast5 = binding.image5;

        next = binding.next;
        current = binding.currentImage;
        pullToRefresh = binding.pullToRefresh;
    }

    private void init() {
        //Broadcast receiver for checking internet connection
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new ConnectivityReceiver();

        //Configuring customized Toast messages
        Toasty.Config.getInstance()
                .setErrorColor( getResources().getColor(R.color.colorPrimaryDark) )
                .setSuccessColor(getResources().getColor(R.color.colorPrimaryDark) )
                .setTextColor(Color.WHITE)
                .tintIcon(true)
                .setTextSize(18)
                .apply();

        cityName = getIntent().getStringExtra(Constants.CITY);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                intent.putExtra(Constants.IS_USER_ACTION,true);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getUpdatedWeather(Constants.API_KEY, cityName);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void showWait() {
        pullToRefresh.setRefreshing(true);
//        progressBar.setVisibility(View.VISIBLE);
    }

    public void removeWait() {
        pullToRefresh.setRefreshing(false);
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {
        flag = false;
        removeWait();
        Toasty.error(getApplicationContext(), appErrorMessage, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void onSuccess(WeatherResponse response) {
        //Displaying data retrieved from server
        flag = true;
        removeWait();

        setData(response);
    }

    private void loadImage(String url,ImageView view ){
        Glide.with(this)
                .load(url)
                .thumbnail(0.1f)
                .into(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, intentFilter);
        ConnectivityReceiver.connectivityReceiverListener = this;
    }

    //Checking internet flag using broadcast receiver
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(flag!=isConnected)
        {
            if(isConnected){
                Toasty.success(this, "Connected to internet", Toast.LENGTH_SHORT, true).show();
            }
            else
            {
                Toasty.error(getApplicationContext(), "Not connected to internet", Toast.LENGTH_LONG, true).show();
            }
        }
        flag= (isConnected);
    }


    //Handling app closure
    private void exitApp() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitApp();
    }

    @Override
    protected void onDestroy() {
        presenter.unSubscribe();
        presenter = null;
        DatabaseCreator.destroyInstance();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) { }
        }
    }


    public void setData(WeatherResponse response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                next.setVisibility(View.VISIBLE);
                city.setText(response.getCity());
                currTemp.setText(response.getCurrentTemp()+"º");
                minTemp.setText(response.getMinTemp()+"º");
                maxTemp.setText(response.getMaxTemp()+"º");
                tempDesc.setText(response.getWeatherType());
                loadImage(response.getWeatherIcon(),current);

                LinkedHashMap<String, ArrayList<String>> forecastData= response.getForecast();
                Iterator hmIterator = forecastData.entrySet().iterator();

                int count =0;

                while (hmIterator.hasNext()) {
                    count++;
                    Map.Entry mapElement = (Map.Entry)hmIterator.next();
                    ArrayList<String> forecast= ((ArrayList<String>)mapElement.getValue());

                    switch (count) {
                        case 1:
                            day1.setText((String) mapElement.getKey());
                            range1.setText(forecast.get(0)  + "/"+ forecast.get(1));
                            loadImage(forecast.get(2), forecast1);
                            break;

                        case 2:
                            day2.setText((String) mapElement.getKey());
                            range2.setText(forecast.get(0)+ "/"+ forecast.get(1));
                            loadImage(forecast.get(2), forecast2);
                            break;

                        case 3:
                            day3.setText((String) mapElement.getKey());
                            range3.setText(forecast.get(0)+ "/"+ forecast.get(1));
                            loadImage(forecast.get(2), forecast3);
                            break;

                        case 4:
                            day4.setText((String) mapElement.getKey());
                            range4.setText(forecast.get(0)+ "/"+ forecast.get(1));
                            loadImage(forecast.get(2), forecast4);
                            break;

                        case 5:
                            day5.setText((String) mapElement.getKey());
                            range5.setText(forecast.get(0) + "/"+ forecast.get(1));
                            loadImage(forecast.get(2), forecast5);
                            break;

                        default:
                            break;
                    }

                }
            }
        });

    }


    public void scheduleAlarm() {
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
