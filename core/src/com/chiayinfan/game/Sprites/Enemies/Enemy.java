package com.chiayinfan.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.chiayinfan.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected  World world;
    protected  PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        // put b2dr into sleep and dont calculate the simulation
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float deltatime);
    public abstract void hitOnHead();
    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

}
