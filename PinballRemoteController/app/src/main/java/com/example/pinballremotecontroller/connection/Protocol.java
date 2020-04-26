package com.example.pinballremotecontroller.connection;

public class Protocol {
    public static final int START = 0;
    public static final int END = 1;
    public static final int SCORE_UP = 2;
    public static final int ACK = 3;

    public static byte codifyMsg(int msgType){
        byte msg = 0;
        switch (msgType){
            case START:
                //0000XXYY -> XX = 00, YY = 00
                msg = 0;
                break;
            case END:
                //0000XXYY -> XX = 01, YY = 00
                msg |= (0x1 << 2);
                break;
            case SCORE_UP:
                //0000XXYY -> XX = 10, YY = 00
                msg |= (0x1 << 3);
                break;
            case ACK:
                //0000XXYY -> XX = 11, YY = 00
                msg |= (0x3 << 2);
                break;
            default:
                msg = -1;
                break;
        }
        return msg;
    }

    public static byte decodMsg(byte msg){
        byte val = -1;
        byte mask = 0x3 << 2;
        byte type = (byte) (msg & mask);

        // SCORE_UP msg
        if(type == 8)
            val = (byte) (msg & (0x2));

        return val;
    }
}
