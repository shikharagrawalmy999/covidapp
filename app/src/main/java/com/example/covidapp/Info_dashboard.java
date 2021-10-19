package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
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

public class Info_dashboard extends AppCompatActivity {
    private Button covid_button;
    private Button state_covid_button;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView edit,logout;
    GoogleSignInClient mGoogleSignInClient;
    TextView name_txt,gender_text,age_text,phone_text,health_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dasboard);
        covid_button = (Button) findViewById(R.id.covid_update_button);

        covid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCovidUpdate();
            }
        });

        state_covid_button = (Button) findViewById(R.id.statewise_covid);

        state_covid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatewiseCovidUpdate();
            }
        });

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("users");

        edit=findViewById(R.id.edit);
        name_txt=findViewById(R.id.name_txt);
        gender_text=findViewById(R.id.gender_txt);
        age_text=findViewById(R.id.phone_txt);
        phone_text=findViewById(R.id.age_txt);
        health_status=findViewById(R.id.age_txt2);
        logout=findViewById(R.id.logout);


        logout.setOnClickListener(v->{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(Info_dashboard.this, gso);

            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(Info_dashboard.this, task ->{
                        Toast.makeText(Info_dashboard.this, "Log out successfully...", Toast.LENGTH_SHORT).show();
                    } );
            auth.signOut();
            startActivity(new Intent(Info_dashboard.this , MainActivity.class));
            finish();
        });


        edit.setOnClickListener(v->{
            Intent intent=new Intent(Info_dashboard.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        });

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

    }
    public void openCovidUpdate(){
        Intent intent = new Intent(this, CovidUpdates.class);
        startActivity(intent);
    }

    public void openStatewiseCovidUpdate(){
        Intent intent = new Intent(this, covid_states_data.class);
        startActivity(intent);
    }

}