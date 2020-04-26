package com.example.pinballremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RemoteControllerActivity extends AppCompatActivity {

    private TextView tvError;
    private ConstraintLayout clView;
    private EditText etPoints;
    private TextView tvScoreboard;
    private Button bStart;
    private Button bReset;


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
    }


    void onClickStart(View v){

    }

    void onClickReset(View v){

    }
}
