package com.asidik.twitchbot;

import java.io.*;
import java.net.Socket;

/**
 * Created by Javier on 03/02/2015.
 */
public class IRCClient extends Thread {

    private IRCParser parser = new IRCParser();

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private String server;
    private int port;
    private String nick;

    public IRCClient(String server, int port, String nick) {
        this.server = server;
        this.port = port;
        this.nick = nick;
    }

    public void connect() throws Exception {
        socket = new Socket(server, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader( socket.getInputStream()));

        // Log on
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + nick + " 8 * : Twitch Drives Menuts\r\n");
        writer.flush( );

        String line = null;
        while ((line = reader.readLine( )) != null) {
            System.out.println(line);
            if (line.indexOf("004") >= 0) {
                // We are now logged in.
                break;
            }
            else if (line.indexOf("433") >= 0) {
                System.out.println("Nicknamelready in use.");
                return;
            }
        }
        System.out.println("conn");
        // Join the channel.
        writer.write("JOIN #drivemenuts \r\n");
        writer.flush( );

        start();
    }

    @Override
    public void run() {
        // Keep reading lines from the server.
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("PING ")) {
                    try {
                        // We must respond to PINGs to avoid being disconnected.
                        writer.write("PONG " + line.substring(5) + "\r\n");
                        writer.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (parser.parse(line)) {
                        writer.write("PRIVMSG #drivemenuts :" + parser.getVotes().toString() + "\r\n");
                        writer.flush();
                    }
                }
                Thread.sleep(20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
