package com.chiayinfan.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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

public class Well extends InteractiveTileObject {
    public Well(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.WELL_BIT);
    }

    @Override
    public void onHeadHit() {

    }
}
