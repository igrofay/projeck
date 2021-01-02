package com.example.myapplication;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.StrictMode;


import org.json.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;

import java.util.stream.Collectors;

public class GetInfoWeather {
    Context context;
    public GetInfoWeather(Context context){
        this.context = context;
    }

    public String  getInfo(String city){
        if(checkInternetConnection(this.context)){
            String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&lang=ru&appid=06ed80ddb1827b8f65b8f2796f798dc7";
            ArrayList<String> allThatIsNeeded = jsonParser(getDataFromURL(url));
            if(allThatIsNeeded==null){
                return "Город не найден";
            }
            return "В "+allThatIsNeeded.get(0)+"\nс температурой "+String.format("%.2f",(Float.parseFloat(allThatIsNeeded.get(2))-273.15))+" °С\nсейчас "+allThatIsNeeded.get(1)+"\nс влажностью "+allThatIsNeeded.get(3)+"\nи со скоростью ветра "+allThatIsNeeded.get(4)+" м/c";
        }else return "Не получается подключиться к сети\nПопробуйте подключиться к сети";
    }

    private ArrayList<String> jsonParser(String dataFromURL) {
        try {
            ArrayList<String> allThatIsNeeded = new ArrayList<>();
            JSONObject reader = new JSONObject(dataFromURL);

            String name = reader.getString("name");
            allThatIsNeeded.add(name);

            JSONArray sys  = reader.getJSONArray("weather");
            JSONObject weather = new JSONObject(sys.getString(0));
            String country = weather.getString("description");
            allThatIsNeeded.add(country);

            JSONObject main  = reader.getJSONObject("main");
            String temperature = main.getString("temp");
            allThatIsNeeded.add(temperature);
            String humidity = main.getString("humidity");
            allThatIsNeeded.add(humidity);

            JSONObject wind  = reader.getJSONObject("wind");
            String speed = wind.getString("speed");
            allThatIsNeeded.add(speed);
            return allThatIsNeeded;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean checkInternetConnection(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnected()) && (networkInfo.isAvailable());
    }//конец checkInternetConnection

    private String getDataFromURL(String url1){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url =new URL(url1);
            InputStream inputStream = url.openStream();
            String result = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            }

            return result;
        } catch (IOException  e) {
            e.printStackTrace();
            return null;
        }
    }//конец getDataFromURL
}