package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class personalDashboard extends AppCompatActivity {

    DatabaseReference reference;
    DatabaseReference status_reference;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView edit,logout;
    GoogleSignInClient mGoogleSignInClient;
    TextView name_txt,gender_text,age_text,phone_text,health_status,infected_text;
    double quantified_health;
    int num = 3;
    long i = 0;
    long number_children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);

        ProgressBar healthProgressBar=(ProgressBar)findViewById(R.id.progressBar_id); // initiate the progress bar
        healthProgressBar.setMax(100);



        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("users");

        name_txt=findViewById(R.id.name_txt);
        gender_text=findViewById(R.id.gender_txt);
        age_text=findViewById(R.id.phone_txt);
        phone_text=findViewById(R.id.age_txt);
        health_status=findViewById(R.id.health_val_id);
        infected_text = findViewById(R.id.infectedContacted_id);




        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue(String.class);
                String gender=snapshot.child("gender").getValue(String.class);
                String age=snapshot.child("age").getValue(String.class);
                String phone_number=snapshot.child("phone_number").getValue(String.class);
                String status = snapshot.child("status").getValue(String.class);
                String alert = snapshot.child("alert").getValue(String.class);

                status = "" +  (100 - ((int) (Double.parseDouble(status)*100)));

                name_txt.setText(name);
                gender_text.setText(gender);
                age_text.setText(age);
                phone_text.setText(phone_number);
                health_status.setText(status);

                if(alert==null)
                {

                }
                else if(alert.equals("Contact Hospital"))
                {
                    infected_text.setText(alert);
                }

                healthProgressBar.setProgress(Integer.parseInt(status));
              //  healthProgressBar.setProgress((int) (quantified_health*100));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        getYaxis();

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
//        result.add(0.5f);
//        result.add(0.75f);
//        result.add(0.4f);
        status_reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("status_arr");

        status_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number_children = snapshot.getChildrenCount();


                for (DataSnapshot data : snapshot.getChildren()){

                    Float x = Float.parseFloat((String) data.getValue());
                    System.out.println("htgrocks"+x);
                    result.add(x);
                    i++;
                }
                if(i==number_children)
                {
                    LineChart chart = (LineChart) findViewById(R.id.line_chart_personal);


                    ArrayList<Entry> entries = new ArrayList<>();
                    System.out.println("hi");
                    for(int j=0;j<number_children;j++)
                    {
                        entries.add(new Entry(j, result.get(j)));
                        System.out.println("hi");
                    }

                    LineDataSet chartdata = new LineDataSet(entries, "Health Status");

                    LineData lineData = new LineData(chartdata);

                    chart.getDescription().setText("Personal Health Status");
                    chart.animateXY(2000, 2000);
                    chart.setData(lineData);
                    chart.invalidate();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error occurred!");
            }
        });

        return result;
    }


}