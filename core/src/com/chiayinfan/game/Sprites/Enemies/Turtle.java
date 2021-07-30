package com.chiayinfan.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.Sprites.State;
import com.chiayinfan.game.SuperMario;

public class Turtle extends Enemy{
    public State currentState;
    public State previousState;


    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private TextureRegion turtleShell;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private float stateTimer;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i ++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), i*16, 0, 16, 24 ));
        walkAnimation = new Animation(0.4f, frames);
        currentState =previousState = State.WALK;
        turtleShell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 4*16, 0, 16, 24 );

        // to know how big the sprite actually is
        setBounds(getX(), getY(), 16/ SuperMario.PPM, 24/SuperMario.PPM);


    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        // align the position with goomba = new Goomba(this, 5.32f, .32f);
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);
        // need to be created before any other fixture is set
        fDef.filter.categoryBits = SuperMario.ENEMY_BIT;
        // what can goomba collide with
        fDef.filter.maskBits = SuperMario.GROUND_BIT
                | SuperMario.COIN_BIT
                | SuperMario.BRICK_BIT
                | SuperMario.ENEMY_BIT
                | SuperMario.OBJECT_BIT
                | SuperMario.MARIO_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);

        // create the head here
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1/SuperMario.PPM);
        vertice[1] = new Vector2(5, 8).scl(1/SuperMario.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1/SuperMario.PPM);
        vertice[3] = new Vector2(3, 3).scl(1/SuperMario.PPM);
        head.set(vertice);

        // reuse the fDef.shape = shape; above
        fDef.shape = head;
        // bounce up to 0.5x
        fDef.restitution = 0.5f;
        fDef.filter.categoryBits = SuperMario.ENEMY_HEAD_BIT;

        b2body.createFixture(fDef).setUserData(this);


    }

    @Override
    public void update(float deltaTime) {
        setRegion(getFrame(deltaTime));
        if(currentState == State.SHELL && stateTime > 5){
            currentState = State.WALK;
            velocity.x = 1;
        }
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-8/SuperMario.PPM);
        b2body.setLinearVelocity(velocity);

    }

    private TextureRegion getFrame(float deltaTime) {
        TextureRegion region;
        switch (currentState){
            case SHELL:
                region = turtleShell;
                break;
            case WALK:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        // flip the direction of the turtle
        if(velocity.x>0 && region.isFlipX() == false){
            region.flip(true, false);
        }
        if(velocity.x<0 && region.isFlipX() == true){
            region.flip(true, false);
        }
        // if currentState == previousState: stateTime + deltaTime
        // if currentState != previousState: stateTime = 0
        stateTime = currentState == previousState ? stateTime + deltaTime : 0;
        // update previous state
        previousState = currentState;
        // return final state
        return region;
    }

    @Override
    public void hitOnHead() {
        if (currentState!= State.SHELL){
            currentState = State.SHELL;
            velocity.x = 0;
        }

    }
}
