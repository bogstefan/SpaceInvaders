package com.stefan.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.stefan.spaceinvaders.Main;
import com.stefan.spaceinvaders.aliens.SI;
import com.stefan.spaceinvaders.aliens.SpaceInvader;
import com.stefan.spaceinvaders.objects.Wall;
import com.stefan.spaceinvaders.player.SpaceShip;
import com.stefan.spaceinvaders.util.LinearEquation;

public class Level implements Screen {

    public static final float WORLD_WIDTH = 120;
    public static final float WORLD_HEIGHT = 200;
    private static final float WALL_Y_START = 20;
    private static final float WALL_WIDTH = 15;
    private static final float WALL_HEIGHT = 10;
    private static final float MIN_SPEED = 0.1F;
    private static final float MAX_SPEED = 0.001F;
    private static final long MIN_SOUND_SPEED = 800;
    private static final long MAX_SOUND_SPEED = 100;
    private static final int MAX_INVADERS = 12;
    public Stage stage;
    public Group spaceInvaderGroup;
    public Group spaceInvaderBoltGroup;
    public Group spaceShipBulletGroup;
    public Group wallGroup;
    public SpaceShip spaceShip;
    public int lives = 3;
    public int score;
    public Hud hud;
    private Vector2 globalFurthestLeft;
    private Vector2 globalFurthestRight;
    private Vector2 globalFurthestDown;
    private Sound spaceInvaderSound1;
    private Sound spaceInvaderSound2;
    private Sound spaceInvaderSound3;
    private Sound spaceInvaderSound4;
    private Sound currentSpaceInvaderSound;
    private Main main;
    private boolean moveRight = true;
    private boolean gameOver = false;
    private boolean won = false;
    private float spaceInvaderSpeed = MIN_SPEED;
    private long spaceInvaderSoundSpeed;
    private long lastTimeSpaceInvaderSoundPlayed;

    Level(Main main) {
        this.main = main;
        globalFurthestDown = new Vector2();
        globalFurthestLeft = new Vector2();
        globalFurthestRight = new Vector2();

        //assets
        spaceInvaderSound1 = main.assetManager.get("sounds/spaceInvaderSound1.mp3");
        spaceInvaderSound2 = main.assetManager.get("sounds/spaceInvaderSound2.mp3");
        spaceInvaderSound3 = main.assetManager.get("sounds/spaceInvaderSound3.mp3");
        spaceInvaderSound4 = main.assetManager.get("sounds/spaceInvaderSound4.mp3");
        currentSpaceInvaderSound = spaceInvaderSound1;

        //groups
        spaceInvaderGroup = new Group();
        spaceInvaderBoltGroup = new Group();
        spaceShipBulletGroup = new Group();
        wallGroup = new Group();

        addSpaceInvader(SI.S1, 1);
        addSpaceInvader(SI.S2, 2);
        addSpaceInvader(SI.S3, 3);
        addSpaceInvader(SI.S4, 4);
        addSpaceInvader(SI.S5, 5);

        //add walls
        addWalls();

        //stage
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), main.batch);

        spaceShip = new SpaceShip(main, this);
        stage.addActor(spaceShip);
        stage.addActor(spaceInvaderGroup);
        stage.addActor(spaceInvaderBoltGroup);
        stage.addActor(spaceShipBulletGroup);
        stage.addActor(wallGroup);

        hud = new Hud(main, this);

        //input processor
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(spaceShip);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getCamera().update();
        main.batch.setProjectionMatrix(stage.getCamera().combined);
        main.renderer.setProjectionMatrix(stage.getCamera().combined);

        updateSpaceInvaderSpeed();
        updateSpaceInvaderSoundSpeed();
        playSpaceInvaderSound();
        moveSpaceInvader();
        checkForGameOver();
        checkForWin();

        stage.act(delta);
        stage.draw();

        main.batch.setProjectionMatrix(hud.hudStage.getCamera().combined);
        main.renderer.setProjectionMatrix(hud.hudStage.getCamera().combined);
        hud.hudStage.act(delta);
        hud.hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        Gdx.input.setInputProcessor(null);
        spaceShip.removeListener(spaceShip.listener);
        spaceShip.remove();
        for (Actor actor : spaceInvaderGroup.getChildren()) {
            actor.remove();
        }
        for (Actor actor : spaceInvaderBoltGroup.getChildren()) {
            actor.remove();
        }
        for (Actor actor : spaceShipBulletGroup.getChildren()) {
            actor.remove();
        }
        for (Actor actor : wallGroup.getChildren()) {
            ((Wall) actor).destroy();
            actor.remove();
        }
        spaceInvaderGroup.remove();
        stage.dispose();
        hud.lives.remove();
        hud.score.remove();
        hud.table.remove();
    }

    private void moveSpaceInvader() {
        if (!spaceInvaderGroup.hasChildren()) {
            won = true;
            return;
        }

        Actor furthestLeft = spaceInvaderGroup.getChildren().get(0);
        Actor furthestRight = spaceInvaderGroup.getChildren().get(0);
        Actor furthestDown = spaceInvaderGroup.getChildren().get(0);

        //get extrema
        for (Actor actor : spaceInvaderGroup.getChildren()) {
            if (actor.getX() < furthestLeft.getX()) {
                furthestLeft = actor;
            }
            if (actor.getX() > furthestRight.getX()) {
                furthestRight = actor;
            }
            if (actor.getY() < furthestDown.getY()) {
                furthestDown = actor;
            }
        }

        globalFurthestLeft.set(furthestLeft.getX(), furthestLeft.getY());
        globalFurthestRight.set(furthestRight.getX(), furthestRight.getY());
        globalFurthestDown.set(furthestDown.getX(), furthestDown.getY());
        spaceInvaderGroup.localToStageCoordinates(globalFurthestLeft);
        spaceInvaderGroup.localToStageCoordinates(globalFurthestRight);
        spaceInvaderGroup.localToStageCoordinates(globalFurthestDown);
        if (globalFurthestDown.y < WALL_Y_START + WALL_WIDTH) {
            gameOver = true;
            return;
        }

        //moving
        if ((globalFurthestLeft.x < 0 || globalFurthestRight.x + SpaceInvader.WIDTH > WORLD_WIDTH)
                && !spaceInvaderGroup.hasActions()) {
            moveRight = !moveRight;
            float direction = moveRight ? 1 : -1;
            spaceInvaderGroup.addAction(Actions.sequence(Actions.moveBy(0, -SpaceInvader.HEIGHT,
                    spaceInvaderSpeed), Actions.moveBy(direction, 0,
                    spaceInvaderSpeed)));
        } else if (moveRight && !spaceInvaderGroup.hasActions()) {
            spaceInvaderGroup.addAction(Actions.moveBy(1, 0, spaceInvaderSpeed));
        } else if (!moveRight && !spaceInvaderGroup.hasActions()) {
            spaceInvaderGroup.addAction(Actions.moveBy(-1, 0, spaceInvaderSpeed));
        }
    }

    private void addSpaceInvader(SI spaceInvader, int row) {
        int current_Amount = 0;
        float currentX = WORLD_WIDTH / 50;
        float y;
        float start = 20;
        float bufferY = SpaceInvader.HEIGHT / 2;
        float bufferX = SpaceInvader.WIDTH / 2;

        switch (row) {
            case 1:
                y = WORLD_HEIGHT - start - (SpaceInvader.HEIGHT + bufferY);
                break;
            case 2:
                y = WORLD_HEIGHT - start - 2 * (SpaceInvader.HEIGHT + bufferY);
                break;
            case 3:
                y = WORLD_HEIGHT - start - 3 * (SpaceInvader.HEIGHT + bufferY);
                break;
            case 4:
                y = WORLD_HEIGHT - start - 4 * (SpaceInvader.HEIGHT + bufferY);
                break;
            case 5:
                y = WORLD_HEIGHT - start - 5 * (SpaceInvader.HEIGHT + bufferY);
                break;
            default:
                y = 0;
                break;
        }

        while (current_Amount < MAX_INVADERS) {
            switch (spaceInvader) {
                case S1:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S1, currentX, y));
                    break;
                case S2:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S2, currentX, y));
                    break;
                case S3:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S3, currentX, y));
                    break;
                case S4:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S4, currentX, y));
                    break;
                case S5:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S5, currentX, y));
                    break;
                case S6:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S6, currentX, y));
                    break;
                case S7:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S7, currentX, y));
                    break;
                case S8:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S8, currentX, y));
                    break;
                case S9:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S9, currentX, y));
                    break;
                default:
                    spaceInvaderGroup.addActor(new SpaceInvader(main, this, SI.S1, currentX, y));
                    break;
            }
            currentX += SpaceInvader.WIDTH + bufferX;
            ++current_Amount;
        }
    }

    private void addWalls() {
        float distance = (WORLD_WIDTH - 4 * WALL_WIDTH) / 3F;
        wallGroup.addActor(new Wall(main, 0, WALL_Y_START,
                WALL_WIDTH, WALL_HEIGHT));
        wallGroup.addActor(new Wall(main, WALL_WIDTH + distance, WALL_Y_START,
                WALL_WIDTH, WALL_HEIGHT));
        wallGroup.addActor(new Wall(main, 2 * WALL_WIDTH + 2 * distance, WALL_Y_START,
                WALL_WIDTH, WALL_HEIGHT));
        wallGroup.addActor(new Wall(main, WORLD_WIDTH - WALL_WIDTH, WALL_Y_START,
                WALL_WIDTH, WALL_HEIGHT));
    }

    private void updateSpaceInvaderSpeed() {
        float[] mAndQ = LinearEquation.mAndQ(60, 1, MIN_SPEED, MAX_SPEED);
        spaceInvaderSpeed = mAndQ[0] * spaceInvaderGroup.getChildren().size + mAndQ[1];
    }

    private void updateSpaceInvaderSoundSpeed() {
        float[] mAndQ = LinearEquation.mAndQ(60, 1, MIN_SOUND_SPEED, MAX_SOUND_SPEED);
        spaceInvaderSoundSpeed = (long) (mAndQ[0] * spaceInvaderGroup.getChildren().size + mAndQ[1]);
    }

    private void playSpaceInvaderSound() {
        if (System.currentTimeMillis() - lastTimeSpaceInvaderSoundPlayed > spaceInvaderSoundSpeed
                && spaceInvaderGroup.getChildren().size > 0) {
            currentSpaceInvaderSound.play(0.1F);

            for (Actor actor : spaceInvaderGroup.getChildren()) {
                SpaceInvader spaceInvader = (SpaceInvader) actor;
                spaceInvader.changeState();
            }

            if (currentSpaceInvaderSound == spaceInvaderSound1) {
                currentSpaceInvaderSound = spaceInvaderSound2;
            } else if (currentSpaceInvaderSound == spaceInvaderSound2) {
                currentSpaceInvaderSound = spaceInvaderSound3;
            } else if (currentSpaceInvaderSound == spaceInvaderSound3) {
                currentSpaceInvaderSound = spaceInvaderSound4;
            } else if (currentSpaceInvaderSound == spaceInvaderSound4) {
                currentSpaceInvaderSound = spaceInvaderSound1;
            }
            lastTimeSpaceInvaderSoundPlayed = System.currentTimeMillis();
        }
    }

    private void checkForGameOver() {
        if (lives == 0) {
            gameOver = true;
        }

        if (gameOver) {
            dispose();
            main.setScreen(new GameOver(main, score));
        }
    }

    private void resetStage() {
        //remove actors
        for (Actor actor : spaceInvaderGroup.getChildren()) {
            actor.remove();
        }
        for (Actor actor : spaceInvaderBoltGroup.getChildren()) {
            actor.remove();
        }
        for (Actor actor : spaceShipBulletGroup.getChildren()) {
            actor.remove();
        }

        spaceInvaderGroup.remove();
        spaceInvaderBoltGroup.remove();
        spaceShipBulletGroup.remove();

        //add actors
        spaceInvaderGroup = new Group();
        spaceInvaderBoltGroup = new Group();
        spaceShipBulletGroup = new Group();

        stage.addActor(spaceInvaderGroup);
        stage.addActor(spaceInvaderBoltGroup);
        stage.addActor(spaceShipBulletGroup);

        addSpaceInvader(SI.S1, 1);
        addSpaceInvader(SI.S2, 2);
        addSpaceInvader(SI.S3, 3);
        addSpaceInvader(SI.S4, 4);
        addSpaceInvader(SI.S5, 5);

    }

    private void checkForWin() {
        if (won) {
            won = false;
            resetStage();
        }
    }

}