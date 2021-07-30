package com.chiayinfan.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chiayinfan.game.Screens.PlayScreen;
import com.chiayinfan.game.Screens.WelcomeScreen1;
import com.chiayinfan.game.Screens.WinScreen;

public class SuperMario extends Game {
	private String playerName;
	// Virtual width and height for the game
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	// pixel per meter
	public static final float PPM = 100;
	// category bit for logic and/or...
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT= 4;
	public static final short COIN_BIT= 8;
	public static final short DESTROYED_BIT= 16;
	// pipes
	public static final short OBJECT_BIT= 32;
	public static final short ENEMY_BIT= 64;
	public static final short ENEMY_HEAD_BIT= 128;
	public static final short WELL_BIT= 256;




	public SpriteBatch batch;
	// to add sound effects
	public static AssetManager manager;


	@Override
	public void create () {
		batch = new SpriteBatch();
		// to add sound effects
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/mariodie.wav", Music.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/stageclear.wav", Sound.class);
		manager.load("audio/sounds/notime.wav", Sound.class);
		manager.finishLoading();


		setScreen(new WelcomeScreen1(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
