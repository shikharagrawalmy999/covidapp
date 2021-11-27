package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class bluetoothDiscovery extends AppCompatActivity implements LocationListener {
    private static final String TAG = "bluetoothDiscovery";
    protected LocationManager locationManager;
    protected Context context;
    protected String latitude, longitude;

    private static final long SCAN_PERIOD = 10000;
    Button enableDiscovery;

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
        enableDiscovery = findViewById(R.id.enableDiscovery);

        enableDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(mBroadcastReceiver2,intentFilter);
            }
        });

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

    public void discoverDevices(View v){
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