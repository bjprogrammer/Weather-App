package com.bobby.chefling.entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bobby.chefling.R;
import com.bobby.chefling.databinding.ActivityEntryBinding;
import com.bobby.chefling.main.MainActivity;
import com.bobby.chefling.utils.Constants;

public class EntryActivity extends AppCompatActivity implements  EntryContract.EntryView {

    private ActivityEntryBinding binding;
    private EditText city;
    private ImageButton submit,back;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EntryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(Constants.CITY, 0);
        editor = pref.edit();

        presenter = new EntryPresenter(pref, editor,this);

        presenter.checkCityExistence(getIntent().getBooleanExtra(Constants.IS_USER_ACTION,false));
    }


    @Override
    public void directMainScreen(String cityName) {
        Intent intent =new Intent(EntryActivity.this, MainActivity.class);
        intent.putExtra(Constants.CITY,cityName);
        startActivity(intent);
        finish();
    }

    public void renderView(String cityName,Boolean isUserAction) {
          binding =  DataBindingUtil.setContentView(this,R.layout.activity_entry);
          city = binding.city;
          submit = binding.submit;
          back = binding.back;

          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

          View decorView = getWindow().getDecorView();
          int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
          decorView.setSystemUiVisibility(uiOptions);

          init(cityName);

          if(isUserAction){
              back.setVisibility(View.VISIBLE);
          }
    }

    private void init(String cityName){
        if(!cityName.isEmpty()){
            city.setText(cityName);
        }

        submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(city.getText().length()>0){
                     presenter.setCity(city.getText().toString());

                     Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                     intent.putExtra(Constants.CITY_RESET,true);
                     intent.putExtra(Constants.CITY,city.getText().toString());
                     startActivity(intent);
                     finish();
                     overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                 }
                 else
                 {
                     city.setError("Please enter city name");
                 }
             }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }

    @Override
    protected void onDestroy() {
        presenter.cleanMemory();
        presenter=null;
        super.onDestroy();
    }
}
