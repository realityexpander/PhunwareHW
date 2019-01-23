package com.realityexpander.phunwarehw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    class StarEvent implements Serializable {
        /*
        INSTANCE FIELDS
         */

        @SerializedName("title")
        private String title;
        @SerializedName("image")
        private String image;
        @SerializedName("timestamp")
        private String timeStamp;
        @SerializedName("date")
        private String date;
        @SerializedName("locationline1")
        private String locationLine1;
        @SerializedName("locationline2")
        private String locationLine2;
        @SerializedName("description")
        private String description;
        @SerializedName("phone")
        private String phone;

        public StarEvent(String title,
                     String image,
                     String timeStamp,
                     String date,
                     String locationLine1,
                     String locationLine2,
                     String description,
                     String phone) {
          this.title = title;
          this.image = image;
          this.timeStamp = timeStamp;
          this.date = date;
          this.locationLine1 = locationLine1;
          this.locationLine2 = locationLine2;
          this.description = description;
          this.phone = phone;
        }

        /*
         *GETTERS AND SETTERS
         */
          public String getTitle() {
            return title;
          }
          public void setTitle(String name) {
            this.title = name;
          }

          public String getThumbnailUrl() {
            return image;
          }
          public void setThumbnailUrl(String image) {
            this.image = image;
          }

          public String getTimeStamp() { return timeStamp;	}
          public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp;	}

          public String getDate() {	return date;	}
          public void setDate(String date) { this.date = date;	}

          public String getLocationLine1() { return locationLine1;	}
          public void setLocationLine1(String locationLine1) { this.locationLine1 = locationLine1;	}

          public String getLocationLine2() { return locationLine2;	}
          public void setLocationLine2(String locationLine2) { this.locationLine2 = locationLine2;	}

          public String getDescription() { return description;	}
          public void setDescription(String description) { this.description = description;	}

          public String getPhone() { return phone;  }
          public void setPhone(String phone) { this.phone = phone;  }

        /*
        TOSTRING
         */
        @Override
        public String toString() {
            return title;
        }
    }

    interface MyAPIService {
        @GET("/phunware-services/dev-interview-homework/master/feed.json")
        Call<List<StarEvent>> getSpacecrafts();
    }

    static class RetrofitClientInstance {

        private static Retrofit retrofit;
        private static final String BASE_URL = "https://raw.githubusercontent.com/";

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

    class ListViewAdapter extends BaseAdapter{

        private List<StarEvent> starEvent;
        private Context context;

        public ListViewAdapter(Context context,List<StarEvent> starEvent){
            this.context = context;
            this.starEvent = starEvent;
        }

        @Override
        public int getCount() {
            return starEvent.size();
        }

        @Override
        public Object getItem(int pos) {
            return starEvent.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=LayoutInflater.from(context).inflate(R.layout.model,viewGroup,false);
            }

            TextView titleTextView = view.findViewById(R.id.title);
            TextView location1TextView = view.findViewById(R.id.location1);
            TextView descriptionTextView = view.findViewById(R.id.description);
            ImageView thumbnailImageView = view.findViewById(R.id.thumbnail);

            final StarEvent thisStarEvent = starEvent.get(position);

            titleTextView.setText(thisStarEvent.getTitle());
            location1TextView.setText(thisStarEvent.getLocationLine1());
            String s = thisStarEvent.getDescription();
            s = s.substring(0, Math.min(s.length(), 200)) + (s.length()>200 ? "..." : "");
            descriptionTextView.setText(s);

            if(thisStarEvent.getThumbnailUrl() != null && thisStarEvent.getThumbnailUrl().length()>0)
            {
                // Picasso.get().load(thisStarEvent.getImageURL()).placeholder(R.drawable.placeholder).into(thumbnailImageView);
                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.GRAY)
                        .borderWidthDp(3)
                        .cornerRadiusDp(100)
                        .oval(false)
                        .build();

                Picasso.get()
                        .load(thisStarEvent.getThumbnailUrl())
                        .fit()
                        .transform(transformation)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(thumbnailImageView);

            } else {
                Toast.makeText(context, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.placeholder).into(thumbnailImageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //Toast.makeText(context, thisStarEvent.getTitle(), Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(getApplicationContext(),ScrollingActivity.class);
                  intent.putExtra("starEvent", thisStarEvent);
                  startActivity(intent);
                }
            });

            return view;
        }
    }

    private ListViewAdapter adapter;
    private ListView mListView;
    ProgressBar myProgressBar;

    private void populateListView(List<StarEvent> spacecraftList) {
        mListView = findViewById(R.id.mListView);
        adapter = new ListViewAdapter(this,spacecraftList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar myProgressBar= findViewById(R.id.myProgressBar);
        myProgressBar.setIndeterminate(true);
        myProgressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

        Call<List<StarEvent>> call = myAPIService.getSpacecrafts();
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
}
