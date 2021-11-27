package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
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
    BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "InfoDashboard";
    Button discover;
    Button contactTracingDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dasboard);
        covid_button = (Button) findViewById(R.id.covid_update_button);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableDisableBT();
        contactTracingDashboard = findViewById(R.id.contactTracingDashboard);


        contactTracingDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Info_dashboard.this, ContactTracingDashboard.class));
            }
        });


        covid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCovidUpdate();
            }
        });

        state_covid_button = (Button) findViewById(R.id.statewise_covid);


        discover = findViewById(R.id.bluetoothDiscButton);
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Info_dashboard.this, bluetoothDiscovery.class));
            }
        });

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

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: Already Enabled BT.");
        }
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