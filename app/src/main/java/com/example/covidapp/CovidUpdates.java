package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.* ;
import java.net.* ;
import java.util.*;

public class CovidUpdates extends AppCompatActivity{
    public Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_updates_india);

        System.out.println("htg_sensei");



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL country_url = new URL("https://coronavirus-19-api.herokuapp.com/countries/india");

                    URLConnection con=country_url.openConnection();

                    InputStream country_stream = con.getInputStream();

                    BufferedReader country_buffer = new BufferedReader(new InputStreamReader(country_stream));



                    String line = country_buffer.readLine();

                    // read each line and write to System.out
                    line = line.substring(1, line.length()-1);           //remove curly brackets
                    String[] keyValuePairs = line.split(",");              //split the string to create key-value pairs
                    map = new HashMap<String,String>();

                    for(String pair : keyValuePairs)                        //iterate over the pairs
                    {
                        String[] entry = pair.split(":");                   //split the pairs to get key and value

                        map.put(entry[0].trim(),entry[1].trim());

                    }


                } catch (Exception e) {
                    System.out.println("Caught htg exception");
                    e.printStackTrace();
                }


            }
        });

        thread.start();
        try {
            thread.join();
        }catch(Exception e){
            System.out.println(e);
        }

        TextView tv1 = (TextView)findViewById(R.id.total_cases_id);
        tv1.setText(map.get("\"cases\""));

        TextView tv2 = (TextView)findViewById(R.id.today_cases_id);
        tv2.setText(map.get("\"todayCases\""));



    }

}