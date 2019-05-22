package com.example.weather_app2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.weather_app2.gson.Weather;
import com.example.weather_app2.gson.Weather2;
import com.example.weather_app2.gson.Weather3;
import com.example.weather_app2.gson.Weather4;
import com.example.weather_app2.util.HttpUtil;
import com.example.weather_app2.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        updateWeather();
        updateBingPic();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }
    private void updateWeather(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);
        String weatherString2=preferences.getString("weather2",null);
        String weatherString3=preferences.getString("weather3",null);
        String weatherString4=preferences.getString("weather4",null);
        if (weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            String weatherId=weather.basic.weatherId;
            String weatherUrl="https://free-api.heweather.net/s6/weather/now?location="+weatherId+"&key=7aa063924dec4fe3a2264895da5245ca";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);
                    if (weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this)
                                .edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
        if (weatherString2!=null){
            Weather2 weather2= Utility.handleWeatherResponse2(weatherString2);
            String weatherId2=weather2.basic.weatherId;
            String weatherUrl="https://free-api.heweather.net/s6/weather/forecast?location="+weatherId2+"&key=7aa063924dec4fe3a2264895da5245ca";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText2=response.body().string();
                    Weather2 weather2=Utility.handleWeatherResponse2(responseText2);
                    if (weather2!=null&&"ok".equals(weather2.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this)
                                .edit();
                        editor.putString("weather2",responseText2);
                        editor.apply();
                    }
                }
            });
        }
        if (weatherString3!=null){
            Weather3 weather3=Utility.handleWeatherResponse3(weatherString3);
            String weatherId3=weather3.basic.weatherId;
            String weatherUrl="https://free-api.heweather.net/s6/weather/lifestyle?location="+weatherId3+"&key=7aa063924dec4fe3a2264895da5245ca";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText3=response.body().string();
                    Weather3 weather3=Utility.handleWeatherResponse3(responseText3);
                    if (weather3!=null&&"ok".equals(weather3.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this)
                                .edit();
                        editor.putString("weather3",responseText3);
                        editor.apply();
                    }
                }
            });
        }
        if (weatherString4!=null){
            Weather4 weather4=Utility.handleWeatherResponse4(weatherString4);
            String weatherId4=weather4.basic.weatherId;
            String weatherUrl="https://free-api.heweather.net/s6/weather/hourly?location="+weatherId4+"&key=7aa063924dec4fe3a2264895da5245ca";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText4=response.body().string();
                    Weather4 weather4=Utility.handleWeatherResponse4(responseText4);
                    if (weather4!=null&&"ok".equals(weather4.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this)
                                .edit();
                        editor.putString("weather4",responseText4);
                        editor.apply();
                    }

                }
            });
        }
    }
    private void updateBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();

            }
        });
    }
}
