package com.asidik.twitchbot;

import com.asidik.twitchbot.widgets.InputWindow;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by Tom on 05/02/2015.
 */
public class InputDisplay {

    InputManager manager;
    Table container;
    Skin skin;

    InputWindow inputWindow;

    public InputDisplay(InputManager inputManager, Skin skin) {
        this.manager = inputManager;
        this.skin = skin;
        container = new Table();
        container.setFillParent(true);
        inputWindow = new InputWindow("Latest Input", skin.get(Window.WindowStyle.class), skin);
        container.right();
        container.add(inputWindow).size(1280/5f, 720f * 0.8f);
    }

    public void addNewvote(UserInput userInput) {
        inputWindow.addVote(userInput);
    }

    public Table getContainer () {
        return container;
    }


}
