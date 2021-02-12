package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    TextView textView = findViewById(R.id.textView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        button.setOnClickListener(v -> {
            getWeather(editText.getText().toString());
        });
    }
    private void getWeather(String city){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=ru&appid=06ed80ddb1827b8f65b8f2796f798dc7",
                this::outputData,
                error -> {
            textView.setText("Ошибка");
                }
        );
        queue.add(request);
    }
    private void outputData(String data){
        WeatherData weatherData = new Gson().fromJson(data,WeatherData.class);
        textView.setText(weatherData.temp);
    }

}