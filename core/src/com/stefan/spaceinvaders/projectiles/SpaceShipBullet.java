package com.stefan.spaceinvaders.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.aliens.SpaceInvader;
import com.stefan.spaceinvaders.objects.Wall;
import com.stefan.spaceinvaders.screens.Level;
import com.stefan.spaceinvaders.util.GlobalLocalPosition;

public class SpaceShipBullet extends Actor implements GlobalLocalPosition {

    private Level level;
    private Vector2 localPosition;
    private Vector2 globalPosition;
    private Sprite spaceShipBulletSprite;
    public static final float WIDTH = 2;
    private static final float HEIGHT = 4;
    private static final float SPEED = 100;

    public SpaceShipBullet(Main main, Level level, float x, float y) {
        this.level = level;
        Texture playerBulletTexture = main.assetManager.get("textures/spaceShipBulletTexture.png");
        localPosition = new Vector2(x, y);
        globalPosition = new Vector2(x, y);

        spaceShipBulletSprite = new Sprite();
        spaceShipBulletSprite.setRegion(playerBulletTexture);
        spaceShipBulletSprite.setPosition(x, y);
        spaceShipBulletSprite.setSize(WIDTH, HEIGHT);

        setBounds(spaceShipBulletSprite.getX(), spaceShipBulletSprite.getY(),
                spaceShipBulletSprite.getWidth(), spaceShipBulletSprite.getHeight());

    }

    @Override
    public void act(float delta) {
        if (globalPosition.y + SpaceInvader.HEIGHT > Level.WORLD_HEIGHT) {
            remove();
        }
        moveBy(0, SPEED * delta);
        handleWallCollision();
        handleSpaceInvaderBoltCollision();
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switchToLocalPosition();
        spaceShipBulletSprite.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
        switchToGlobalPosition();
    }

    @Override
    protected void positionChanged() {
        setGlobalLocalPosition();
        super.positionChanged();
    }

    @Override
    public void setGlobalLocalPosition() {
        float localX = getX();
        float localY = getY();
        localPosition.set(localX, localY);
        globalPosition.set(localX, localY);
        level.spaceShipBulletGroup.localToStageCoordinates(globalPosition);
    }

    @Override
    public void switchToGlobalPosition() {
        spaceShipBulletSprite.setPosition(globalPosition.x, globalPosition.y);
    }

    @Override
    public void switchToLocalPosition() {
        spaceShipBulletSprite.setPosition(localPosition.x, localPosition.y);
    }

    private void handleSpaceInvaderBoltCollision() {
        for (Actor actor : level.spaceInvaderBoltGroup.getChildren()) {
            SpaceInvaderBolt spaceInvaderBolt = (SpaceInvaderBolt) actor;
            if (spaceInvaderBolt.getBoundingRectangle()
                    .overlaps(spaceShipBulletSprite.getBoundingRectangle())) {
                actor.remove();
                remove();
                return;
            }
        }
    }

    public Rectangle getBoundingRectangle() {
        return spaceShipBulletSprite.getBoundingRectangle();
    }

    private void handleWallCollision() {
        outer:
        for (Actor actor : level.wallGroup.getChildren()) {
            Wall wall = (Wall) actor;
            for (Rectangle rectangle : wall.wallChunks) {
                if (spaceShipBulletSprite.getBoundingRectangle().overlaps(rectangle)) {
                    wall.removeChunk(rectangle);
                    remove();
                    break outer;
                }
            }
        }
    }

}
