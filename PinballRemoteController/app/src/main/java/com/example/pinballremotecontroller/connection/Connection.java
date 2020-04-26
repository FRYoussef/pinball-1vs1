package com.example.pinballremotecontroller.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Connection {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private final BluetoothSocket socket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer;
    private int numBytes;

    public Connection(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            tmp.connect();
            tmpIn = tmp.getInputStream();
            tmpOut = tmp.getOutputStream();

        } catch (IOException e) {
            Log.e("Conection", "Socket's create() method failed", e);
        }
        socket = tmp;
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public int write(byte[] bytes){
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public int read(){
        mmBuffer = new byte[4];
        try {
            numBytes = mmInStream.read(mmBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public int shutDown(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public byte getFirstByte(){
        return (numBytes > 0) ? mmBuffer[0] : -1;
    }

    public boolean isConnected(){
        return socket.isConnected();
    }
}
