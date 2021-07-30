package com.chiayinfan.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chiayinfan.game.SuperMario;

public class Hud implements Disposable {
    //Scene2D.ui stage and its viewport for hud
    public Stage stage;
    private Viewport viewport;

    // Mario score/time tracking variables
    private static Integer worldTimer;
    private float timeCount;
    private static Integer score;

    // Scene2D widgets
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 6;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        // Reference https://github.com/libgdx/libgdx/wiki/Scene2d.ui#widget-and-widgetgroup
        stage = new Stage(viewport, sb);

        // To organize the widgets
        Table table = new Table();
        table.top();
        // set the table as the size of the stage
        table.setFillParent(true);

        // set the format of the timer, font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // TIME is string already, dont need to use String.format
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // Update your name here
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // These three evenly occupy the top row 1/3, 1/3, 1/3
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        // Add a new row(second row)
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);

    }

    public static Integer getScore() {
        return score;
    }

    public static Integer getWorldTimer() {
        return worldTimer;
    }



    // Update the timer
    public void update(float deltaTime){
        timeCount += deltaTime;
        if(timeCount >= 1){
            worldTimer -= 1;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        if (worldTimer <4){
            countdownLabel.setColor(Color.RED);
        }
    }
    // static reference hud without passing coin/brick object
    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }


    @Override
    public void dispose() {
        stage.dispose();

    }
}
