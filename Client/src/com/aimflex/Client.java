package com.aimflex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private Socket _socket;
    private String _ip;
    private int _port;
    private DataInputStream _input;
    private DataInputStream _serverinput;
    private DataOutputStream _output;

    public Client(String ip, int port) {
        _ip = ip;
        _port = port;
        try {
            _socket = new Socket(_ip, _port);
            System.out.print("Connected to server.\n");
            _input = new DataInputStream(System.in);
            _serverinput = new DataInputStream(_socket.getInputStream());
            _output = new DataOutputStream(_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String in = "";

        while (!in.equals("end")) {
            try {
                if (_serverinput.readUTF().equals("heartbeat")) {
                    System.out.print("Received heartbeat\n");
                    _output.writeUTF("reply");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            _input.close();
            _output.close();
            _socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}
