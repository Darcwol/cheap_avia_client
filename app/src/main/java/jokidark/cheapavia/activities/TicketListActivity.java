package jokidark.cheapavia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import jokidark.cheapavia.R;
import jokidark.cheapavia.adapter.TicketListAdapter;
import jokidark.cheapavia.models.Airport;
import jokidark.cheapavia.models.Date;
import jokidark.cheapavia.models.RecyclerItemClickListener;
import jokidark.cheapavia.models.Trip;
import jokidark.cheapavia.service.DatabaseService;


public class TicketListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    public static ArrayList<Airport> airports;
    public static HashMap<String, String> airports_map;
   // ArrayList<Trip> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AddTicketActivity.updateActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_view);
        init();
    }


    void updateList() {
        SharedPreferences preferences = getSharedPreferences("tickets", MODE_PRIVATE);
        String ids = preferences.getString("ids", "");
        if (ids == null || ids.equals("")) {
            Intent intent = new Intent(this, AddTicketActivity.class);
            startActivity(intent);
        } else {
            String[] id_set_arr = ids.split(",");
            ArrayList<String> id_set = new ArrayList<>();
            Collections.addAll(id_set, id_set_arr);
            DatabaseService.getAll(this, result -> {
                ArrayList<Trip> tickets = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {
                    Trip trip = new Trip();
                    try {
                        JSONObject object = result.getJSONObject(i);
                        if (!id_set.contains(String.valueOf(object.getInt("id")))) continue;
                        trip.setId(object.getInt("id"));
                        Date date = new Date(
                                object.getJSONObject("departure_date").getString("year") + "-" +
                                        object.getJSONObject("departure_date").getString("month") + "-" +
                                        object.getJSONObject("departure_date").getString("day")

                        );
                        trip.setDeparture_date(date);
                        trip.setDestination(object.getString("destination"));
                        trip.setOrigin(object.getString("origin"));
                        trip.setPrice(object.getDouble("price"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tickets.add(trip);
                }
                mAdapter = new TicketListAdapter(tickets);
                recyclerView.setAdapter(mAdapter);
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = tickets.get(position).getId();
                        Intent selectedIntent = new Intent(getBaseContext(), TicketActivity.class);
                        selectedIntent.putExtra("id", id);
                        startActivity(selectedIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
            });
        }
    }

    private void init(){
        Airport.getJsonList(this, result -> {
            airports = new ArrayList<>();
            airports_map = new HashMap<>();
            try {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject airport = result.getJSONObject(i);
                    String name, code;
                    code = airport.getString("code");
                    name = airport.getJSONObject("name_translations").getString("en");
                    airports.add(new Airport(code, name));
                    airports_map.put(code, name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            setContentView(R.layout.activity_ticket_list);
            recyclerView = findViewById(R.id.ticketsListView);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            updateList();
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(this, AddTicketActivity.class);
                startActivity(intent);
            });
            updateList();
        });
        FirebaseApp.initializeApp(this);

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
