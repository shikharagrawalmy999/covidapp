package com.example.covidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<RecyclerData> tokenList;
    private Context currentContext;

    public RecyclerViewAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.tokenList = recyclerDataArrayList;
        this.currentContext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        RecyclerData recyclerData = tokenList.get(position);
        holder.city.setText(recyclerData.getCity());
        holder.state.setText(recyclerData.getState());
        holder.country.setText(recyclerData.getCountry());
        holder.postalCode.setText(recyclerData.getPostalCode());
        holder.date.setText(recyclerData.getDate());
        holder.time.setText(recyclerData.getTime());
        holder.healthStatus.setText(recyclerData.getHealthStatus());
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return tokenList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView city;
        private TextView state;
        private TextView country;
        private TextView postalCode;
        private TextView date;
        private TextView time;
        private TextView healthStatus;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.cityName);
            state = itemView.findViewById(R.id.stateName);
            country = itemView.findViewById(R.id.countryName);
            postalCode = itemView.findViewById(R.id.postalCode);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            healthStatus = itemView.findViewById(R.id.health_status);
        }
    }
}
