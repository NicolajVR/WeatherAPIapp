package com.example.weatherapaapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//HTTP Queue System.

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    //Constructor - makes it so we only can make one instance at the time.
    private MySingleton(Context context){
        ctx = context;
        requestQueue = getRequestQueue();

    }
    //Checks if the instance is in memory.
    public static synchronized MySingleton getInstance(Context context){
        if(instance == null){
            //Creates a new instance if its not in memory.
            instance = new MySingleton(context);
        }
        //returns the instance if its already in memory.
        return instance;
    }
    
    //checks to see if there is a RequestQueue.
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            //if there is no req que, it creates one.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        //if the req que already exits it will return it.
        return requestQueue;
    }
    
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
