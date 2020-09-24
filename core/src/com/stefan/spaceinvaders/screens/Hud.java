package com.stefan.spaceinvaders.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.stefan.spaceinvaders.Main;

import java.util.Locale;

public class Hud {

    public Stage hudStage;
    private Level level;
    private Touchpad touchpad;
    Label score;
    Label lives;
    Table table;

    Hud(Main main, Level level) {
        this.level = level;
        main.font.getData().setScale(0.15F);
        score = new Label(String.format(Locale.US, "Score: %d", level.score),
                new Label.LabelStyle(main.font, Color.WHITE));
        lives = new Label(String.format(Locale.US, "Lives: %d", level.lives),
                new Label.LabelStyle(main.font, Color.WHITE));

        hudStage = new Stage(new FitViewport(Level.WORLD_WIDTH * 1, Level.WORLD_HEIGHT), main.batch);
        Skin touchPadSkin = new Skin();
        touchPadSkin.add("touchBackground", new Texture("joystick/touchBackground.png"));
        touchPadSkin.add("touchKnob", new Texture("joystick/touchKnob.png"));
        Drawable touchPadBackground = touchPadSkin.getDrawable("touchBackground");
        Drawable touchPadKnob = touchPadSkin.getDrawable("touchKnob");
        touchPadBackground.setMinHeight(20);
        touchPadBackground.setMinWidth(20);
        touchPadKnob.setMinHeight(10);
        touchPadKnob.setMinWidth(10);

        TouchpadStyle touchpadStyle = new TouchpadStyle(touchPadBackground, touchPadKnob);
        touchpad = new Touchpad(1, touchpadStyle);
        //touchpad.setBounds(0, 0, 20, 20);
        table = new Table();
        //table.setFillParent(true);
        table.setBounds(0, 0, hudStage.getViewport().getWorldWidth(), hudStage.getViewport().getWorldHeight());
        table.top();
        table.add(score).expandX().padTop(10);
        table.add(lives).expandX().padTop(10);
        //table.addActorAt(10, touchpad);
        hudStage.addActor(table);
    }

    public void update() {
        score.setText(String.format(Locale.US, "Score: %d", level.score));
        lives.setText(String.format(Locale.US, "Lives: %d", level.lives));
    }

}
