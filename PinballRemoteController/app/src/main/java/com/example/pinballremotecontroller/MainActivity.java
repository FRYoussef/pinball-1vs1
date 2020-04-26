package com.example.pinballremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_ENABLE_BT = 1;

    private TextView tvError;
    private RelativeLayout rlView;
    private RecyclerView rvBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = findViewById(R.id.tvError);
        rlView = findViewById(R.id.rlView);
        rvBluetooth = findViewById(R.id.rvBluetooth);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            showError();
            Log.e("MainActivity", "Bluetooth not supported");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rlView.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            showError();
            Log.e("MainActivity", "Bluetooth inactivated");
            return;
        }
        // TODO: load data on the recycler view
        Log.d("MainActivity", "Bluetooth activated");
    }
}
