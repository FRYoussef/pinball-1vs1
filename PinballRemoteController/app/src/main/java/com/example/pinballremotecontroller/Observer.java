package com.example.pinballremotecontroller;

public interface Observer {
    void notify(int param);
    void errorReading();
}
