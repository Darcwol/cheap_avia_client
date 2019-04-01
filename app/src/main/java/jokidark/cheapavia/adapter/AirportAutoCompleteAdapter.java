package jokidark.cheapavia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jokidark.cheapavia.R;
import jokidark.cheapavia.activities.TicketListActivity;
import jokidark.cheapavia.models.Airport;

public class AirportAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private final Context mContext;
    private List<Airport> mResults;

    public AirportAutoCompleteAdapter(Context context){
        mContext = context;
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Airport getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        Airport airport = getItem(position);
        ((TextView) convertView.findViewById(R.id.text1)).setText(airport.getCode());
        ((TextView) convertView.findViewById(R.id.text2)).setText(airport.getName());

        return convertView;
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    List<Airport> airports = findAirports(String.valueOf(constraint));
                    filterResults.values = airports;
                    filterResults.count = airports.size();
                }
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0){
                    mResults = (List<Airport>) results.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<Airport> findAirports(String t){
        ArrayList<Airport> airports = new ArrayList<>();

        if(TicketListActivity.airports != null) {
            TicketListActivity.airports.forEach(airport -> {
                if(airport.getName().toLowerCase().contains(t.toLowerCase())) airports.add(airport);
            });
        }

        return airports;
    }
}
