package com.stefan.spaceinvaders.aliens;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.projectiles.SpaceInvaderBolt;
import com.stefan.spaceinvaders.projectiles.SpaceShipBullet;
import com.stefan.spaceinvaders.screens.Level;
import com.stefan.spaceinvaders.util.GlobalLocalPosition;

public class SpaceInvader extends Actor implements GlobalLocalPosition {

    private Vector2 globalPosition;
    private Vector2 localPosition;
    private Main main;
    private Level level;
    private Sprite spaceInvaderSprite;
    private static final long SHOOTING_SPEED = 1000;
    private long lastTimeShot;
    public static final float WIDTH = 5;
    public static final float HEIGHT = 5;
    private Sound spaceInvaderKilledSound;
    private Texture spaceInvaderExplosionTexture;
    private Texture textureA;
    private Texture textureB;

    public SpaceInvader(Main main, Level level, SI spaceInvader, float x, float y) {
        this.main = main;
        this.level = level;
        globalPosition = new Vector2(x, y);
        localPosition = new Vector2(x, y);

        spaceInvaderSprite = new Sprite();
        spaceInvaderExplosionTexture = main.assetManager.get("textures/spaceInvaderExplosionTexture.png");
        spaceInvaderKilledSound = main.assetManager.get("sounds/spaceInvaderKilledSound.mp3");
        lastTimeShot = System.currentTimeMillis();
        initTextures(spaceInvader);
        spaceInvaderSprite.setRegion(textureA);
        spaceInvaderSprite.setPosition(globalPosition.x, globalPosition.y);
        spaceInvaderSprite.setSize(WIDTH, HEIGHT);

        spaceInvaderSprite.setColor((float) Math.random());
        spaceInvaderSprite.setAlpha(1);
        setBounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void act(float delta) {
        setGlobalLocalPosition();
        for (Actor actor : level.spaceShipBulletGroup.getChildren()) {
            SpaceShipBullet spaceShipBullet = (SpaceShipBullet) actor;
            if (spaceInvaderSprite.getBoundingRectangle().overlaps(spaceShipBullet.getBoundingRectangle())) {
                level.score += 5;
                level.hud.update();
                actor.remove();
                explode();
                break;
            }
        }
        shoot();
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switchToLocalPosition();
        spaceInvaderSprite.draw(batch);
        super.draw(batch, parentAlpha);
        switchToGlobalPosition();
    }

    @Override
    public void setGlobalLocalPosition() {
        float localX = getX();
        float localY = getY();
        localPosition.set(localX, localY);
        globalPosition.set(localX, localY);
        level.spaceInvaderGroup.localToStageCoordinates(globalPosition);
    }

    @Override
    public void switchToGlobalPosition() {
        spaceInvaderSprite.setPosition(globalPosition.x, globalPosition.y);
    }

    @Override
    public void switchToLocalPosition() {
        spaceInvaderSprite.setPosition(localPosition.x, localPosition.y);
    }

    @Override
    public void setColor(Color color) {
        spaceInvaderSprite.setColor(color);
        super.setColor(color);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        spaceInvaderSprite.setColor(r, g, b, a);
        super.setColor(r, g, b, a);
    }


    private void initTextures(SI spaceInvader) {
        switch (spaceInvader) {
            case S1:
                textureA = main.assetManager.get("textures/texture1A.png");
                textureB = main.assetManager.get("textures/texture1B.png");

                break;
            case S2:
                textureA = main.assetManager.get("textures/texture2A.png");
                textureB = main.assetManager.get("textures/texture2B.png");
                break;
            case S3:
                textureA = main.assetManager.get("textures/texture3A.png");
                textureB = main.assetManager.get("textures/texture3B.png");
                break;
            case S4:
                textureA = main.assetManager.get("textures/texture4A.png");
                textureB = main.assetManager.get("textures/texture4B.png");
                break;
            case S5:
                textureA = main.assetManager.get("textures/texture5A.png");
                textureB = main.assetManager.get("textures/texture5B.png");
                break;
            case S6:
                textureA = main.assetManager.get("textures/texture6A.png");
                textureB = main.assetManager.get("textures/texture6B.png");
                break;
            case S7:
                textureA = main.assetManager.get("textures/texture7A.png");
                textureB = main.assetManager.get("textures/texture7B.png");
                break;
            case S8:
                textureA = main.assetManager.get("textures/texture8A.png");
                textureB = main.assetManager.get("textures/texture8B.png");
                break;
            case S9:
                textureA = main.assetManager.get("textures/texture9A.png");
                textureB = main.assetManager.get("textures/texture9B.png");
                break;
            default:
                textureA = main.assetManager.get("textures/texture1A.png");
                textureB = main.assetManager.get("textures/texture1B.png");
        }
    }

    public void changeState() {
        if (spaceInvaderSprite.getTexture() == textureA) {
            spaceInvaderSprite.setRegion(textureB);
        } else if (spaceInvaderSprite.getTexture() == textureB) {
            spaceInvaderSprite.setRegion(textureA);
        }
    }

    private void explode() {
        spaceInvaderSprite.setTexture(spaceInvaderExplosionTexture);
        spaceInvaderKilledSound.play(0.1F);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                remove();
            }
        }, 0.05F);
    }

    private void shoot() {
        if (System.currentTimeMillis() - lastTimeShot > SHOOTING_SPEED
                && level.spaceInvaderBoltGroup.getChildren().size < 5
                && main.random.nextInt(100) > 80) {
            SpaceInvaderBolt spaceInvaderBolt = new SpaceInvaderBolt(main, level,
                    globalPosition.x + WIDTH / 2, globalPosition.y + HEIGHT);
            level.spaceInvaderBoltGroup.addActor(spaceInvaderBolt);
            lastTimeShot = System.currentTimeMillis();
        }
    }

}