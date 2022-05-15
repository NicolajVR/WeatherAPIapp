package com.example.weatherapaapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    //Request for weather report.
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    //Variable
    String cityID;
    Context context;

    //Constructor.
    public WeatherDataService(Context context){
        this.context = context;
    }

    public interface  VolleyResponseListerner{
        void onError(String message);

        void onResponse(String cityID);
    }

    public void  getCityByID(String cityName, VolleyResponseListerner volleyResonseListerner){
        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityID = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Worked but did not return the ID number to MainActivity.
                //Toast.makeText(context, "City ID = " + cityID, Toast.LENGTH_SHORT).show();
                volleyResonseListerner.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show();
                volleyResonseListerner.onError("Something is wrong!");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

        //returned a null problem!
        //return cityID;
    }

    public interface ForecastByIDResponse{
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    //List of weatherReports
    public void getCityForecastByID(String cityID, ForecastByIDResponse forecastByIDResponse){
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        //URL for API.
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

        //get the json object (Callbacks)
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                    //get the first item in the array

                    //getting the first object, CASTING it to be an JSONObject. - if spelled incorrect it will crash.
                    for (int i=0; i< consolidated_weather_list.length(); i++){
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        WeatherReportModel one_day_weather = new WeatherReportModel();
                        one_day_weather.setId(first_day_from_api.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        one_day_weather.setCreated(first_day_from_api.getString("created"));
                        one_day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day_weather.setMax_temp(first_day_from_api.getLong("max_temp"));
                        one_day_weather.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day_weather.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        one_day_weather.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        one_day_weather.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        one_day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day_weather.setVisibility(first_day_from_api.getLong("visibility"));
                        one_day_weather.setPredictability(first_day_from_api.getInt("predictability"));
                        weatherReportModels.add(one_day_weather);
                    }
                    forecastByIDResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Run the request for weather report on Response.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName, final GetCityForecastByNameCallback getCityForecastByNameCallback){
        //fetch the city id given the city name
        getCityByID(cityName, new VolleyResponseListerner() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                //have the city id
                getCityForecastByID(cityID, new ForecastByIDResponse(){
                    @Override
                    public void onError(String message){

                    }
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels){
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }
                });
            }
        });

        //fetch the city forecast given the city id.
    }
}

//TODO - Fix Buttons, Middle buttons takes name and shows info (should NOT do that)
//TODO - Third button still reads out what you typed insted of giving info based on name