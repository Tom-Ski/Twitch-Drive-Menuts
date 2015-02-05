package com.asidik.twitchbot.widgets;

import com.asidik.twitchbot.UserInput;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;


public class InputWindow extends Window {

    Array<UserInput> inputBuffer = new Array<UserInput>();
    Array<UserInput> immaStealYoInputs = new Array<UserInput>();

    int maxBufferSize = 10;
    Skin skin;
    int count = 0;

    public InputWindow (String title, WindowStyle style, Skin skin) {
        super(title, style);
        this.skin = skin;
        refresh();
        top();
        defaults().fillX().expandX().height(30);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (UserInput input : inputBuffer) {
            input.update(delta);
            if (input.dead) {
                immaStealYoInputs.add(input);
            }
        }
        count = 0;
        for (UserInput message : immaStealYoInputs) {
            inputBuffer.removeValue(message, false);
            count++;
        }
        immaStealYoInputs.clear();
        if (count > 0) {
            refresh();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void addVote (UserInput userInput) {
        if (inputBuffer.size > maxBufferSize) {
            inputBuffer.removeValue(inputBuffer.first(), false);
        }
        inputBuffer.add(userInput);
        refresh();
    }

    private void refresh() {
        getChildren().clear();
        getCells().clear();
        for (UserInput message : inputBuffer) {
            Label label = new Label(message.userName, skin);
            add(label);
            row();
        }

    }


}
