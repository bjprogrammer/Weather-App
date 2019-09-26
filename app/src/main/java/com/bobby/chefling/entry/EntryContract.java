package com.bobby.chefling.entry;

public interface EntryContract {

    interface EntryView {
        void directMainScreen(String cityName);
        void renderView(String cityName, Boolean isUserAction);
    }

    interface Presenter{
        void checkCityExistence(Boolean isUserAction);
        void setCity(String cityName);
        void cleanMemory();
    }
}
