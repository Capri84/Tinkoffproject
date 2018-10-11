package com.example.capri.tinkoffproject;

import android.app.Application;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

  //  private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
  //      context = context.getApplicationContext();
    }

  /*  public static Context getContext() {
        return context;
    }*/
}
