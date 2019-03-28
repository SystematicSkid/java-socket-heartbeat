package com.aimflex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat extends Thread {
    private Socket _socket = null;
    private DataOutputStream _output = null;
    private DataInputStream _input = null;
    private float _lastReceived = 0.0f;
    private float _nextHeartbeat = 0.0f;
    private boolean _isAlive = true;
    private Timer _timer = null;

    static final int MINUTE = 5 * 1000; // 1 second in ms * 60

    public void Receive()
    {
        if(_input != null) {
            try {
                String in = "";
                while("reply".equals(in)) {
                    in = this._input.readUTF();
                    System.out.println(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TimerTask Send()
    {
        if(_output != null)
        {
            try {
                System.out.print("Sending heartbeat\n");
                _output.writeUTF("heartbeat");
                Receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void run()
    {
        try {
            _timer = new Timer();
            TimerTask task = Send();
            _timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(_output != null)
                    {
                        try {
                            System.out.print("Sending heartbeat\n");
                            _output.writeUTF("heartbeat");
                            Receive();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 10*1000, 5*1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Heartbeat(Socket socket, DataOutputStream output, DataInputStream input)
    {
        _socket = socket;
        _output = output;
        _input = input;


        _nextHeartbeat = System.currentTimeMillis() + MINUTE;

    }

    public boolean IsAlive() {
        return _isAlive;
    }
}
