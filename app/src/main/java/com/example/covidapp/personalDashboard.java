package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class personalDashboard extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView edit,logout;
    GoogleSignInClient mGoogleSignInClient;
    TextView name_txt,gender_text,age_text,phone_text,health_status;
    int num = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_dashboard);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("users");

        //edit=findViewById(R.id.edit);
        name_txt=findViewById(R.id.name_txt);
        gender_text=findViewById(R.id.gender_txt);
        age_text=findViewById(R.id.phone_txt);
        phone_text=findViewById(R.id.age_txt);
        health_status=findViewById(R.id.age_txt2);
        //logout=findViewById(R.id.logout);


//        logout.setOnClickListener(v->{
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//            mGoogleSignInClient = GoogleSignIn.getClient(personalDashboard.this, gso);
//
//            mGoogleSignInClient.signOut()
//                    .addOnCompleteListener(personalDashboard.this, task ->{
//                        Toast.makeText(personalDashboard.this, "Log out successfully...", Toast.LENGTH_SHORT).show();
//                    } );
//            auth.signOut();
//            startActivity(new Intent(personalDashboard.this , MainActivity.class));
//            finish();
//        });




        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue(String.class);
                String gender=snapshot.child("gender").getValue(String.class);
                String age=snapshot.child("age").getValue(String.class);
                String phone_number=snapshot.child("phone_number").getValue(String.class);
                String status=snapshot.child("status").getValue(String.class);

                name_txt.setText(name);
                gender_text.setText(gender);
                age_text.setText(age);
                phone_text.setText(phone_number);
                health_status.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        LineChart chart = (LineChart) findViewById(R.id.line_chart_personal);

        ArrayList<String> xAxis = this.getXaxis();
        ArrayList<Float> yAxis = this.getYaxis();

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0;i<this.num;i++)
        {
            entries.add(new Entry(i, yAxis.get(i)));
        }

        LineDataSet chartdata = new LineDataSet(entries, "Health Status");

        LineData lineData = new LineData(chartdata);

        chart.getDescription().setText("Personal Health Status");
        chart.animateXY(2000, 2000);
        chart.setData(lineData);
        chart.invalidate();

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