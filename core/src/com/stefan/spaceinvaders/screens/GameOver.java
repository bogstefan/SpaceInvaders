package com.stefan.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.stefan.spaceinvaders.Main;

import java.util.Locale;

class GameOver implements Screen {

    private Main main;

    GameOver(Main main, int score) {
        this.main = main;
        main.layout.setText(main.font, String.format(Locale.US, "Game Over!\n You reached %d points.\n" +
                "Press \"S\" to restart!", score), Color.WHITE, Level.WORLD_WIDTH, Align.center, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            main.setScreen(new Level(main));
        }

        main.batch.begin();
        main.font.draw(main.batch, main.layout, 0, Level.WORLD_HEIGHT / 2);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
