package com.chiayinfan.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.chiayinfan.game.Scenes.Hud;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.SuperMario;

public class Mario extends Sprite {

    public State currentState;
    public State previousState;
    // The world that mario is going to live in
    public World world;
    public Body b2body;
    private Animation marioRun;
    private Animation marioFly;
    private TextureRegion marioDead;
    private Animation marioWin;

    private float stateTimer;
    private boolean runningRight;


    private boolean marioIsDead;

    private boolean marioIsWin = false;
    private int winPosition = 33;


    private TextureRegion marioStand;
    // Constructor
    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STAND;
        previousState = State.STAND;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i ++)
            frames.add(new TextureRegion(getTexture(), i*16, 11, 16, 16));
        // create TextureRegion, catch the pic from Mario_and_Enemies.pack
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 4; i < 6; i ++)
            frames.add(new TextureRegion(getTexture(), i*16, 11, 16, 16));
        marioFly = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 2; i ++)
            frames.add(new TextureRegion(getTexture(), i*16*6, 11, 16, 16));
        marioWin = new Animation(0.5f, frames);
        frames.clear();

        marioDead = new TextureRegion(getTexture(), 16*6,11,16,16);

        defineMario();
        // grab the pic from Mario_and_Enemies.pack up-left to down-right
        marioStand = new TextureRegion(getTexture(), 0,11,16,16);
        // to know how big the sprite actually is
        setBounds(0,0,16/SuperMario.PPM, 16/SuperMario.PPM);
        setRegion(marioStand);
    }
    public void update(float deltaTime){
        // align the mario's position with C.

        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight() /2);
        //System.out.println(b2body.getPosition().x);
        setRegion(getFrame(deltaTime));

    }
    public TextureRegion getFrame(float deltaTime){
        // get mario's current state, Enum JUMP FLY RUN DEAD
        currentState = getState();
        TextureRegion region;

        switch (currentState){
            case DEAD:
                region = marioDead;
                break;
            case FLY:
                region = (TextureRegion) marioFly.getKeyFrame(stateTimer);
                break;
            case WIN:
                region = (TextureRegion) marioWin.getKeyFrame(stateTimer, true);
                break;
            case RUN:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALL:
            case STAND:
            default:
                region = marioStand;
                break;
        }
        // if mario is running to the left but he is not facing to the left
        if (((b2body.getLinearVelocity().x < 0 || ! runningRight )&& !region.isFlipX())){
            region.flip(true,false);
            runningRight = false;
            // if mario is running to the right but he is not facing to the right
        }else if (((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX())){
            region.flip(true,false);
            runningRight = true;
        }
        // if currentState == previousState: stateTimer + deltaTime
        // if currentState != previousState: stateTimer = 0
        stateTimer = currentState == previousState ? stateTimer + deltaTime: 0;
        // update previous state
        previousState = currentState;
        // return final state
        return region;
    }



    public State getState(){
        if (marioIsDead || fallOrTimeUp()){
            return State.DEAD;
        }
        else if (win() || marioIsWin){
            return State.WIN;
        }
        else if (b2body.getLinearVelocity().y>0 ||
                b2body.getLinearVelocity().y<0 && previousState == State.FLY){
            return State.FLY;
        }
        else if (b2body.getLinearVelocity().y<0){
            return State.FALL;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUN;
        }
        else{
            return State.STAND;
        }
    }


    public boolean isDead(){
        return marioIsDead;
    }
    public float getStateTimer(){
        return stateTimer;
    }
    public void deadSoundEffect(){
        SuperMario.manager.get("audio/music/mario_music.ogg", Music.class).stop();
        SuperMario.manager.get("audio/sounds/mariodie.wav", Music.class).play();
    }
    public boolean win(){
        if(b2body.getPosition().x > winPosition && !marioIsWin){
            SuperMario.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            SuperMario.manager.get("audio/sounds/stageclear.wav", Sound.class).play();
            //setPosition(winPosition, 0);

            b2body.applyLinearImpulse(new Vector2(0, 2.0f), b2body.getWorldCenter(),true);

            marioIsWin = true;
            return true;
        }return false;
    }


    public boolean fallOrTimeUp(){
        if(b2body.getPosition().y < 0.210 || Hud.getWorldTimer()<0){
            dead();
            return true;
        }return false;
    }
    public void dead(){
        deadSoundEffect();
        marioIsDead = true;
        Filter filter = new Filter();

        // maskBit is about what a fixture could collide with
        // when it is NOTHING_BIT, then mario cannot collide with the ground
        filter.maskBits = SuperMario.NOTHING_BIT;
        // for every fixture in the box2d body, set the filter bit to NOTHING_BIT
        for (Fixture fixture: b2body.getFixtureList())
            fixture.setFilterData(filter);
        // mario pop up and fall down
        b2body.applyLinearImpulse(new Vector2(0, 1.0f), b2body.getWorldCenter(),true);
    }
    public void hit(){
        dead();
    }

    public void defineMario(){
        BodyDef bDef = new BodyDef();
        bDef.position.set(32/SuperMario.PPM, 32/SuperMario.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);
        // need to be created before any other fixture is set
        fDef.filter.categoryBits = SuperMario.MARIO_BIT;
        // what can mario collide with
        fDef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT|
                SuperMario.ENEMY_BIT|
                SuperMario.OBJECT_BIT|
                SuperMario.ENEMY_HEAD_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        // ˙ o ˙ o: mario's head, ˙ sensor
        head.set(new Vector2(-2/SuperMario.PPM, 6/SuperMario.PPM), new Vector2(2/SuperMario.PPM, 6/SuperMario.PPM));
        fDef.shape = head;
        fDef.isSensor = true;
        b2body.createFixture(fDef).setUserData("head");
    }
}
