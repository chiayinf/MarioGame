package com.chiayinfan.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.Sprites.Brick;
import com.chiayinfan.game.Sprites.Coin;
import com.chiayinfan.game.Sprites.Enemies.Enemy;
import com.chiayinfan.game.Sprites.Enemies.Goomba;
import com.chiayinfan.game.Sprites.Enemies.Turtle;
import com.chiayinfan.game.Sprites.Well;
import com.chiayinfan.game.SuperMario;
//World creator, create all objects, except mario
public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

/*
    public Array<Goomba> getGoombas() {
        return goombas;
    }

 */
    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;
        // create ground bodies and fixtures
        // down to up, get ground
        for (MapObject object:map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ SuperMario.PPM, (rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

            body = world.createBody(bDef);
            // start at center and expand in both direction
            shape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM, rectangle.getHeight()/2/SuperMario.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);
        }
        // create pipes bodies and fixtures
        for (MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/SuperMario.PPM, (rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

            body = world.createBody(bDef);
            // start at center and expand in both direction
            shape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM, rectangle.getHeight()/2/SuperMario.PPM);
            fDef.shape = shape;
            // when enemy collide the pipes the enemy could turn arround
            fDef.filter.categoryBits = SuperMario.OBJECT_BIT;
            body.createFixture(fDef);
        }

        // create pipes bricks and fixtures
        for (MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, rectangle);

        }

        // create pipes coins and fixtures
        for (MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rectangle);
        }
        // create all goombas
        goombas = new Array<Goomba>();
        for (MapObject object:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rectangle.getX()/SuperMario.PPM, rectangle.getY()/SuperMario.PPM));
        }

        // create all turtles
        turtles = new Array<Turtle>();
        for (MapObject object:map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rectangle.getX()/SuperMario.PPM, rectangle.getY()/SuperMario.PPM));
        }
        // create wells
        for (MapObject object:map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Well(screen, rectangle);
        }


    }
    
}
