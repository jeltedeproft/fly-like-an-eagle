package com.jeltedeproft.flylikeaneagle.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jeltedeproft.flylikeaneagle.FlyLikeAnEagle;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Fly Like an Eagle");
        config.setWindowedMode(1280, 720);
        config.useVsync(true);
        config.setForegroundFPS(144);
        new Lwjgl3Application(new FlyLikeAnEagle(), config);
    }
}
