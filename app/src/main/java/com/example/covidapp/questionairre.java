package com.example.covidapp;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class questionairre extends AppCompatActivity {
    public ArrayList<CheckBox> checkboxes;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Button submit_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionairre);

        submit_button=findViewById(R.id.submit_button);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users");

        checkboxes=new ArrayList<>();
        checkboxes.add((CheckBox)findViewById(R.id.symp_cough_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_fever_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_sore_id));
        checkboxes.add((CheckBox)findViewById(R.id.none1_id));

        checkboxes.add((CheckBox)findViewById(R.id.diabetes_id));
        checkboxes.add((CheckBox)findViewById(R.id.lung_id));
        checkboxes.add((CheckBox)findViewById(R.id.asthma_id));
        checkboxes.add((CheckBox)findViewById(R.id.none2_id));

        checkboxes.add((CheckBox)findViewById(R.id.ten_id));
        checkboxes.add((CheckBox)findViewById(R.id.five_id));
        checkboxes.add((CheckBox)findViewById(R.id.zero_id));
        checkboxes.add((CheckBox)findViewById(R.id.none3_id));

        submit_button.setOnClickListener(v->{
            if(checkboxes.get(0).isChecked()||checkboxes.get(1).isChecked()||checkboxes.get(2).isChecked()||checkboxes.get(3).isChecked()){
                if(checkboxes.get(4).isChecked()||checkboxes.get(5).isChecked()||checkboxes.get(6).isChecked()||checkboxes.get(7).isChecked()){
                    if(checkboxes.get(8).isChecked()||checkboxes.get(9).isChecked()||checkboxes.get(10).isChecked()||checkboxes.get(11).isChecked()){
                        Check();
                    }
                    else{
                        Toast.makeText(questionairre.this, "Please answer 3rd question", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(questionairre.this, "Please answer 2nd question", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(questionairre.this, "Please answer 1st question", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Check()
    {
        String msg="";

        // Concatenation of the checked options in if

        // isChecked() is used to check whether
        // the CheckBox is in true state or not.

        if((checkboxes.get(0).isChecked()||checkboxes.get(2).isChecked()) && checkboxes.get(10).isChecked())
            reference.child(user.getUid()).child("status").setValue("At Risk");
        else reference.child(user.getUid()).child("status").setValue("Healthy");
        finish();

    }
}