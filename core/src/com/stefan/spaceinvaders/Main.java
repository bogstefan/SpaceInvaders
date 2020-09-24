package com.stefan.spaceinvaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stefan.spaceinvaders.screens.LoadingScreen;

import java.util.Random;

public class Main extends Game {

    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;
    public GlyphLayout layout;
    public AssetManager assetManager;
    public Random random;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
        layout = new GlyphLayout();
        random = new Random();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        assetManager.dispose();
        font.dispose();
    }

}
