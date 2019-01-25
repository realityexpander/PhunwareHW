package com.realityexpander.phunwarehw;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Athanas on 1/23/19.
 */

class ListViewAdapter extends BaseAdapter {

  private List<StarEvent> starEvents;
  private Context context;

  public ListViewAdapter(Context context, List<StarEvent> starEvents) {
//    super();
    this.context = context;
    this.starEvents = starEvents;
  }

  @Override
  public int getCount() {
    return starEvents.size();
  }

  @Override
  public Object getItem(int pos) {
    return starEvents.get(pos);
  }

  @Override
  public long getItemId(int pos) {
    return pos;
  }

  @Override
  public View getView(int position, View view, ViewGroup viewGroup) {
    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.model, viewGroup, false);
    }

    TextView titleTextView = view.findViewById(R.id.title);
    TextView location1TextView = view.findViewById(R.id.location1);
    TextView descriptionTextView = view.findViewById(R.id.description);
    TextView shareTextView = view.findViewById(R.id.model_share);
    final ImageView thumbnailImageView = view.findViewById(R.id.thumbnail);

    final View statusBar = view.findViewById(android.R.id.statusBarBackground);
    final View navigationBar = view.findViewById(android.R.id.navigationBarBackground);

    final StarEvent thisStarEvent = starEvents.get(position);

    titleTextView.setText(thisStarEvent.getTitle());
    location1TextView.setText(thisStarEvent.getLocationLine1());
    descriptionTextView.setText(thisStarEvent.getDescription());

    if (thisStarEvent.getThumbnailUrl() != null && thisStarEvent.getThumbnailUrl().length() > 0) {
      // Set the circular transformation
      final Transformation transformation = new RoundedTransformationBuilder()
              .borderColor(Color.GRAY)
              .borderWidthDp(3)
              .cornerRadiusDp(100)
              .oval(false)
              .build();

      // Load Image Caches
      Picasso.get()
              .load(thisStarEvent.getThumbnailUrl())
              .fit()
              .transform(transformation)
              .networkPolicy(NetworkPolicy.OFFLINE)
              .into(thumbnailImageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                  // Try again online if cache failed
                  Picasso.get()
                          .load(thisStarEvent.getThumbnailUrl())
                          .placeholder(R.drawable.placeholder_nomoon)
                          .error(R.drawable.placeholder_nomoon)
                          .into(thumbnailImageView);
                }
              });

    } else {
      // Toast.makeText(context, "Empty Image URL", Toast.LENGTH_LONG).show();
      Picasso.get().load(R.drawable.placeholder_nomoon).into(thumbnailImageView);
    }

    shareTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        StarEvent.shareIntent(context, thisStarEvent);
      }
    });

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context.getApplicationContext(), ScrollingActivity.class);
        intent.putExtra("starEvents", thisStarEvent);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, (View)thumbnailImageView, "poster")
                .toBundle();

        // Check if we can attempt to reduce flicker
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
          List<Pair<View, String>> pairs = new ArrayList<>();
          if (statusBar != null) {
            pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
          }
          if (navigationBar != null) {
            pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
          }
          pairs.add(Pair.create((View)thumbnailImageView, "poster"));
          options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                  pairs.toArray(new Pair[pairs.size()])).toBundle();
        }

        context.startActivity(intent, options);
      }
    });

    return view;
  }


}





