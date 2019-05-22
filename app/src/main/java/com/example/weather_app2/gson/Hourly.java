package com.example.weather_app2.gson;

import com.google.gson.annotations.SerializedName;

public class Hourly {
    @SerializedName("time")
    public String time;
    @SerializedName("cond_txt")
    public String txt;
    @SerializedName("tmp")
    public String tmp;
}