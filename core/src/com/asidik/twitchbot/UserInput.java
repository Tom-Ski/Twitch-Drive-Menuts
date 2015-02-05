package com.asidik.twitchbot;

/**
 * Created by Tom on 05/02/2015.
 */
public class UserInput {

    public String userName;
    public InputType input;
    public boolean dead;

    float trackTime;
    float messageTimeout = 10f;

    public UserInput (String userName, InputType type) {
        this.userName = userName;
        this.input = type;
    }

    public void update (float delta) {
        trackTime += delta;
        if (trackTime > messageTimeout) dead = true;
    }



}
