package com.chiayinfan.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chiayinfan.game.Scenes.LeaderBoard;
import com.chiayinfan.game.SuperMario;
import com.chiayinfan.game.Scenes.Hud;

import java.util.ArrayList;


public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    boolean newRecord = false;

    private Game game;

    public GameOverScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperMario) game).batch);

        Preferences prefs = Gdx.app.getPreferences("leaderboard");
        Integer score = prefs.getInteger("highscore", 0);
        System.out.println("before" + score);
        Integer newScore = Hud.getScore();
        System.out.println("before" + newScore);
        if (newScore > score) {
            System.out.println("in if before" + score);
            System.out.println("in if before" + newScore);
            prefs.putInteger("highscore", newScore);
            prefs.flush();
            newRecord = true;
        }
        score = prefs.getInteger("highscore", 0);
        System.out.println("after " + score);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainOverLabel = new Label("CLICK TO PLAY WORLD 1-1 AGAIN", font);

        Label topLabel = new Label("HIGHEST SCORE: " + String.valueOf(score), font);
        Label yourScoreLabel = new Label("YOUR SCORE: " + String.valueOf(newScore), font);
        Label newHighScoreLabel = new Label("NEW HIGHEST RECORD!!!", font);


        table.add(gameOverLabel).expandX();
        if (newRecord) {
            table.row();
            table.add(newHighScoreLabel).expandX().padTop(10f);

        } else {
            table.row();
            table.add(topLabel).expandX().padTop(10f);
        }
        table.row();
        table.add(yourScoreLabel).expandX().padTop(10f);
        table.row();
        table.add(playAgainOverLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((SuperMario) game));
            // get rid of the old screen
            dispose();
        }
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

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
        stage.dispose();

    }
}
