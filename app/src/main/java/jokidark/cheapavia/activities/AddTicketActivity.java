package jokidark.cheapavia.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import jokidark.cheapavia.R;
import jokidark.cheapavia.adapter.AirportAutoCompleteAdapter;
import jokidark.cheapavia.adapter.TicketListAdapter;
import jokidark.cheapavia.models.Airport;
import jokidark.cheapavia.models.AirportAutoCompleteTextView;
import jokidark.cheapavia.models.Date;
import jokidark.cheapavia.models.Trip;
import jokidark.cheapavia.service.DatabaseService;

public class AddTicketActivity extends AppCompatActivity {

    private static WeakReference<TicketListActivity> ticketListActivityWeakReference;
    public static void updateActivity(TicketListActivity activity) {
        ticketListActivityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        final AirportAutoCompleteTextView origin = findViewById(R.id.origin);
        final AirportAutoCompleteTextView destination = findViewById(R.id.destination);
        final TextView date = findViewById(R.id.flightDate);
        date.setShowSoftInputOnFocus(false);
        date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        date.setOnClickListener((v) -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        });
        Button save = findViewById(R.id.saveRes);


        origin.setThreshold(1);
        origin.setAdapter(new AirportAutoCompleteAdapter(this));
        origin.setLoadingIndicator(findViewById(R.id.progress_bar));
        origin.setOnFocusChangeListener((view, b) -> {
            if(!b){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        origin.setOnItemClickListener((adapterView, view, position, id) -> {
            Airport airport = (Airport) adapterView.getItemAtPosition(position);
            origin.setText(airport.toString());
        });


        destination.setThreshold(1);
        destination.setAdapter(new AirportAutoCompleteAdapter(this));
        destination.setLoadingIndicator(findViewById(R.id.progress_bar));
        destination.setOnFocusChangeListener((view, b) -> {
            if(!b){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        destination.setOnItemClickListener((adapterView, view, position, id) -> {
            Airport airport = (Airport) adapterView.getItemAtPosition(position);
            destination.setText(airport.toString());
        });


        save.setOnClickListener(view -> {
            Date trip_date = new Date(date.getText().toString(), true);
            String trip_dest = null, trip_origin = null;
            try {
                trip_dest = destination.getText().toString().substring(
                        destination.getText().toString().indexOf("(") + 1,
                        destination.getText().toString().indexOf(")"));
            } catch (Exception e){
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }
            try{
                trip_origin = origin.getText().toString().substring(
                        origin.getText().toString().indexOf("(") + 1,
                        origin.getText().toString().indexOf(")"));
            } catch (Exception e){
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }
            if (trip_dest != null && trip_origin != null) {
                Trip trip = new Trip(trip_date, trip_dest, trip_origin);
                DatabaseService.insertTrip(trip, this, result -> {
                    try {
                        FirebaseMessaging.getInstance().subscribeToTopic("ticket_n" + String.valueOf(result.getInt("id")));
                        SharedPreferences tickets = getSharedPreferences("tickets", MODE_PRIVATE);
                        SharedPreferences.Editor editor = tickets.edit();
                        String ids = tickets.getString("ids", "");
                        ids+=result.getInt("id")+",";
                        editor.putString("ids", ids);
                        editor.apply();
                        String[] id_set_arr = ids.split(",");
                        ArrayList<String> id_set = new ArrayList<>();
                        Collections.addAll(id_set, id_set_arr);
                        DatabaseService.getAll(this, result1 -> {
                            ArrayList<Trip> ids_arr = new ArrayList<>();
                            for (int i = 0; i < result1.length(); i++) {
                                Trip trip1 = new Trip();
                                try {
                                    JSONObject object = result1.getJSONObject(i);
                                    if (!id_set.contains(String.valueOf(object.getInt("id"))))
                                        continue;
                                    trip1.setId(object.getInt("id"));
                                    Date date1 = new Date(
                                            object.getJSONObject("departure_date").getString("year") + "-" +
                                                    object.getJSONObject("departure_date").getString("month") + "-" +
                                                    object.getJSONObject("departure_date").getString("day")

                                    );
                                    trip1.setDeparture_date(date1);
                                    trip1.setDestination(object.getString("destination"));
                                    trip1.setOrigin(object.getString("origin"));
                                    trip1.setPrice(object.getDouble("price"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ids_arr.add(trip1);
                                int id = trip1.getId();
                                Intent selectedIntent = new Intent(getBaseContext(), TicketActivity.class);
                                selectedIntent.putExtra("id", id);
                                startActivity(selectedIntent);
                            }
                            TicketListAdapter mAdapter = new TicketListAdapter(ids_arr);
                            ticketListActivityWeakReference.get().recyclerView.setAdapter(mAdapter);
                            ticketListActivityWeakReference.get().updateList();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
