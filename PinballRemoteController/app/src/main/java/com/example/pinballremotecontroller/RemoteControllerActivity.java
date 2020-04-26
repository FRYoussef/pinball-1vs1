package com.example.pinballremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pinballremotecontroller.connection.Connection;
import com.example.pinballremotecontroller.connection.ConnectionListener;
import com.example.pinballremotecontroller.connection.Protocol;

import java.util.Set;

public class RemoteControllerActivity extends AppCompatActivity implements Observer{
    private static final int PLAYER_1 = 0;
    private static final int PLAYER_2 = 1;

    private TextView tvError;
    private ConstraintLayout clView;
    private EditText etPoints;
    private TextView tvScoreboard;
    private Button bStart;
    private Button bReset;
    private Connection connection;
    private int playerScore1;
    private int playerScore2;
    private int maxPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_controller);

        tvError = findViewById(R.id.tvError);
        clView = findViewById(R.id.clView);
        etPoints = findViewById(R.id.etPoints);
        tvScoreboard = findViewById(R.id.tvScoreboard);
        bStart = findViewById(R.id.bStart);
        bReset = findViewById(R.id.bReset);
        resetValues();

        // attach events
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onClickStart(v); }
        });
        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onClickReset(v); }
        });

        String mac = getIntent().getStringExtra("mac");
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        BluetoothDevice device = null;
        for(BluetoothDevice d : pairedDevices)
            if(d.getAddress().equals(mac))
                device = d;

        connection = new Connection(device);
        if(!connection.isConnected()){
            showError(R.string.connection_error);
            return;
        }

        // socket listener
        Thread th = new ConnectionListener(connection, this);
        th.setDaemon(true);
        th.start();
    }


    void onClickStart(View v){
        connection.write(new byte[Protocol.codifyMsg(Protocol.START)]);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                maxPoints = Integer.parseInt(etPoints.getText().toString());
                bStart.setEnabled(false);
                etPoints.setEnabled(false);
                tvScoreboard.setText("Player 1 | Player 2\n\n"
                        + playerScore1 + " : " + playerScore2);
            }
        });
    }

    void onClickReset(View v){
        connection.write(new byte[Protocol.codifyMsg(Protocol.END)]);
        resetValues();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvScoreboard.setText(R.string.default_scoreboard);
            }
        });

    }

    private void showError(final int error){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clView.setVisibility(View.GONE);
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            }
        });

        //close connection
        connection.shutDown();
    }

    @Override
    public void notify(int param) {
        if(param == PLAYER_1)
            playerScore2++;
        else if(param == PLAYER_2)
            playerScore2++;

        if(playerScore1 >= maxPoints || playerScore2 >= maxPoints){
            connection.write(new byte[Protocol.codifyMsg(Protocol.END)]);
            resetValues();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(playerScore1 > playerScore2)
                        tvScoreboard.setText(R.string.player1_wins);
                    else
                        tvScoreboard.setText(R.string.player2_wins);
                }
            });
        }
    }

    @Override
    public void errorReading() {
        showError(R.string.connection_error);
    }

    private void resetValues(){
        playerScore1 = 0;
        playerScore2 = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bStart.setEnabled(true);
                etPoints.setEnabled(true);
            }
        });
    }
}
