package com.example.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class bluetoothDiscovery extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    ListView bluetoothListView;
    ArrayAdapter<String> bluetoothAdp;

    ArrayList bluetoothList;

    Button scanBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothListView = (ListView) findViewById(R.id.bluetoothDevices);
        bluetoothList = new ArrayList();

        bluetoothAdp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, bluetoothList);
        bluetoothListView.setAdapter(bluetoothAdp);

        scanBtn = findViewById(R.id.scanButton);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverDevices(v);
            }
        });

    }

    public void discoverDevices(View v)
    {
        bluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothList.add(bluetoothDevice.getName());
                bluetoothAdp.notifyDataSetChanged();
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