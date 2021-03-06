package com.example.pinballremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pinballremotecontroller.bluetoothList.BluetoothItem;
import com.example.pinballremotecontroller.bluetoothList.MyBluetoothAdapter;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_ENABLE_BT = 1;

    private TextView tvError;
    private RelativeLayout rlView;
    private RecyclerView rvBluetooth;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = findViewById(R.id.tvError);
        rlView = findViewById(R.id.rlView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            showMessage(R.string.no_bluetooth);
            Log.e("MainActivity", "Bluetooth not supported");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }
        updateList();
    }

    private void showMessage(final int error){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rlView.setVisibility(View.GONE);
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateList(){
        final Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() == 0) {
            Log.d("MainActivity", "No Bluetooth devices paired.");
            showMessage(R.string.no_paired_devices);
            return;
        }

        rvBluetooth = findViewById(R.id.rvBluetooth);
        final MainActivity context = this;
        // show paired devices list
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rvBluetooth.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvBluetooth.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                        layoutManager.getOrientation());
                rvBluetooth.addItemDecoration(dividerItemDecoration);

                ArrayList<BluetoothItem> alBlu = new ArrayList<>();
                for(BluetoothDevice d : pairedDevices)
                    alBlu.add(new BluetoothItem(d));

                rvBluetooth.setAdapter(new MyBluetoothAdapter(alBlu, context));
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            showMessage(R.string.bluetooth_denied);
            Log.e("MainActivity", "Bluetooth inactivated");
            return;
        }

        Log.d("MainActivity", "Bluetooth activated");
        updateList();
    }

    public void onClickItem(BluetoothDevice device){
        Intent intent = new Intent(getApplicationContext(), RemoteControllerActivity.class);
        intent.putExtra("mac", device.getAddress());
        startActivity(intent);
        showMessage(R.string.connecting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(rvBluetooth != null){
                    rlView.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                }
            }
        });
    }
}
