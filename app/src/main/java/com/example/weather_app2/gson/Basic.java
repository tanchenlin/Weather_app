package com.example.weather_app2.gson;

import android.text.style.UpdateAppearance;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("location")
    public String cityName;
    @SerializedName("cid")
    public String weatherId;

}
