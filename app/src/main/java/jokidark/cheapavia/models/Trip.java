package jokidark.cheapavia.models;

import android.support.annotation.NonNull;

import jokidark.cheapavia.activities.TicketListActivity;

public class Trip {
    private double price;
    private Date departure_date;
    private String destination;
    private String origin;
    private int id;

    public Trip() {
    }

    public Trip(Date departure_date, String destination, String origin){
        this(0, departure_date, destination, origin);
    }

    Trip(double price, Date departure_date, String destination, String origin){
        this(price, departure_date, destination, origin, 0);
    }

    Trip(double price,
         Date departure_date, String destination, String origin, int id) {
        this.price = price;
        this.departure_date = departure_date;
        this.destination = destination;
        this.origin = origin;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @NonNull
    @Override
    public String toString() {
        String origin, destination;
        if(TicketListActivity.airports_map != null) {
            destination = TicketListActivity.airports_map.get(this.destination) + "(" + this.destination + ")";
            origin = TicketListActivity.airports_map.get(this.origin) + "(" + this.origin + ")";
        }
        else {
            destination = this.destination;
            origin = this.origin;
        }
        return  "Price: " + price + " ₴" +
                "\nDeparture date: " + departure_date.toHumanString() +
                "\nOrigin: " + origin +
                "\nDestination: " +  destination + "\n____________________________________________________";
    }

    public String getHumanOrigin(){
        if(TicketListActivity.airports_map != null) {
            return TicketListActivity.airports_map.get(this.origin) + "(" + this.origin + ")";
        }
        return origin;
    }

    public String getHumanDestination(){
        if(TicketListActivity.airports_map != null) {
            return TicketListActivity.airports_map.get(this.destination) + "(" + this.destination + ")";
        }
        return destination;
    }

    @Override
    public boolean equals(Object obj) {
        Trip trip = (Trip)obj;
        return this.price==trip.price &&
                this.id == trip.id &&
                this.destination.equals(trip.destination) &&
                this.origin.equals(trip.origin) &&
                this.departure_date.equals(trip.departure_date);
    }
}
