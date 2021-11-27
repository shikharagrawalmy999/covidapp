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

public class questionnaire extends AppCompatActivity {
    public ArrayList<CheckBox> checkboxes;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Button submit_button;
    double healthRatio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        submit_button=findViewById(R.id.submit_button);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users");

        checkboxes=new ArrayList<>();
        checkboxes.add((CheckBox)findViewById(R.id.symp_cough_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_fever_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_sore_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_body_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_breathing_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_hearing_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_pink_id));
        checkboxes.add((CheckBox)findViewById(R.id.symp_loss_id));
        checkboxes.add((CheckBox)findViewById(R.id.none1_id));

        checkboxes.add((CheckBox)findViewById(R.id.diabetes_id));
        checkboxes.add((CheckBox)findViewById(R.id.lung_id));
        checkboxes.add((CheckBox)findViewById(R.id.asthma_id));
        checkboxes.add((CheckBox)findViewById(R.id.hypertension_id));
        checkboxes.add((CheckBox)findViewById(R.id.kidney_id));
        checkboxes.add((CheckBox)findViewById(R.id.none2_id));

        checkboxes.add((CheckBox)findViewById(R.id.yes_id));
        checkboxes.add((CheckBox)findViewById(R.id.yes_2_id));
        checkboxes.add((CheckBox)findViewById(R.id.no_id));

        checkboxes.add((CheckBox)findViewById(R.id.ten_id));
        checkboxes.add((CheckBox)findViewById(R.id.five_id));
        checkboxes.add((CheckBox)findViewById(R.id.zero_id));
        checkboxes.add((CheckBox)findViewById(R.id.none3_id));

        submit_button.setOnClickListener(v->{
            if(checkboxes.get(0).isChecked()||checkboxes.get(1).isChecked()||checkboxes.get(2).isChecked()||checkboxes.get(3).isChecked()||checkboxes.get(4).isChecked()||checkboxes.get(5).isChecked()||checkboxes.get(6).isChecked()||checkboxes.get(7).isChecked()||checkboxes.get(8).isChecked()){
                if(checkboxes.get(9).isChecked()||checkboxes.get(10).isChecked()||checkboxes.get(11).isChecked()||checkboxes.get(12).isChecked()||checkboxes.get(13).isChecked()||checkboxes.get(14).isChecked()){
                    if(checkboxes.get(15).isChecked()||checkboxes.get(16).isChecked()||checkboxes.get(17).isChecked()){
                        if(checkboxes.get(18).isChecked()||checkboxes.get(19).isChecked()||checkboxes.get(20).isChecked()||checkboxes.get(21).isChecked()){
                            healthRatio=Health_status();
                            Check();
                        }
                        else{
                            Toast.makeText(questionnaire.this,"Please answer 4th question", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(questionnaire.this, "Please answer 3rd question", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(questionnaire.this, "Please answer 2nd question", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(questionnaire.this, "Please answer 1st question", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Check() {
        reference.child(user.getUid()).child("status").setValue(healthRatio);
        QuestionOne response1 = new QuestionOne();
        QuestionThree response3 = new QuestionThree();
        QuestionTwo response2 = new QuestionTwo();
        QuestionFour response4 = new QuestionFour();

        response1.setFever(checkboxes.get(0).isChecked());
        response1.setSoreThroat(checkboxes.get(1).isChecked());
        response1.setCough(checkboxes.get(2).isChecked());
        response1.setDifficultyInBreathing(checkboxes.get(3).isChecked());
        response1.setBodyAche(checkboxes.get(4).isChecked());
        response1.setSmellTaste(checkboxes.get(5).isChecked());
        response1.setPinkEyes(checkboxes.get(6).isChecked());
        response1.setHearingImpairment(checkboxes.get(7).isChecked());
        response1.setNone(checkboxes.get(8).isChecked());

        response2.setLungDisease(checkboxes.get(9).isChecked());
        response2.setAsthma(checkboxes.get(10).isChecked());
        response2.setDiabetes(checkboxes.get(11).isChecked());
        response2.setHypertension(checkboxes.get(12).isChecked());
        response2.setKidneyDisorder(checkboxes.get(13).isChecked());
        response2.setNone(checkboxes.get(14).isChecked());

        response3.setNegative(checkboxes.get(15).isChecked());
        response3.setPositive(checkboxes.get(16).isChecked());
        response3.setNo(checkboxes.get(17).isChecked());

        response4.setLast10To14(checkboxes.get(18).isChecked());
        response4.setLast5To10(checkboxes.get(19).isChecked());
        response4.setLast0To5(checkboxes.get(20).isChecked());
        response4.setNone(checkboxes.get(21).isChecked());

        reference.child(user.getUid()).child("Question1").setValue(response1);
        reference.child(user.getUid()).child("Question2").setValue(response2);
        reference.child(user.getUid()).child("Question3").setValue(response3);
        reference.child(user.getUid()).child("Question4").setValue(response4);

        Toast.makeText(getApplicationContext(), "Questionnaire submitted", Toast.LENGTH_SHORT).show();
        finish();
    }

    public double Health_status()
    {
        if(checkboxes.get(16).isChecked()) return 1;
        double status=0;
        for (int i=0;i<8;i++){
            if (checkboxes.get(i).isChecked()) status=status+5;
        }
        for (int i=9;i<14;i++){
            if (checkboxes.get(i).isChecked()){
                if (status>0) status =status+0.5;
            }
        }
        if ((checkboxes.get(15).isChecked())&&(status>0)) status-=2;

        if (checkboxes.get(18).isChecked()) status +=5;
        if (checkboxes.get(19).isChecked()) status +=7;
        if (checkboxes.get(20).isChecked()) status +=10;

        status=status/52.5;
        return status;
    }

}