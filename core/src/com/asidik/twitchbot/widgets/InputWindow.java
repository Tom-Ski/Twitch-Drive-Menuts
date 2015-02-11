package com.asidik.twitchbot.widgets;

import com.asidik.twitchbot.InputType;
import com.asidik.twitchbot.UserInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;


public class InputWindow extends Window {

    Array<UserInput> inputBuffer = new Array<UserInput>();
    Array<UserInput> immaStealYoInputs = new Array<UserInput>();

    Image left;
    Image right;
    Image up;
    Image down;

    int maxBufferSize = 10;
    Skin skin;
    int count = 0;

    public InputWindow(String title, WindowStyle style, Skin skin) {
        super(title, style);
        this.skin = skin;
        refresh();
        top();
        defaults().fillX().expandX().height(30).pad(0, 20, 0, 20);

        Sprite arrowSprite = new Sprite(new Texture(Gdx.files.internal("Right.png")));
        arrowSprite.setOrigin(arrowSprite.getWidth()/2f, arrowSprite.getHeight()/2f);

        left = new Image(arrowSprite);
        right = new Image(arrowSprite);
        up = new Image(arrowSprite);
        down = new Image(arrowSprite);

        left.setOrigin(arrowSprite.getWidth()/2f, arrowSprite.getHeight()/2f);
        right.setOrigin(arrowSprite.getWidth()/2f, arrowSprite.getHeight()/2f);
        up.setOrigin(arrowSprite.getWidth() / 2f, arrowSprite.getHeight() / 2f);
        down.setOrigin(arrowSprite.getWidth() / 2f, arrowSprite.getHeight() / 2f);

        left.setRotation(180f);
        up.setRotation(90f);
        down.setRotation(-90f);
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

    public void addVote(UserInput userInput) {
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
            Table labelContainer = new Table();
            Label label = new Label(message.userName, skin);
            labelContainer.add(label).expandX();
            labelContainer.add(getArrow(message.input));
            add(labelContainer);
            row();
        }
    }


    private Image getArrow (InputType type) {
        switch (type) {
            case UP:
                return new Image(up.getDrawable());
            case DOWN:
                return new Image(down.getDrawable());
            case RIGHT:
                return new Image(right.getDrawable());
            case LEFT:
                return new Image(left.getDrawable());
        }
        return null;
    }


}
