package com.example.covidapp;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.* ;
import java.net.* ;
import java.util.*;

public class CovidUpdates extends AppCompatActivity{

    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_updates_india);

        System.out.println("htg_sensei");

        String json_string=fetch_json_string();

        JSONObject root = null;

        info = new ArrayList<>();

        try {

            root = new JSONObject(json_string);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            TextView tv1 = (TextView)findViewById(R.id.activeCases_id);
            tv1.setText("Total Active Cases: "+root.getString("activeCases"));

            TextView tv2 = (TextView)findViewById(R.id.newlyActiveCases_id);
            tv2.setText("New Active Cases: "+root.getString("activeCasesNew"));

            TextView tv3 = (TextView)findViewById(R.id.recoveredNew_id);
            tv3.setText("New Recovered: "+root.getString("recoveredNew"));

            TextView tv4 = (TextView)findViewById(R.id.deathsNew_id);
            tv4.setText("New Deaths: "+root.getString("deathsNew"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        PieChart pieChart = findViewById(R.id.nationalPieChart_id);

        ArrayList<PieEntry> parameter = new ArrayList<>();
        try{
            parameter.add(new PieEntry(abs(Integer.parseInt(root.getString("activeCasesNew"))),"New Active"));
            parameter.add(new PieEntry(abs(Integer.parseInt(root.getString("recoveredNew"))), "New Recovered"));
            parameter.add(new PieEntry(abs(Integer.parseInt(root.getString("deathsNew"))), "New Death"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        PieDataSet pieDataSet = new PieDataSet(parameter, "");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("COVID-DATA");
        pieChart.setData(pieData);
        pieChart.animate();
        pieChart.invalidate();




    }

    public String fetch_json_string() {
        final String[] json_string = {null};


        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL national_url = new URL("https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true");
                    URLConnection con = national_url.openConnection();

                    InputStream national_stream = con.getInputStream();

                    BufferedReader national_buffer = new BufferedReader(new InputStreamReader(national_stream));

                    String inputLine = null;

                    StringBuffer response = new StringBuffer();
                    while ((inputLine = national_buffer.readLine()) != null) {
                        response.append(inputLine);
                    }

                    national_buffer.close();

                    json_string[0] = response.toString();

                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Printing jsonstring");
        System.out.println(json_string[0]);
        return json_string[0];
    }

}