package com.chiayinfan.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chiayinfan.game.SuperMario;

public class WinScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    private Texture texture;
    public SpriteBatch batch;

    public WinScreen(Game game) {
        this.game = game;

        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperMario) game).batch);

        texture = new Texture(Gdx.files.internal("mario.jpeg"));
        batch = new SpriteBatch();

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label congratulatedLabel = new Label("CONGRATULATED MARIO", font);
        Label gameLabel = new Label("WORLD 1-1 FINISHED", font);
        Label continueLabel = new Label("CLICK TO ENTER WORLD 1-2", font);

        table.add(congratulatedLabel).expandX();
        table.row();
        table.add(gameLabel).expandX();
        table.row();
        table.add(continueLabel).expandX();


        stage.addActor(table);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen2((SuperMario) game));
            // get rid of the old screen
            dispose();
        }
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();
        //batch.draw(texture, 520, 0, 120, 210);
        //batch.end();
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
        batch.dispose();

    }
}
