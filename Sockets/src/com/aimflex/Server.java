package com.aimflex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class Server {
    public static final String SERVER_RECEIVED_MESSAGE_S = "[Server] Received message %s\n";
    private Socket _socket;
    private ServerSocket _server;
    private final int _port;
    private DataInputStream _input;
    private DataOutputStream _output;
    private Timer _timer;

    boolean SendHeartbeat()
    {
        return true;
    }

    void ReceiveData() throws IOException {
        String in = "";
        while(!"end".equals(in))
        {
            in = this._input.readUTF();
            System.out.printf(SERVER_RECEIVED_MESSAGE_S, in);
        }
        System.out.print("[Server] Connection Closed\n");
        this._socket.close();
        this._input.close();
    }



    public Server(int port) throws IOException {
        this._port = port;
            this._server = new ServerSocket(port);
            System.out.print("[Server] Waiting for client...\n");
            this._socket = this._server.accept();
            this._input = new DataInputStream(new BufferedInputStream(this._socket.getInputStream()));
            this._output = new DataOutputStream(this._socket.getOutputStream());
            Heartbeat heartbeat = new Heartbeat(_socket, _output, _input);
            heartbeat.start();
            ReceiveData();
    }

    @Override
    public String toString() {
        return "Server{" +
                "_socket=" + _socket +
                ", _server=" + _server +
                ", _port=" + _port +
                ", _input=" + _input +
                ", _output=" + _output +
                '}';
    }
}
