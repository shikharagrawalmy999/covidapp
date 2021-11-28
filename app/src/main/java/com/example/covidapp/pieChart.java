package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;

public class pieChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        PieChart piechart = findViewById(R.id.nationalPieChart_id);
    }
}