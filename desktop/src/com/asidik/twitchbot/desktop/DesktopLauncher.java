package com.asidik.twitchbot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.asidik.twitchbot.Bot;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Menuts";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Bot(), config);
    }
}
