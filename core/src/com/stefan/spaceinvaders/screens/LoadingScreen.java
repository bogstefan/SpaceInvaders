package com.stefan.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stefan.spaceinvaders.Main;

public class LoadingScreen implements Screen {

    private Main main;
    private static final long DISTANCE = 20;

    public LoadingScreen(Main main) {
        this.main = main;
        loadAssets();
    }

    private void loadAssets() {
        //textures
        main.assetManager.load("textures/texture1A.png", Texture.class);
        main.assetManager.load("textures/texture1B.png", Texture.class);
        main.assetManager.load("textures/texture2A.png", Texture.class);
        main.assetManager.load("textures/texture2B.png", Texture.class);
        main.assetManager.load("textures/texture3A.png", Texture.class);
        main.assetManager.load("textures/texture3B.png", Texture.class);
        main.assetManager.load("textures/texture4A.png", Texture.class);
        main.assetManager.load("textures/texture4B.png", Texture.class);
        main.assetManager.load("textures/texture5A.png", Texture.class);
        main.assetManager.load("textures/texture5B.png", Texture.class);
        main.assetManager.load("textures/texture6A.png", Texture.class);
        main.assetManager.load("textures/texture6B.png", Texture.class);
        main.assetManager.load("textures/texture7A.png", Texture.class);
        main.assetManager.load("textures/texture7B.png", Texture.class);
        main.assetManager.load("textures/texture8A.png", Texture.class);
        main.assetManager.load("textures/texture8B.png", Texture.class);
        main.assetManager.load("textures/texture9A.png", Texture.class);
        main.assetManager.load("textures/texture9B.png", Texture.class);
        main.assetManager.load("textures/spaceShipBulletTexture.png", Texture.class);
        main.assetManager.load("textures/spaceInvaderBoltTexture.png", Texture.class);
        main.assetManager.load("textures/spaceInvaderExplosionTexture.png", Texture.class);
        main.assetManager.load("textures/spaceShipTexture.png", Texture.class);
        main.assetManager.load("textures/spaceShipExplosionTexture.png", Texture.class);
        main.assetManager.load("textures/ufo.png", Texture.class);

        //sounds
        main.assetManager.load("sounds/spaceInvaderSound1.mp3", Sound.class);
        main.assetManager.load("sounds/spaceInvaderSound2.mp3", Sound.class);
        main.assetManager.load("sounds/spaceInvaderSound3.mp3", Sound.class);
        main.assetManager.load("sounds/spaceInvaderSound4.mp3", Sound.class);
        main.assetManager.load("sounds/spaceShipShootSound.mp3", Sound.class);
        main.assetManager.load("sounds/ufoSound.mp3", Sound.class);
        main.assetManager.load("sounds/spaceInvaderKilledSound.mp3", Sound.class);
        main.assetManager.load("sounds/spaceShipExplosionSound.mp3", Sound.class);

        //fonts
        main.assetManager.load("fonts/font1.fnt", BitmapFont.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float progress = main.assetManager.getProgress();
        main.renderer.begin(ShapeRenderer.ShapeType.Filled);
        main.renderer.rect(DISTANCE, Gdx.graphics.getHeight() / 2,
                (Gdx.graphics.getWidth() - 2 * DISTANCE) * progress, 20);
        main.renderer.end();

        if (main.assetManager.update()) {
            main.setScreen(new Level(main));
        }
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
