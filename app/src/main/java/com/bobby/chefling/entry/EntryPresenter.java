package com.bobby.chefling.entry;

import android.content.SharedPreferences;

import com.bobby.chefling.utils.Constants;


public class EntryPresenter implements  EntryContract.Presenter{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EntryContract.EntryView view;

    EntryPresenter(SharedPreferences pref, SharedPreferences.Editor editor, EntryContract.EntryView view) {
        this.editor = editor;
        this.pref = pref;
        this.view = view;
    }


    @Override
    public void checkCityExistence(Boolean isUserAction) {

        String cityName = pref.getString(Constants.CITY,"");
        if(cityName.length()>0){
            if(!isUserAction){
                view.directMainScreen(cityName);
            }
            else {
                view.renderView(cityName,isUserAction);
            }
        }
        else
        {
            view.renderView(cityName,isUserAction);
        }
    };

    //Setting first time launch  boolean value in shared preference
    @Override
    public void setCity(String cityName) {
        editor.putString(Constants.CITY,cityName);
        editor.apply();
    }

    @Override
    public void cleanMemory(){
         pref=null;
         editor=null;
         view=null;
    }
}

