package com.stefan.spaceinvaders.aliens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.stefan.spaceinvaders.screens.Level;

public class Ufo extends Sprite {

    private static final float WIDTH = 10;
    private static final float HEIGHT = 5;
    public static final float SPEED = 10;
    private Sound ufoSound;

    public Ufo() {
        setRegion(new Texture("textures/UFO.png"));
        setPosition(Level.WORLD_WIDTH - WIDTH, Level.WORLD_HEIGHT - HEIGHT);
        setSize(WIDTH, HEIGHT);
        ufoSound = Gdx.audio.newSound(Gdx.files.internal("sounds/UFO.mp3"));
    }

    public void dispose() {
        getTexture().dispose();
    }

    public void playSound() {
        ufoSound.loop(0.1F);
    }
}
