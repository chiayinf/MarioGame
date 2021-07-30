package com.chiayinfan.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.chiayinfan.game.Scenes.Hud;
import com.chiayinfan.game.Scenes.MyTextInputListener;
import com.chiayinfan.game.Sprites.Enemies.Enemy;
import com.chiayinfan.game.Sprites.State;
import com.chiayinfan.game.Sprites.Mario;
import com.chiayinfan.game.SuperMario;
import com.chiayinfan.game.Tools.B2WorldCreator;
import com.chiayinfan.game.Tools.WorldContactListener;


public class PlayScreen implements Screen {
    private SuperMario game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    // Load the map into the game
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Mario player;

    private Music music;

    private boolean isTimeAlert = false;
    private int alertTime = 4;

    // constructor
    public PlayScreen(SuperMario game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        // Keep the ratio of the pic and refill the empty space with black bar to maintain the aspect ratio
        gamePort = new FitViewport(SuperMario.V_WIDTH/SuperMario.PPM, SuperMario.V_HEIGHT/SuperMario.PPM, gameCam);
        hud = new Hud(game.batch);

        // import background to the game
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/SuperMario.PPM);
        // initially set the gameCam to be centered correctly
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // when the object stands still, box2d wont calculate and simulation
        // Sleep the object when rest, save time when doing calculations
        world = new World(new Vector2(0, -10), true);
        // allows for debug lines of the box2d world
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        // creat mario in the game world, player is created after the world is created
        player = new Mario( this);

        world.setContactListener(new WorldContactListener());

        music = SuperMario.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        //music.play();
        // x, y the starting point after falling

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }
    public void handleInput(float deltaTime){
        if(player.currentState != State.DEAD) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 0.3f), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

        }
    }
    public void update(float deltaTime) {
        // handle user input first
        handleInput(deltaTime);
        // takes 1 step in the physic simulation(60 times per second)
        world.step(1/60f, 6, 2);

        player.update(deltaTime);
        for(Enemy enemy:creator.getEnemies()){
            enemy.update(deltaTime);
            // activate the goomba when mario is getting close enough
            if(enemy.getX() < player.getX() + 220/SuperMario.PPM)
                enemy.b2body.setActive(true);
        }

        hud.update(deltaTime);

        // attach the gameCam to the mario's x axial
        if(player.currentState != State.DEAD){
        gameCam.position.x = player.b2body.getPosition().x;}
        // update the game camera
        gameCam.update();
        // update what exactly needs to be updated
        renderer.setView(gameCam);

    }

    @Override
    public void render(float delta) {
        update(delta);
        // clear the game screen with black
        ScreenUtils.clear(0, 0, 0, 1);
        // render the game map
        renderer.render();

        // render the Box2DDebugLines
        b2dr.render(world, gameCam.combined);
        // control the main part of the camera
        game.batch.setProjectionMatrix((gameCam.combined));
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy:creator.getEnemies()){
            enemy.draw(game.batch);
        }
        game.batch.end();

        // set our batch to draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(win()){
            game.setScreen(new WinScreen(game));
            dispose();
        }
        timer();



    }
    public boolean gameOver(){
        if(player.currentState == State.DEAD && player.getStateTimer() > 2 ){
            return true;
        }
        return false;
    }
    public boolean win(){

        if(player.currentState == State.WIN && player.getStateTimer() > 2 ){
            return true;
        }
        return false;
    }
    public void timer(){
        if(Hud.getWorldTimer()<alertTime && !isTimeAlert) {
            SuperMario.manager.get("audio/sounds/notime.wav", Sound.class).play();
            isTimeAlert = true;
        }
    }
    @Override
    public void resize(int width, int height) {
        // update the game viewport
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld() {
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
