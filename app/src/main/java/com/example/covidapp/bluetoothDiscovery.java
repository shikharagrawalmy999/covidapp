package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class bluetoothDiscovery extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    BluetoothAdapter bluetoothAdapter;
    ListView bluetoothListView;
    ArrayAdapter<String> bluetoothAdp;

    ArrayList<String> bluetoothList;
    ArrayList<BluetoothDevice> mDeviceList;
    Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);
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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothListView = (ListView) findViewById(R.id.bluetoothDevices);
        bluetoothList = new ArrayList<String>();
        mDeviceList = new ArrayList<BluetoothDevice>();
        bluetoothAdp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bluetoothList);
        bluetoothListView.setAdapter(bluetoothAdp);

        scanBtn = findViewById(R.id.scanButton);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Scan Button clicked");
                bluetoothList.clear();
                discoverDevices(v);
            }
        });
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

    public void discoverDevices(View v){
        bluetoothAdapter.startDiscovery();
        System.out.println("Bluetooth Discovery has started");
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received a request");

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                Date current_Date = new Date();
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Token currentToken = new Token();
                currentToken.setDATE(current_Date.toString());
                currentToken.setLONGITUDE(longitude);
                currentToken.setLATITUDE(latitude);
                currentToken.setMAC_ADDRESS(bluetoothDevice.getAddress());

                reference.child(user.getUid()).child("Token").setValue(currentToken);
//                System.out.println("The latitude is: "+currentToken.LATITUDE);
//                System.out.println("The longitude is: "+currentToken.LONGITUDE);
//                System.out.println("The Date is: "+currentToken.DATE);
//                System.out.println("The MAC Address is: "+currentToken.MAC_ADDRESS);

                bluetoothList.add(bluetoothDevice.getName());
                mDeviceList.add(bluetoothDevice);
                bluetoothAdp.notifyDataSetChanged();
                System.out.println("A device was found!"+bluetoothDevice.getName());
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);

    }
}