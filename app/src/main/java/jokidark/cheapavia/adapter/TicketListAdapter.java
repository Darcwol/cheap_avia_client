package jokidark.cheapavia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jokidark.cheapavia.R;
import jokidark.cheapavia.models.Trip;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketViewHolder> {
    private Trip[] mDataSet;
    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TicketViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.list_item_ticket);
        }
    }

    public TicketListAdapter(ArrayList<Trip> dataSet){
        mDataSet = new Trip[dataSet.size()];
        mDataSet = dataSet.toArray(mDataSet);
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ticket, parent, false);
        return new TicketViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.textView.setText(mDataSet[position].toString());
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
