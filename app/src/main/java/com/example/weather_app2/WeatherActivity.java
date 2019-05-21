package com.example.weather_app2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.preference.PreferenceManager;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.example.weather_app2.gson.Forecast;
import com.example.weather_app2.gson.Suggestion;
import com.example.weather_app2.gson.Weather;
import com.example.weather_app2.gson.Weather2;
import com.example.weather_app2.gson.Weather3;
import com.example.weather_app2.service.AutoUpdateService;
import com.example.weather_app2.util.HttpUtil;
import com.example.weather_app2.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity  {
    public DrawerLayout drawerLayout;
    private Button navButton;
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comforText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private Button location;
    private Button speak;
    private SpeechSynthesizer mSpeechSynthesizer;
    private String TEXT;
    private String appId = "16292219";
    private String appKey = "D02zId8pTT0TfRaRbEQVnnZ7";
    private String secretKey = "PLBpEVZWZyjalIXxpD2SnnH3MxAmzTlg";
    private TtsMode ttsMode = TtsMode.ONLINE;
    private String degree;
    private String weatherInfo;
    private String comfort;
    private String carWash;
    private String sport;
    private boolean statu=true;
    private ImageView imageView;
    private AnimationDrawable drawable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        speak=(Button)findViewById(R.id.speak);
        location=(Button)findViewById(R.id.location);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comforText=(TextView)findViewById(R.id.comfort_text);
        imageView=(ImageView)findViewById(R.id.play_anim);
        drawable=(AnimationDrawable) imageView.getBackground();



        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_green_light,android.R.color.holo_blue_dark);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);
        String weatherString2=preferences.getString("weather2",null);
        String weatherString3=preferences.getString("weather3",null);
         final String weatherId,weatherId2,weatherId3;
         drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
         navButton=(Button)findViewById(R.id.nav_button);
         location.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(WeatherActivity.this,MapActivity.class);
                 startActivity(intent);
             }
         });
         speak.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (statu){
                voice();}else {mSpeechSynthesizer.stop();drawable.selectDrawable(0);drawable.stop();statu=true;}
             }
         });
        if (weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
             weatherId=weather.basic.weatherId;
              showWeatherInfo(weather);

        }else {
             weatherId=getIntent().getStringExtra("weather_id");

            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        if (weatherString2!=null){
            Weather2 weather2=Utility.handleWeatherResponse2(weatherString2);
            weatherId2=weather2.basic.weatherId;
            showWeatherInfo2(weather2);
        }else{
            weatherId2=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather2(weatherId2);
        }
        if (weatherString3!=null){
            Weather3 weather3=Utility.handleWeatherResponse3(weatherString3);
            weatherId3=weather3.basic.weatherId;
            showWeatherInfo3(weather3);
        }else {
            weatherId3=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather3(weatherId3);

        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
                requestWeather2(weatherId2);
                requestWeather3(weatherId3);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        String bingPic=preferences.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }
    public void requestWeather(final String weatherId){
        String weatherUrl="https://free-api.heweather.net/s6/weather/now?location="+weatherId+"&key=7aa063924dec4fe3a2264895da5245ca";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s=weather.status;
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                         swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadBingPic();
    }
    public void requestWeather2(final String weatherId2){
        String weatherUrl="https://free-api.heweather.net/s6/weather/forecast?location="+weatherId2+"&key=7aa063924dec4fe3a2264895da5245ca";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText2=response.body().string();
                final Weather2 weather2=Utility.handleWeatherResponse2(responseText2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s=weather2.status;
                        if (weather2!=null&&"ok".equals(weather2.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather2",responseText2);
                            editor.apply();
                            showWeatherInfo2(weather2);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        } swipeRefresh.setRefreshing(false);

                    }
                });

            }
        });
    }
    public void requestWeather3(final String weatherId3){
        String weatherUrl="https://free-api.heweather.net/s6/weather/lifestyle?location="+weatherId3+"&key=7aa063924dec4fe3a2264895da5245ca";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText3=response.body().string();
                final Weather3 weather3=Utility.handleWeatherResponse3(responseText3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather3!=null&&"ok".equals(weather3.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather3",responseText3);
                            editor.apply();
                            showWeatherInfo3(weather3);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        } swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });
    }
    private void showWeatherInfo(Weather weather){
        if (weather!=null&&"ok".equals(weather.status)){
        String cityName=weather.basic.cityName;
        String updateTime=weather.update.update;
        degree=weather.now.temperature+"/℃";
        weatherInfo=weather.now.txt;
        String feel_tmp=weather.now.feel_tmp;
        String re_hum=weather.now.re_hum;
        aqiText.setText(feel_tmp);
        pm25Text.setText(re_hum);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        weatherLayout.setVisibility(View.VISIBLE);
            Intent intent=new Intent(this, AutoUpdateService.class);
            startService(intent);
    }else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }

    }
    private void showWeatherInfo2(Weather2 weather2){
        if (weather2!=null&&"ok".equals(weather2.status)){
            forecastLayout.removeAllViews();
            for (Forecast forecast:weather2.forecastList.subList(1,weather2.forecastList.size())){
                View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dateText=(TextView)view.findViewById(R.id.date_text);
                TextView infoText=(TextView)view.findViewById(R.id.info_text);
                TextView maxText=(TextView)view.findViewById(R.id.max_text);
                TextView minText=(TextView)view.findViewById(R.id.min_text);
                TextView infoText2=(TextView)view.findViewById(R.id.info2_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.info);
                maxText.setText(forecast.max);
                minText.setText(forecast.min);
                infoText2.setText(forecast.info2);
                forecastLayout.addView(view);
            }

        }else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }
    private void showWeatherInfo3(Weather3 weather3){
        if (weather3!=null&&"ok".equals(weather3.status)){
            for (Suggestion suggestion:weather3.suggestionList.subList(0,1)){
                comfort="舒适度："+suggestion.suggestions;
                comforText.setText(comfort);
            }
            for (Suggestion suggestion2:weather3.suggestionList.subList(6,7)){
                 carWash="洗车指数："+suggestion2.suggestions;
                carWashText.setText(carWash);
            }
            for (Suggestion suggestion3:weather3.suggestionList.subList(3,4)){
                sport="运动建议："+suggestion3.suggestions;
                sportText.setText(sport);
            }

        }else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }
    private void voice(){
        TEXT="今天天气"+weatherInfo+"，"+"温度"+degree+"，"+comfort+carWash+sport;
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);
        mSpeechSynthesizer.setAppId(appId);
        mSpeechSynthesizer.setApiKey(appKey, secretKey);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "4");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        mSpeechSynthesizer.initTts(ttsMode);
        mSpeechSynthesizer.speak(TEXT);
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
        drawable.start();
        statu=false;
    }
    @Override
    protected void onDestroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;

        }
        super.onDestroy();
    }
    SpeechSynthesizerListener listener=new SpeechSynthesizerListener() {
        @Override
        public void onSynthesizeStart(String s) {
          //合成启动
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
          //合成数据到达
        }

        @Override
        public void onSynthesizeFinish(String s) {
          //合成完成
        }

        @Override
        public void onSpeechStart(String s) {
          //朗读开始
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
            //语音播放中：返回码
        }

        @Override
        public void onSpeechFinish(String s) {
            //朗读结束
            mSpeechSynthesizer.stop();
            drawable.selectDrawable(0);
            drawable.stop();
        }

        @Override
        public void onError(String s, SpeechError speechError) {
          //异常
        }
    };

}
