package com.example.pinballremotecontroller.bluetoothList;

import android.bluetooth.BluetoothDevice;

public class BluetoothItem {
    private BluetoothDevice device;

    public BluetoothItem(BluetoothDevice device) {
        this.device = device;
    }

    public String getName() {
        return device.getName();
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
