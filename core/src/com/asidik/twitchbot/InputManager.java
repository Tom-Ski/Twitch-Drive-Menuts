package com.asidik.twitchbot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tom on 05/02/2015.
 */
public class InputManager extends Thread {

    private Bot bot;
    private ConcurrentHashMap<InputType, Integer> votes;
    private InputDisplay display;

    private static final long COUNT_TIMER = 5000L;
    private static long timeLastCount = 0L;
    private static long trackTime = 0L;
    private static long delta = 0L;

    public InputManager (Bot bot) {
        super("InputManager");
        this.bot = bot;
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
        }
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

            System.out.println(trackTime);

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
