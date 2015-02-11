package com.asidik.twitchbot.android;


import android.util.Log;
import com.asidik.twitchbot.InputType;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Tom on 10/02/2015.
 */
public class SocketConnection implements Runnable {

    NetworkService service;
    PrintWriter out;

    public SocketConnection(NetworkService service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.setServerAddress(InetAddress.getByName(service.SERVER));
            Log.d(service.TAG, "Connecting");

            service.socket = new Socket(service.getServerAddress(), service.port);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(service.socket.getOutputStream())), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(service.socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                InputType type = InputType.valueOf(inputLine);
                if (type != null) {
                    service.sendBluetoothInstruction(type);
                }
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
