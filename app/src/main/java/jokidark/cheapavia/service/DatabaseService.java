package jokidark.cheapavia.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import jokidark.cheapavia.interfaces.ServerCallbackArray;
import jokidark.cheapavia.interfaces.ServerCallbackObject;
import jokidark.cheapavia.models.Trip;

public class DatabaseService {
    private final static String url = "http://apicheapavia.tk:8080";

    public static void getAll(Context context, ServerCallbackArray callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringBuilder err = new StringBuilder();
        JsonArrayRequest request = new JsonArrayRequest(url + "/get", callback::onSuccess, err::append);
        queue.add(request);
    }

    public static void insertTrip(Trip trip, Context context, ServerCallbackObject callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        Map<String, String>  params = new HashMap<>();
        params.put("price", String.valueOf(trip.getPrice()));
        params.put("destination", trip.getDestination());
        params.put("origin", trip.getOrigin());
        params.put("id", String.valueOf(trip.getId()));
        params.put("departure_date", trip.getDeparture_date().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url+"/insert", parameters, callback::onSuccess, new StringBuilder()::append);
        queue.add(stringRequest);
    }

    public static void getTrip(int id, Context context, ServerCallbackObject callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "/get?id="+id, new JSONObject(), callback::onSuccess, new StringBuilder()::append);
        queue.add(request);
    }

    public static boolean updateTrip(Trip trip, Context context){
        AtomicBoolean success = new AtomicBoolean(false);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/update",
                response -> success.set(true), error -> success.set(false)){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("price", String.valueOf(trip.getPrice()));
                params.put("destination", trip.getDestination());
                params.put("origin", trip.getOrigin());
                params.put("id", String.valueOf(trip.getId()));
                params.put("departure_date", trip.getDeparture_date().toString());

                return params;
            }
        };
        queue.add(stringRequest);
        return success.get();
    }

}
