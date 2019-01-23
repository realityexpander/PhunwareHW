package com.realityexpander.phunwarehw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrollingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    final ImageView poster = (ImageView) findViewById(R.id.poster);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setTitle("");

    getIntent().getSerializableExtra("starEvent");
    Intent i = getIntent();
    final MainActivity.StarEvent thisStarEvent = (MainActivity.StarEvent) i.getSerializableExtra("starEvent");

    Picasso.get()
            .load(thisStarEvent.getThumbnailUrl())
            .placeholder(R.drawable.placeholder_nomoon)
            .into(poster);

    TextView dateTextView = findViewById(R.id.date);
    TextView titleTextView = findViewById(R.id.title);
    TextView descriptionTextView = findViewById(R.id.description);

    dateTextView.setText(thisStarEvent.getDate());
    titleTextView.setText(thisStarEvent.getTitle());
    descriptionTextView.setText(thisStarEvent.getDescription());



    ImageView phoneImageView = findViewById(R.id.phone1);
    phoneImageView.setOnClickListener( new View.OnClickListener() {
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

    ImageView shareImageView = findViewById(R.id.share);
    shareImageView.setOnClickListener( new View.OnClickListener() {
      public void onClick(View v) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, thisStarEvent.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, thisStarEvent.getDescription());
        share.putExtra(Intent.EXTRA_PHONE_NUMBER, thisStarEvent.getPhone());
        share.putExtra(Intent.EXTRA_TITLE, thisStarEvent.getLocationLine2());
        startActivity(Intent.createChooser(share, "Share Event"));
      }
    });

  }

  public static Date parse(String input ) throws java.text.ParseException {

    //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
    //things a bit.  Before we go on we have to repair this.
    SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.sssz" );

    //this is zero time so we need to add that TZ indicator for
    if ( input.endsWith( "Z" ) ) {
      input = input.substring( 0, input.length() - 1) + "GMT-00:00";
    } else {
      int inset = 6;

      String s0 = input.substring( 0, input.length() - inset );
      String s1 = input.substring( input.length() - inset, input.length() );

      input = s0 + "GMT" + s1;
    }

    return df.parse( input );

  }


}
