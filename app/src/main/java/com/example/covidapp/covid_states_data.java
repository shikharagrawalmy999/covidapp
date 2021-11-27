package com.example.covidapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

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



public class covid_states_data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_updates_states_data);

        String json_string=fetch_json_string();

        try {

            ListView listView = (ListView) findViewById(R.id.state_list_view);

            List<String> items = new ArrayList<>();
            JSONObject root = new JSONObject(json_string);

            JSONArray array= root.getJSONArray("regionData");



            for(int i=0;i<array.length();i++)
            {
                JSONObject object= array.getJSONObject(i);
                items.add(object.getString("region"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);

            if (listView != null) {
                listView.setAdapter(adapter);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(covid_states_data.this, StateDataDisplay.class);
                    String message = (String) (listView.getItemAtPosition(position));

                    intent.putExtra("message_key", message);
                    startActivity(intent);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }



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
        System.out.println("Printing jsonstring");
        System.out.println(json_string[0]);
        return json_string[0];
    }
}