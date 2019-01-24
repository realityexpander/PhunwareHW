package com.realityexpander.phunwarehw;

import android.app.Application;
import android.content.Context;

public class phunwareApp extends Application {

  private static Context context;

  public void onCreate() {
    super.onCreate();
    phunwareApp.context = getApplicationContext();
  }

  public static Context getAppContext() {
    return phunwareApp.context;
  }
}
