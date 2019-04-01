package jokidark.cheapavia.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import jokidark.cheapavia.interfaces.ServerCallbackArray;

public class Airport {
    public String getCode() {
        return code;
    }
    private String code;
    public String getName() {
        return name;
    }
    private String name;

    public Airport(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public static void getJsonList(Context context, ServerCallbackArray callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final StringBuilder err = new StringBuilder();
        JsonArrayRequest request = new JsonArrayRequest("http://api.travelpayouts.com/data/ru/airports.json", callback::onSuccess, err::append);
        queue.add(request);
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
