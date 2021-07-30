package com.chiayinfan.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.chiayinfan.game.Sprites.Enemies.Enemy;
import com.chiayinfan.game.Sprites.InteractiveTileObject;
import com.chiayinfan.game.Sprites.Mario;
import com.chiayinfan.game.SuperMario;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        // there are 2 objects, but don't know which is the mario's head
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        // collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // check which is the mario's head
        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            //  head = fixA.getUserData()
            // if  fixA.getUserData() == "head", head = fixA, else head = fixB
            Fixture head = fixA.getUserData() == "head" ? fixA: fixB;
            //  object = head
            // if  fixA == head, object = fixB, else object = fixA
            Fixture object = head == fixA ? fixB : fixA;
            // check whether the head hit the coin or brick, if so, do the onHeadHit()
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
        switch (cDef){
            // if mario collide with an enemy bit
            case SuperMario.ENEMY_HEAD_BIT | SuperMario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            // if enemy collide with an object bit
            case SuperMario.ENEMY_BIT | SuperMario.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case SuperMario.MARIO_BIT | SuperMario.WELL_BIT:
            case SuperMario.MARIO_BIT | SuperMario.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT){
                    ((Mario) fixA.getUserData()).hit();
                }else{
                    ((Mario) fixB.getUserData()).hit();
                }
                break;

            case SuperMario.ENEMY_BIT | SuperMario.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
