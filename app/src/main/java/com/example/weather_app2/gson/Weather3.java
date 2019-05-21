package com.example.weather_app2.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather3 {
    public Basic basic;
    public Update update;
    public String status;
    @SerializedName("lifestyle")
    public List<Suggestion>suggestionList;
}
