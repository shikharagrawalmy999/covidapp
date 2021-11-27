package com.example.covidapp;
import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class StateDataDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_updates_states);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");

        //System.out.println(str);

        String json_string=fetch_json_string();
        JSONObject object = null;

        try {


            JSONObject root = new JSONObject(json_string);

            JSONArray array= root.getJSONArray("regionData");



            for(int i=0;i<array.length();i++)
            {
                object = array.getJSONObject(i);
                if(str.equals(object.get("region"))){
                    break;
                }
            }



            TextView tv1 = (TextView)findViewById(R.id.state_name_id);
            tv1.setText(str);
            TextView tv2 = (TextView)findViewById(R.id.state_active_cases_id);
            tv2.setText(object.getString("activeCases"));
            TextView tv3 = (TextView)findViewById(R.id.state_new_infected_id);
            tv3.setText(object.getString("newInfected"));
            TextView tv4 = (TextView)findViewById(R.id.state_recovered_id);
            tv4.setText(object.getString("recovered"));
            TextView tv5 = (TextView)findViewById(R.id.state_new_recovered_id);
            tv5.setText(object.getString("newRecovered"));


        } catch(Exception e) {
            e.printStackTrace();
        }

        PieChart pieChart = findViewById(R.id.pieChart_view);

        ArrayList<PieEntry> parameter = new ArrayList<>();
        try{
            parameter.add(new PieEntry(abs(Integer.parseInt(object.getString("activeCases"))),"Active Cases"));
            parameter.add(new PieEntry(abs(Integer.parseInt(object.getString("newInfected"))), "Newly Infected"));
            parameter.add(new PieEntry(abs(Integer.parseInt(object.getString("newRecovered"))), "Newly Recovered"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        PieDataSet pieDataSet = new PieDataSet(parameter, "");
        //setting x axis
//        String[] xAxisLables = new String[]{"Active Cases", "Newly Infected", "Recovered", "Newly Recovered"};
//        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));

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
                    URL state_url = new URL("https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true");
                    URLConnection con = state_url.openConnection();

                    InputStream state_stream = con.getInputStream();

                    BufferedReader state_buffer = new BufferedReader(new InputStreamReader(state_stream));

                    String inputLine = null;

                    StringBuffer response = new StringBuffer();
                    while ((inputLine = state_buffer.readLine()) != null) {
                        response.append(inputLine);
                    }

                    state_buffer.close();

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
        return json_string[0];
    }
    private BarDataSet getDataSet(JSONObject object) {

        ArrayList<BarEntry> valueSet = new ArrayList<>();
        try{
            valueSet.add(new BarEntry(0,Integer.parseInt(object.getString("activeCases"))));
            valueSet.add(new BarEntry(1,Integer.parseInt(object.getString("newInfected"))));
            valueSet.add(new BarEntry(2,Integer.parseInt(object.getString("recovered"))));
            valueSet.add(new BarEntry(3,Integer.parseInt(object.getString("newRecovered"))));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "Numbers");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        return barDataSet;
    }

}
