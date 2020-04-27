package com.example.pinballremotecontroller.bluetoothList;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BluetoothViewHolder extends RecyclerView.ViewHolder {

    public TextView tv;

    public BluetoothViewHolder(@NonNull View itemView) {
        super(itemView);
        tv = (TextView) itemView;
    }
}
