package com.stefan.spaceinvaders.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectSet;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.screens.Level;

public class Wall extends Actor {

    public ObjectSet<Rectangle> wallChunks;
    private Main main;

    public Wall(Main main, float x, float y, float width, float height) {
        this.main = main;
        wallChunks = new ObjectSet<Rectangle>();
        construct((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        main.renderer.begin(ShapeRenderer.ShapeType.Line);

        for (Rectangle rectangle : wallChunks) {
            main.renderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

        main.renderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void construct(int x, int y, int width, int height) {
        for (int currentY = y; currentY < y + height; ++currentY) {
            for (int currentX = x; currentX < x + width; ++currentX) {
                wallChunks.add(new Rectangle(currentX, currentY, 1, 1));
            }
        }
    }

    public void removeChunk(Rectangle rectangle) {
        wallChunks.remove(rectangle);
    }

    public void destroy(){
        wallChunks.clear();
    }

}
