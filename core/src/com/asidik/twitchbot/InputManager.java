package com.asidik.twitchbot;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tom on 05/02/2015.
 */
public class InputManager {

    private Bot bot;
    private ConcurrentHashMap<InputType, Integer> votes;
    private InputDisplay display;

    public InputManager (Bot bot) {
        this.bot = bot;
        votes = new ConcurrentHashMap<InputType, Integer>();
        for (InputType type : InputType.values()) {
            votes.put(type, 0);
        }
        display = new InputDisplay(this, bot.skin);
    }

    public void refreshVotes () {

    }

    public void registerVote (UserInput userInput) {
        votes.put(userInput.input, votes.get(userInput.input) + 1);
        display.addNewvote(userInput);
    }

    public InputDisplay getDisplay() {
        return display;
    }
}
