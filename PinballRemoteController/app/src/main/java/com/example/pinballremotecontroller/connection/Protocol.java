package com.example.pinballremotecontroller.connection;

import android.util.Log;

public class Protocol {
    public static final int START = 0;
    public static final int END = 1;
    public static final int SCORE_UP = 2;

    public static final byte START_VAL = 0; //XXYY:0000 -> XX = 00, YY = 00
    public static final byte END_VAL = (0x1 << 6); //XXYY:0000 -> XX = 01, YY = 00
    public static final byte SCORE_UP_VAL = -128; //XXYY:0000 -> XX = 10, YY = 00

    public static final byte PARAM_P1 = 0;
    public static final byte PARAM_P2 = 16;

    public static byte codifyMsg(int msgType){
        byte msg;
        switch (msgType){
            case START:
                msg = START_VAL;
                break;
            case END:
                msg = END_VAL;
                break;
            case SCORE_UP:
                msg = SCORE_UP_VAL;
                break;
            default:
                msg = -1;
                break;
        }
        Log.d("Protocol encod", "" + msg);
        return msg;
    }

    public static byte decodMsg(byte msg){
        Log.d("Protocol decod", "" + msg);
        byte val = -1;
        byte mask = -64; // 1100:0000
        byte msg_type = (byte) (msg & mask);

        if(msg_type == SCORE_UP_VAL)
            val = (byte) (msg & (0x1 << 4));

        return val;
    }
}
