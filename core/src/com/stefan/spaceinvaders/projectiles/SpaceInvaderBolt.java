package com.stefan.spaceinvaders.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.objects.Wall;
import com.stefan.spaceinvaders.screens.Level;
import com.stefan.spaceinvaders.util.GlobalLocalPosition;

public class SpaceInvaderBolt extends Actor implements GlobalLocalPosition {

    private Vector2 globalPosition;
    private Vector2 localPosition;
    private Sprite spaceInvaderBoltSprite;
    private Level level;

    private static final float WIDTH = 2;
    private static final float HEIGHT = 3;
    private static final int SPEED = 50;

    public SpaceInvaderBolt(Main main, Level level, float x, float y) {
        this.level = level;
        localPosition = new Vector2(x, y);
        globalPosition = new Vector2(x, y);
        spaceInvaderBoltSprite = new Sprite();
        Texture spaceInvaderBoltTexture = main.assetManager.get("textures/spaceInvaderBoltTexture.png");
        spaceInvaderBoltSprite.setRegion(spaceInvaderBoltTexture);
        spaceInvaderBoltSprite.setPosition(globalPosition.x, globalPosition.y);
        spaceInvaderBoltSprite.setSize(WIDTH, HEIGHT);
        setBounds(spaceInvaderBoltSprite.getX(), spaceInvaderBoltSprite.getY(),
                spaceInvaderBoltSprite.getWidth(), spaceInvaderBoltSprite.getHeight());
    }

    @Override
    protected void positionChanged() {
        setGlobalLocalPosition();
        super.positionChanged();
    }

    @Override
    public void act(float delta) {
        if (globalPosition.y < 0) {
            remove();
        }
        handleWallCollision();
        addAction(Actions.moveBy(0, -SPEED * delta));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switchToLocalPosition();
        spaceInvaderBoltSprite.draw(batch);
        super.draw(batch, parentAlpha);
        switchToGlobalPosition();
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
        spaceInvaderBoltSprite.setPosition(globalPosition.x, globalPosition.y);
    }

    @Override
    public void switchToLocalPosition() {
        spaceInvaderBoltSprite.setPosition(localPosition.x, localPosition.y);
    }

    public Rectangle getBoundingRectangle() {
        return spaceInvaderBoltSprite.getBoundingRectangle();
    }

    private void handleWallCollision() {
        outer:
        for (Actor actor : level.wallGroup.getChildren()) {
            Wall wall = (Wall) actor;
            for (Rectangle rectangle : wall.wallChunks) {
                if (spaceInvaderBoltSprite.getBoundingRectangle().overlaps(rectangle)) {
                    wall.removeChunk(rectangle);
                    remove();
                    break outer;
                }
            }
        }
    }

}
