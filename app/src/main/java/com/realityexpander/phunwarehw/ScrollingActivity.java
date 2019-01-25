package com.realityexpander.phunwarehw;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    final ImageView poster = (ImageView) findViewById(R.id.poster);

    TextView dateTextView = findViewById(R.id.date);
    TextView titleTextView = findViewById(R.id.title);
    TextView descriptionTextView = findViewById(R.id.description);
    ImageView phoneImageView = findViewById(R.id.phone1);
    ImageView shareImageView = findViewById(R.id.share);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle("");

    Intent i = getIntent();
    final StarEvent thisStarEvent = (StarEvent) i.getSerializableExtra("starEvents");

    // Load Image Caches
    Picasso.get()
            .load(thisStarEvent.getThumbnailUrl())
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.placeholder_nomoon)
            .into(poster, new Callback() {
              @Override
              public void onSuccess() {
                scheduleStartPostponedTransition(poster);
              }

              @Override
              public void onError(Exception e) {
                // Try again online if cache failed
                Picasso.get()
                        .load(thisStarEvent.getThumbnailUrl())
                        .placeholder(R.drawable.placeholder_nomoon)
                        .error(R.drawable.placeholder_nomoon)
                        .into(poster);
                scheduleStartPostponedTransition(poster);
              }
            });

    // Only suspend for transition only if given valid image link
    if (thisStarEvent.getThumbnailUrl() != null)
      supportPostponeEnterTransition();

    // Populate the fields
    setDateTextView(dateTextView, thisStarEvent);
    titleTextView.setText(thisStarEvent.getTitle());
    descriptionTextView.setText(thisStarEvent.getDescription());

    // Call Phone Intent
    phoneImageView.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (thisStarEvent.getPhone() != null) {
          Intent callIntent = new Intent(Intent.ACTION_CALL);
          callIntent.setData(Uri.parse("tel:" + thisStarEvent.getPhone()));
          startActivity(callIntent);
        } else {
          Toast.makeText(getApplicationContext(), "No Phone Number", Toast.LENGTH_LONG).show();
        }
      }
    });

    // Share Intent
    shareImageView.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        StarEvent.shareIntent(getApplicationContext(), thisStarEvent);
      }
    });

  }

  private void setDateTextView(TextView dateTextView, StarEvent thisStarEvent) {
    // Format date to be like: Sep 27, 2015 at 2:02am
    try {
      Date d = parse(thisStarEvent.getDate());
      SimpleDateFormat spf = new SimpleDateFormat("MMM d, yyyy 'at' h:MM", new Locale("en", "US"));
      SimpleDateFormat spfa = new SimpleDateFormat("a", new Locale("en", "US"));
      String s = spf.format(d) + spfa.format(d).toLowerCase();
      dateTextView.setText(s);
    } catch (ParseException e) {
      e.printStackTrace();
      dateTextView.setText(thisStarEvent.getDate());
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        // Reverse the animation
        supportFinishAfterTransition();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }


  //   Schedules the shared element transition to be started immediately
  //   after the shared element has been measured and laid out within the
  //   activity's view hierarchy. Some common places where it might make
  //   sense to call this method.
  private void scheduleStartPostponedTransition(final View sharedElement) {
    sharedElement.getViewTreeObserver().addOnPreDrawListener(
        new ViewTreeObserver.OnPreDrawListener() {
          @Override
          public boolean onPreDraw() {
            sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              startPostponedEnterTransition();
            }
            supportStartPostponedEnterTransition();
            return true;
          }
        });
  }

  // Parse date String:
  //      YYYY = four-digit year
  //      MM   = two-digit month (01=January, etc.)
  //      DD   = two-digit day of month (01 through 31)
  //      hh   = two digits of hour (00 through 23) (am/pm NOT allowed)
  //      mm   = two digits of minute (00 through 59)
  //      ss   = two digits of second (00 through 59)
  //      s    = one or more digits representing a decimal fraction of a second
  //      TZD  = time zone designator (Z or +hh:mm or -hh:mm)
  public static Date parse(String input) throws java.text.ParseException {

    //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
    //things a bit.  Before we go on we have to repair this.
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssz", Locale.US);

    //this is zero time so we need to add that TZ indicator for
    if (input.endsWith("Z")) {
      input = input.substring(0, input.length() - 1) + "GMT-00:00";
    } else {
      int inset = 6;
      String s0 = input.substring(0, input.length() - inset);
      String s1 = input.substring(input.length() - inset, input.length());
      input = s0 + "GMT" + s1;
    }
    return df.parse(input);
  }

}
