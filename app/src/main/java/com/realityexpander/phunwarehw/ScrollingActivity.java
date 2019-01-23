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


}
