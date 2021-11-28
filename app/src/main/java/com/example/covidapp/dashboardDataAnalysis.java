package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Date;

public class dashboardDataAnalysis extends AppCompatActivity implements LocationListener {

    private static final String TAG = "dashboardDataAnalysis";
    int num=3; //change this to get more values on line chart
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    protected LocationManager locationManager;
    protected Context context;
    protected String latitude, longitude;


    FirebaseUser user;
    DatabaseReference reference;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> bluetoothList;

    public void startThread(){
        Runnable r2=new Runnable() {
            @Override
            public void run() {
                while(true){
                    scan();
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(r2).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_data_analyis);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableDisableBT();
        enableDiscovery();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users");

        bluetoothList = new ArrayList<String>();




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


        startThread();
    }
    public void enableDiscovery(){
        System.out.println("btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
    }

    public void scan(){
        System.out.println("Scan Button clicked");
        bluetoothList.clear();
        discoverDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
//        unregisterReceiver(broadcastReceiver);
//        unregisterReceiver(mBroadcastReceiver2);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = ""+location.getLatitude();
        longitude = ""+location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public void discoverDevices(){
        bluetoothAdapter.startDiscovery();
        System.out.println("Bluetooth Discovery has started");
    }

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        System.out.println( "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        System.out.println("mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        System.out.println("mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        System.out.println("mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        System.out.println("mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received a request");

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                Date current_Date = new Date();
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int type = bluetoothDevice.getType();
                if(type==BluetoothDevice.DEVICE_TYPE_CLASSIC){
                    System.out.println("Hey, you have a classic BT");
                }
                Token currentToken = new Token();
                currentToken.setDATE(current_Date.toString());
                currentToken.setLONGITUDE(longitude);
                currentToken.setLATITUDE(latitude);
                currentToken.setMAC_ADDRESS(bluetoothDevice.getAddress().toUpperCase());

                DatabaseReference tokenRef = reference.child(user.getUid()).child("Token");
                tokenRef.push().setValue(currentToken);

                bluetoothList.add(bluetoothDevice.getName());
                System.out.println("A device was found!"+bluetoothDevice.getName());
            }
        }
    };

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
        if(bluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!bluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(bluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: Already Enabled BT.");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);

    }

}