package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class covidUpdatesOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_updates_options);

        CardView card_view1 = (CardView) findViewById(R.id.card_view_national_updates); // creating a CardView and assigning a value.
        card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(covidUpdatesOptions.this, CovidUpdates.class);

                startActivity(intent);
            }
        });
        CardView card_view2 = (CardView) findViewById(R.id.card_view_state_updates); // creating a CardView and assigning a value.
        card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(covidUpdatesOptions.this, covid_states_data.class);
                startActivity(intent);
            }
        });

    }
}