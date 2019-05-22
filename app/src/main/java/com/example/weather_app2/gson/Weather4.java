package com.example.weather_app2.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather4 {
    public Basic basic;
    public Update update;
    public String status;
    @SerializedName("hourly")
    public List<Hourly> hourlyList;
}
