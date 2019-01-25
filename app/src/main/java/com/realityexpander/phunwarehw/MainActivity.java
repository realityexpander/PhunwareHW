package com.realityexpander.phunwarehw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;


import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ProgressBar mProgressBar = findViewById(R.id.mProgressBar);
    mProgressBar.setIndeterminate(true);
    mProgressBar.setVisibility(View.VISIBLE);

    // Create the RetrofitInstance interface
    phunAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(phunAPIService.class);
    Call<List<StarEvent>> call = myAPIService.getStarEvents();
    call.enqueue(new Callback<List<StarEvent>>() {
      @Override
      public void onResponse(Call<List<StarEvent>> call, Response<List<StarEvent>> response) {
        mProgressBar.setVisibility(View.GONE);
        populateListView(response.body());
      }

      @Override
      public void onFailure(Call<List<StarEvent>> call, Throwable throwable) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
      }
    });



  }

  private void populateListView(List<StarEvent> starEventList) {
    GridView listView = findViewById(R.id.mListView);
    ListViewAdapter adapter = new ListViewAdapter(this, starEventList);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
                              int position, long id) {
        Toast.makeText(getApplicationContext(), "" + position,
                Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.transition.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  interface phunAPIService {
    @GET("/phunware-services/dev-interview-homework/master/feed.json")
    Call<List<StarEvent>> getStarEvents();
  }

  static class RetrofitClientInstance {

    private static final String BASE_URL = "https://raw.githubusercontent.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
      if (retrofit == null) {

        //setup cache
        File httpCacheDirectory = new File( phunwareApp.getAppContext().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      }
      return retrofit;
    }
  }

  private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
      okhttp3.Response originalResponse = chain.proceed(chain.request());
      if (isOnline()) {
        int maxAge = 60; // read from cache for 1 minute
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
      } else {
        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .build();
      }
    }
  };

  public static boolean isOnline() {
    ConnectivityManager cm =
            (ConnectivityManager) phunwareApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

}

