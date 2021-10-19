package com.example.covidapp;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

        System.out.println(str);

        String json_string=fetch_json_string();

        try {


            JSONObject root = new JSONObject(json_string);

            JSONArray array= root.getJSONArray("regionData");

            JSONObject object = null;

            for(int i=0;i<array.length();i++)
            {
                object = array.getJSONObject(i);
                if(str.equals(object.get("region"))){
                    break;
                }
            }

            System.out.println("htg_sensei");
            System.out.println(object);


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
}