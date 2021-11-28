package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class dashboardDataAnalysis extends AppCompatActivity {

    int num=3; //change this to get more values on line chart
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_data_analyis);

        auth=FirebaseAuth.getInstance();


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
        CardView card_view5 = (CardView) findViewById(R.id.card_view_logout); // creating a CardView and assigning a value.
        card_view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(dashboardDataAnalysis.this, gso);

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(dashboardDataAnalysis.this, task ->{
                            Toast.makeText(dashboardDataAnalysis.this, "Log out successfully...", Toast.LENGTH_SHORT).show();
                        } );
                auth.signOut();
                startActivity(new Intent(dashboardDataAnalysis.this , MainActivity.class));
                finish();
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