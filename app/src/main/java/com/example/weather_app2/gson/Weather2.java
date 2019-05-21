package com.example.weather_app2.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather2 {
    public Basic basic;
    public Update update;
    public String status;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
