package com.example.weatherapaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Class Variables
    Button btn_cityID, btn_getWeatherByID, btn_getWeatherByName;
    EditText et_dataInput;
    ListView lv_weatherReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets ID to the Variables of objects from layout.
        btn_cityID = findViewById(R.id.btn_getCityID);
        btn_getWeatherByID = findViewById(R.id.btn_getWeatherByCityID);
        btn_getWeatherByName = findViewById(R.id.btn_getWeatherByCityName);

        et_dataInput = findViewById(R.id.et_datainput);
        lv_weatherReport = findViewById(R.id.lv_weatherReport);

        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        //Click Listeners (Click Events)
        btn_cityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Callback
                weatherDataService.getCityByID(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListerner() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Somethings wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "Returned an ID of " + cityID, Toast.LENGTH_SHORT).show();
                    }
                });
                //Button to get weather report based on ID.
                btn_getWeatherByID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //test to get london weatherReport.
                        weatherDataService.getCityForecastByID(et_dataInput.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Somethings wrong!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReportModel> weatherReportModels) {
                                //put the entire list into the listview control

                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                                lv_weatherReport.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });
                //Button to get the weather Report by Name.
                btn_getWeatherByName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //test to get london weatherReport.
                        weatherDataService.getCityForecastByName(et_dataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Somethings wrong!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReportModel> weatherReportModels) {
                                //put the entire list into the listview control

                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                                lv_weatherReport.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });
            }
        });
    }
}

//Tutorial Followed: https://www.youtube.com/watch?v=xPi-z3nOcn8