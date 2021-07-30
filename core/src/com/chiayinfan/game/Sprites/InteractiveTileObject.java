package com.chiayinfan.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.SuperMario;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX()+bounds.getWidth()/2)/ SuperMario.PPM, (bounds.getY()+bounds.getHeight()/2)/SuperMario.PPM);

        body = world.createBody(bDef);
        // start at center and expand in both direction
        shape.setAsBox(bounds.getWidth()/2/SuperMario.PPM, bounds.getHeight()/2/SuperMario.PPM);
        fDef.shape = shape;
        fixture = body.createFixture(fDef);
    }
    public abstract void onHeadHit();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    };

    public TiledMapTileLayer.Cell getCell(){
        // get the graphic layer
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        // *SuperMario.PPM, scale up back to the tilemap scale, divide by the tile size(16)
        return layer.getCell((int)(body.getPosition().x * SuperMario.PPM/16), (int)(body.getPosition().y * SuperMario.PPM/16));

    }
}
