package com.asidik.twitchbot;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tom on 05/02/2015.
 */
public class InputManager extends Thread {

    private Bot bot;
    private ConcurrentHashMap<InputType, Integer> votes;
    private InputDisplay display;

    private ServerSocket serverSocket;
    private PrintWriter writer;
    private boolean menutsConnected;

    private static final long COUNT_TIMER = 5000L;
    private static long timeLastCount = 0L;
    private static long trackTime = 0L;
    private static long delta = 0L;

    public InputManager (Bot bot) {
        super("InputManager");
        this.bot = bot;
        try {
            serverSocket = new ServerSocket(5000);
            Socket clientSocket = serverSocket.accept();
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            menutsConnected = true;
            System.out.println("Connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        votes = new ConcurrentHashMap<InputType, Integer>();
        for (InputType type : InputType.values()) {
            votes.put(type, 0);
        }
        display = new InputDisplay(this, bot.skin);

        timeLastCount = System.currentTimeMillis();

        start();
    }

    public void refreshVotes () {
        clearVotes();
    }

    private void countVotes() {
        InputType mostVoted = null;
        Integer voteNumber = 0;
        for (Map.Entry<InputType, Integer> vote : votes.entrySet()) {
            if (voteNumber < vote.getValue()) {
                voteNumber = vote.getValue();
                mostVoted = vote.getKey();
            }
        }
        if (mostVoted != null) {
            bot.client.sendMessage("drivemenuts", "Most voted: " + mostVoted.toString());
            if (menutsConnected) {
                sendInputPacket(mostVoted);
            }
        }
    }

    private void sendInputPacket(InputType mostVoted) {
        writer.println(mostVoted.name());
        writer.flush();
    }

    private void clearVotes() {
        for (InputType type : InputType.values()) {
            votes.put(type, 0);
        }
    }

    public void registerVote (UserInput userInput) {
        votes.put(userInput.input, votes.get(userInput.input) + 1);
        display.addNewvote(userInput);
    }

    public InputDisplay getDisplay() {
        return display;
    }

    @Override
    public void run() {
        while (true) {
            delta = System.currentTimeMillis() - timeLastCount;
            trackTime += delta;

            if (trackTime > COUNT_TIMER) {
                countVotes();
                refreshVotes();
                trackTime = 0;
            }

            timeLastCount = System.currentTimeMillis();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
