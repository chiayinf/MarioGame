package com.chiayinfan.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.chiayinfan.game.Scenes.Hud;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.Sprites.Enemies.Enemy;
import com.chiayinfan.game.SuperMario;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;


    // Constructor
    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i ++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i *16, 0, 16, 16 ));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        // to know how big the sprite actually is
        setBounds(getX(), getY(), 16/SuperMario.PPM, 16/SuperMario.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float deltaTime){
        stateTime += deltaTime;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
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

    // remove goomba from screen, draw goomba => draw nothing
    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
    @Override
    public void hitOnHead() {
        setToDestroy = true;
        Hud.addScore(500);
    }
}
