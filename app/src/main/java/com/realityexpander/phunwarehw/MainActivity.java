package com.realityexpander.phunwarehw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {


  ProgressBar myProgressBar;
  private ListViewAdapter adapter;
//  private ListView mListView;
  private GridView mListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ProgressBar myProgressBar = findViewById(R.id.myProgressBar);
    myProgressBar.setIndeterminate(true);
    myProgressBar.setVisibility(View.VISIBLE);

    /*Create handle for the RetrofitInstance interface*/
    MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

    Call<List<StarEvent>> call = myAPIService.getStarEvents();
    call.enqueue(new Callback<List<StarEvent>>() {

      @Override
      public void onResponse(Call<List<StarEvent>> call, Response<List<StarEvent>> response) {
        myProgressBar.setVisibility(View.GONE);
        populateListView(response.body());
      }

      @Override
      public void onFailure(Call<List<StarEvent>> call, Throwable throwable) {
        myProgressBar.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
      }
    });

  }

  private void populateListView(List<StarEvent> starEventList) {
    mListView = findViewById(R.id.mListView);
    adapter = new ListViewAdapter(this, starEventList);
    mListView.setAdapter(adapter);
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
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  interface MyAPIService {
    @GET("/phunware-services/dev-interview-homework/master/feed.json")
    Call<List<StarEvent>> getStarEvents();
  }

  static class RetrofitClientInstance {

    private static final String BASE_URL = "https://raw.githubusercontent.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
      if (retrofit == null) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      }
      return retrofit;
    }
  }

}
