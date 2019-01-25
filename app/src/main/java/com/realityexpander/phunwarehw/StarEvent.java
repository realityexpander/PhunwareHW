package com.realityexpander.phunwarehw;

import android.content.Context;
import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chris Athanas on 1/23/19.
 */

public class StarEvent implements Serializable {
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

  public static void shareIntent(Context context, StarEvent thisStarEvent) {
    Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("text/plain");
    share.putExtra(Intent.EXTRA_SUBJECT, thisStarEvent.getTitle());
    share.putExtra(Intent.EXTRA_TEXT, thisStarEvent.getTitle() +", "+ thisStarEvent.getDescription());
    share.putExtra(Intent.EXTRA_PHONE_NUMBER, thisStarEvent.getPhone());
    share.putExtra(Intent.EXTRA_TITLE, thisStarEvent.getLocationLine2());
    context.startActivity(Intent.createChooser(share, "Share Event"));
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

  public String getTimeStamp() {
    return timeStamp;
  }
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }

  public String getLocationLine1() {
    return locationLine1;
  }
  public void setLocationLine1(String locationLine1) {
    this.locationLine1 = locationLine1;
  }

  public String getLocationLine2() {
    return locationLine2;
  }
  public void setLocationLine2(String locationLine2) {
    this.locationLine2 = locationLine2;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /*
  TOSTRING
   */
  @Override
  public String toString() {
    return title;
  }
}