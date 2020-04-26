package com.example.pinballremotecontroller.connection;

import com.example.pinballremotecontroller.Observer;

public class ConnectionListener extends Thread{
    private Connection connection;
    private Observer observer;

    public ConnectionListener(Connection connection, Observer observer){
        this.connection = connection;
        this.observer = observer;
    }

    @Override
    public void run() {
        while(true){
            if(connection.read() == 1){
                observer.errorReading();
                break;
            }

            int result = Protocol.decodMsg(connection.getFirstByte());
            if(result != -1){
                //confirm reception
                connection.write(new byte[Protocol.codifyMsg(Protocol.ACK)]);
                observer.notify(result);
            }
        }
    }
}
