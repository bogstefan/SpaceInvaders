package com.stefan.spaceinvaders.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.projectiles.SpaceInvaderBolt;
import com.stefan.spaceinvaders.projectiles.SpaceShipBullet;
import com.stefan.spaceinvaders.screens.Level;

public class SpaceShip extends Actor {

    private static final float SPEED = 100;
    private static final long SHOOTING_DELAY = 500;
    private static final int WIDTH = 8;
    private static final int HEIGHT = 4;
    public InputListener listener;
    private Main main;
    private Sprite spaceShipSprite;
    private Level level;
    private Sound spaceShipShootSound;
    private Sound spaceShipExplodeSound;
    private Texture spaceShipExplodeTexture;
    private long lastTimeShot;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean dieing = false;

    public SpaceShip(Main main, Level level) {
        this.main = main;
        this.level = level;
        this.lastTimeShot = System.currentTimeMillis();
        spaceShipShootSound = main.assetManager.get("sounds/spaceShipShootSound.mp3");
        spaceShipExplodeSound = main.assetManager.get("sounds/spaceShipExplosionSound.mp3");
        Texture spaceShipTexture = main.assetManager.get("textures/spaceShipTexture.png");
        spaceShipExplodeTexture = main.assetManager.get("textures/spaceShipExplosionTexture.png");

        spaceShipSprite = new Sprite();
        spaceShipSprite.setRegion(spaceShipTexture);
        spaceShipSprite.setPosition(0, 0);
        spaceShipSprite.setSize(WIDTH, HEIGHT);

        setBounds(spaceShipSprite.getX(), spaceShipSprite.getY(),
                spaceShipSprite.getWidth(), spaceShipSprite.getHeight());

        listener = new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT && getX() > 0 && !dieing) {
                    moveLeft = true;
                }
                if (keycode == Input.Keys.RIGHT && !dieing) {
                    moveRight = true;
                }
                if (keycode == Input.Keys.SPACE && !dieing) {
                    shoot();
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    moveLeft = false;
                }
                if (keycode == Input.Keys.RIGHT) {
                    moveRight = false;
                }
                return true;
            }
        };
        addListener(listener);
    }

    @Override
    protected void positionChanged() {
        if (getX() < 0) {
            setX(0);
        } else if (getX() + WIDTH > Level.WORLD_WIDTH) {
            setX(Level.WORLD_WIDTH - WIDTH);
        }
        if (dieing) {
            moveRight = false;
            moveLeft = false;
        }
        spaceShipSprite.setPosition(getX(), getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        spaceShipSprite.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        move(delta);
        handleSpaceInvaderBolts();
        super.act(delta);
    }

    private void shoot() {
        if (System.currentTimeMillis() - lastTimeShot > SHOOTING_DELAY) {
            lastTimeShot = System.currentTimeMillis();
            SpaceShipBullet spaceShipBullet = new SpaceShipBullet(main, level, getX()
                    + WIDTH / 2 - SpaceShipBullet.WIDTH / 2, getY() + HEIGHT);
            level.spaceShipBulletGroup.addActor(spaceShipBullet);
            spaceShipShootSound.play(0.2F);
        }
    }

    private void handleSpaceInvaderBolts() {
        for (Actor actor : level.spaceInvaderBoltGroup.getChildren()) {
            SpaceInvaderBolt spaceInvaderBolt = (SpaceInvaderBolt) actor;
            if (spaceInvaderBolt.getBoundingRectangle()
                    .overlaps(spaceShipSprite.getBoundingRectangle())
                    && !dieing) {
                dieing = true;
                --level.lives;
                level.hud.update();
                spaceShipSprite.setRegion(spaceShipExplodeTexture);
                spaceShipExplodeSound.play(0.1F);
                spaceInvaderBolt.remove();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        level.spaceShip.remove();
                        level.spaceShip = new SpaceShip(main, level);
                        level.stage.addActor(level.spaceShip);
                        level.stage.setKeyboardFocus(level.spaceShip);
                        dieing = false;
                    }
                }, 0.2F);
                return;
            }
        }
    }

    private void move(float delta) {
        if (moveLeft) {
            addAction(Actions.moveBy(-SPEED * delta, 0));
        }
        if (moveRight) {
            addAction(Actions.moveBy(SPEED * delta, 0));
        }
    }

}
