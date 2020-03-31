package com.damatooliveira.covid.instance.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.damatooliveira.covid.instance.game.data.GameData;
import com.damatooliveira.covid.instance.game.screens.SplashScreen;

public class GameMain extends Game {

    //Virtual Screen size and Box2D Scale(Pixels Per Meter)
    public static final int V_WIDTH = 512;
    public static final int V_HEIGHT = 256;
    public static final float PPM = 100;

    public static final short WALL_BIT = 1;
    public static final short BART_BIT = 2;
    public static final short CONT_BIT = 4;
    public static final short SPRAY_BIT = 8;
    public static final short VIRUS_BIT = 16;
    public static final short ALC_BIT = 32;
    public static final short OBJ_BIT = 64;
    public static final short DOOR_BIT = 128;
    public static final short TAP_BIT = 256;
    public static final short PHASE_BIT = 512;

    public SpriteBatch gamebatch;
    public static AssetManager manager;

    private GameData gameData;
    @Override
    public void create() {
        gamebatch = new SpriteBatch();
        this.gameData = new GameData();
        manager = new AssetManager();
        manager.load("sounds/health.wav", Sound.class);
        manager.load("sounds/hit.wav", Sound.class);
        manager.load("sounds/info.wav",Sound.class);
        manager.load("sounds/interact.wav",Sound.class);
        manager.load("sounds/phase.wav",Sound.class);
        manager.load("sounds/select.wav",Sound.class);
        manager.load("sounds/shot.wav",Sound.class);
        manager.finishLoading();

        setScreen(new SplashScreen(this));
        Gdx.app.setLogLevel(Logger.DEBUG);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render () {
        super.render();
    }

    public GameData getGameData() {
        return gameData;
    }
    public void playClick(){
        manager.get("sounds/select.wav",Sound.class).play();
    }
    public void playLife(){
        manager.get("sounds/health.wav",Sound.class).play();
    }
    public void playShot(){
        manager.get("sounds/shot.wav",Sound.class).play();
    }
    public void playPhase(){
        manager.get("sounds/phase.wav",Sound.class).play();
    }
    public void playHit(){
        manager.get("sounds/hit.wav",Sound.class).play();
    }
    public void playInteract(){
        manager.get("sounds/interact.wav",Sound.class).play();
    }
    public void playInfo(){
        manager.get("sounds/info.wav",Sound.class).play();
    }
}
