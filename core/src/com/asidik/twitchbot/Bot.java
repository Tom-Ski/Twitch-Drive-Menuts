package com.asidik.twitchbot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Bot extends ApplicationAdapter {

    Stage stage;
    Skin skin;
    IRCClient client;
    InputManager inputManager;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        inputManager = new InputManager(this);
        client = new IRCClient(inputManager, "wilhelm.freenode.net", 6667, "letomsk");
        try {
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.addActor(inputManager.getDisplay().getContainer());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }
}
