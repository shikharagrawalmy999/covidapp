package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class dashboardDataAnalysis extends AppCompatActivity {

    int num=3; //change this to get more values on line chart



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_data_analyis);


        CardView card_view1 = (CardView) findViewById(R.id.card_view_personal); // creating a CardView and assigning a value.
        card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardDataAnalysis.this, personalDashboard.class);

                startActivity(intent);
            }
        });
        CardView card_view2 = (CardView) findViewById(R.id.card_view_updates); // creating a CardView and assigning a value.
        card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardDataAnalysis.this, covidUpdatesOptions.class);
                startActivity(intent);
            }
        });
        CardView card_view3 = (CardView) findViewById(R.id.card_view_edit); // creating a CardView and assigning a value.
        card_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboardDataAnalysis.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        CardView card_view4 = (CardView) findViewById(R.id.card_view_proximity); // creating a CardView and assigning a value.
        card_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardDataAnalysis.this, ContactTracingDashboard.class);

                startActivity(intent);
            }
        });



    }

    //need to use firebase to get stored data
    private ArrayList<String> getXaxis()
    {
        ArrayList<String> result = new ArrayList<String>();

        // Initialize an ArrayList with add()
        result.add("htg1");
        result.add("htg2");
        result.add("htg3");
        return result;
    }

    //need to use firebase to get quantified value of health
    private ArrayList<Float> getYaxis()
    {
        ArrayList<Float> result = new ArrayList<>();
        result.add(0.5f);
        result.add(0.75f);
        result.add(0.4f);
        return result;
    }

}