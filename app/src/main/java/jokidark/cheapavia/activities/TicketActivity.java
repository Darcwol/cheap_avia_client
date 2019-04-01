package jokidark.cheapavia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import jokidark.cheapavia.R;
import jokidark.cheapavia.models.Date;
import jokidark.cheapavia.models.Trip;
import jokidark.cheapavia.service.DatabaseService;

public class TicketActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TicketListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_view);
        int id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                String str = bundle.getString("id");
                if (str != null)
                    id = Integer.parseInt(str);
                else
                    Log.e("TicketActivity", "Not a number");
            }
        }
        DatabaseService.getTrip(id, this, selectedResult -> {
            Trip selectedTrip = new Trip();
            try {
                selectedTrip.setId(selectedResult.getInt("id"));
                selectedTrip.setDeparture_date(new Date(
                        selectedResult.getJSONObject("departure_date").getString("year") + "-" +
                                selectedResult.getJSONObject("departure_date").getString("month") + "-" +
                                selectedResult.getJSONObject("departure_date").getString("day")));
                selectedTrip.setDestination(selectedResult.getString("destination"));
                selectedTrip.setOrigin(selectedResult.getString("origin"));
                selectedTrip.setPrice(selectedResult.getDouble("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setContentView(R.layout.activity_ticket);

            TextView origin = findViewById(R.id.origin);
            TextView destination = findViewById(R.id.destination);
            TextView date = findViewById(R.id.date);
            TextView price = findViewById(R.id.price);
            TextView ticketName = findViewById(R.id.ticket_name);
            Button buyBtn = findViewById(R.id.buyBtn);

            origin.setText(getString(R.string.origin, selectedTrip.getHumanOrigin()));
            destination.setText(getString(R.string.destination, selectedTrip.getHumanDestination()));
            date.setText(getString(R.string.date, selectedTrip.getDeparture_date().toHumanString()));
            ticketName.setText(getString(R.string.ticketName, selectedTrip.getOrigin(), selectedTrip.getDestination()));
            price.setText(getString(R.string.price, String.valueOf(selectedTrip.getPrice()) + " ₴"));
            if(selectedTrip.getPrice() == 0){
                buyBtn.setClickable(false);
                buyBtn.setOnClickListener(view -> {
                    Toast.makeText(this, "Sorry, there is no cheap ticket now. " +
                            "We'll notify you, when we find something", Toast.LENGTH_LONG).show();
                });
            }
            else{
                buyBtn.setOnClickListener(view -> {
                    Toast.makeText(this, "Sorry, now you cannot buy ticket from here, " +
                            "but we are working on this. Go to aviasales.ru and buy this ticket.", Toast.LENGTH_LONG).show();
                });
            }

        });


    }
}
