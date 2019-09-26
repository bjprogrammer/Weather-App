package com.bobby.chefling.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Converter {
    @TypeConverter
    public static LinkedHashMap<String, ArrayList<String>> fromString(String value) {
        Type listType = new TypeToken<LinkedHashMap<String,ArrayList<String>>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromHashMap(LinkedHashMap<String, ArrayList<String>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
