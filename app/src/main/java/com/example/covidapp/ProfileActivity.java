package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    EditText name_edit,gender_edit,phone_number_edit,age_edit;
    TextView save;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    TextView questionairre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //binding views
        name_edit=findViewById(R.id.name_edit_text);
        gender_edit=findViewById(R.id.gender_edit_text);
        phone_number_edit=findViewById(R.id.phone_edit_text);
        age_edit=findViewById(R.id.age_edit_text);
        questionairre=findViewById(R.id.questionairre);

        save=findViewById(R.id.save);

        questionairre.setOnClickListener(v->{
            Intent info_dashboard=new Intent(ProfileActivity.this, questionairre.class);
            startActivity(info_dashboard);
        });

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("users");
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String phone_number = snapshot.child("phone_number").getValue(String.class);

                    name_edit.setText(name);
                    gender_edit.setText(gender);
                    phone_number_edit.setText(phone_number);
                    age_edit.setText(age);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        save.setOnClickListener(v->{
            save_data();
        });
    }

    private void save_data() {
        if(!name_edit.getText().toString().equals("")){
            if(!gender_edit.getText().toString().equals("")){
                if(!phone_number_edit.getText().toString().equals("") && phone_number_edit.getText().toString().length()==10){
                    if(!age_edit.getText().toString().equals("")){

                        reference.child(user.getUid()).child("name").setValue(name_edit.getText().toString());
                        reference.child(user.getUid()).child("gender").setValue(gender_edit.getText().toString());
                        reference.child(user.getUid()).child("phone_number").setValue(phone_number_edit.getText().toString());
                        reference.child(user.getUid()).child("age").setValue(age_edit.getText().toString());
                        Intent info_dashboard=new Intent(ProfileActivity.this, Info_dashboard.class);
                        startActivity(info_dashboard);
                        finish();
                        Toast.makeText(ProfileActivity.this, "Changes saved successfully.", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "Please enter your age", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Please enter 10 digit mobile no.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ProfileActivity.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(ProfileActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
    }
}