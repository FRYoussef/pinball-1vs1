package com.example.pinballremotecontroller.bluetoothList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinballremotecontroller.MainActivity;
import com.example.pinballremotecontroller.R;

import java.util.ArrayList;

public class MyBluetoothAdapter extends RecyclerView.Adapter<BluetoothViewHolder> {
    private ArrayList<BluetoothItem> alItems;
    private MainActivity context;

    public MyBluetoothAdapter(ArrayList<BluetoothItem> alItems, MainActivity context) {
        this.alItems = alItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_item, parent, false);

        return new BluetoothViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothViewHolder holder, int position) {
        final BluetoothItem item = alItems.get(position);
        holder.tv.setText(item.getName());
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onClickItem(item.getDevice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return alItems.size();
    }
}
