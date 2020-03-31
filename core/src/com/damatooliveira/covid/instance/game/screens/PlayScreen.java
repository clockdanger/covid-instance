package com.damatooliveira.covid.instance.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.control.ControleContaminado;
import com.damatooliveira.covid.instance.game.data.GamePlayer;
import com.damatooliveira.covid.instance.game.scene.Hud;
import com.damatooliveira.covid.instance.game.sprites.Bart;
import com.damatooliveira.covid.instance.game.sprites.Contaminado;
import com.damatooliveira.covid.instance.game.sprites.interatives.Door;
import com.damatooliveira.covid.instance.game.sprites.interatives.PhaseShift;
import com.damatooliveira.covid.instance.game.sprites.interatives.TapWater;
import com.damatooliveira.covid.instance.game.sprites.itens.Alcool;
import com.damatooliveira.covid.instance.game.sprites.itens.Spray;
import com.damatooliveira.covid.instance.game.sprites.itens.Virus;
import com.damatooliveira.covid.instance.game.tools.WorldCreator;
import com.damatooliveira.covid.instance.game.tools.WorldLevelManagement;
import com.damatooliveira.covid.instance.game.tools.WorldListener;

public class PlayScreen implements Screen {

    //Reference to our Game, used to set Screens
    private GameMain gameMain;
    //private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //carreamento do mapa
    private WorldLevelManagement management;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private WorldCreator creator;

    private Bart player;

    private int[] layers;

    public PlayScreen(GameMain gameMain){
        this.gameMain = gameMain;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(gameMain.V_WIDTH / gameMain.PPM,
                gameMain.V_HEIGHT / gameMain.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(gameMain);

        //Load our map and setup our map renderer

        management = new WorldLevelManagement(hud.getWorldLevel(), gameMain);
        map = management.getWorldMap();//maploader.load("covid_instance_map/map01.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / gameMain.PPM);
        layers = new int[]{2};

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2,
                gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, 0 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();
        player = new Bart(this);
        creator = new WorldCreator(this);

        //create player in our game world
        world.setContactListener(new WorldListener());

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    //separate our update logic from render

        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
       // b2dr.render(world, gamecam.combined);

        gameMain.gamebatch.setProjectionMatrix(gamecam.combined);
        gameMain.gamebatch.begin();
        for (Door door: creator.getDoors()){
            door.draw(gameMain.gamebatch);
        }
        gameMain.gamebatch.end();
        renderer.render(layers);
        //draw your batch
        gameMain.gamebatch.begin();

        for (TapWater tap : creator.getTaps()){
            tap.draw(gameMain.gamebatch);
        }
        player.draw(gameMain.gamebatch);
        for(Contaminado contaminado : creator.getContaminados()) {
            contaminado.draw(gameMain.gamebatch);
            for (Virus virus : contaminado.getVirus()){
                virus.draw(gameMain.gamebatch);
            }
        }
        for (Alcool alcool: player.getAlcool()){
            alcool.draw(gameMain.gamebatch);
        }
        for(Spray spray: creator.getSprays()) {
            spray.draw(gameMain.gamebatch);
        }
        for (Virus virus: creator.getStaticVirus()){
            virus.draw(gameMain.gamebatch);
        }
        gameMain.gamebatch.end();

        //render our cortines in map
        for(Rectangle rectangle: creator.getCortinasRect()) {
            if (player.getBoundingRectangle().overlaps(rectangle)) renderer.render(layers);
            for (Contaminado contaminado: creator.getContaminados()){
                if (contaminado.getBoundingRectangle().overlaps(rectangle)) renderer.render(layers);
            }
        }

        //Set our batch to now draw what the Hud camera sees.
        gameMain.gamebatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.hudDraw(gameMain.gamebatch);
        for (PhaseShift phase : creator.getPhaseShifts()){
            phase.update(delta);
            if (phase.isDestroyed()){
                if (phase.getParseLevel()==4){
                    gameMain.setScreen(new GameOver(gameMain,new GamePlayer(null,
                            4,
                            hud.getTime()[0],
                            hud.getTime()[1],
                            hud.getTime()[2])
                    ));
                }else {
                    hud.setWorldLevel(phase.getParseLevel());
                    gameMain.playPhase();
                    gameMain.setScreen(new PlayScreen(gameMain));
                }

            }
        }

        if ( hud.getHealth() == 0 || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            hud.setHealth();
            gameMain.setScreen(new GameOver(gameMain,
                    new GamePlayer(null,
                            hud.getWorldLevel(),
                            hud.getTime()[0],
                            hud.getTime()[1],
                            hud.getTime()[2])
                    ));
        }

    }
    public void update(float dt){
        handleInput(dt);
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for (Alcool alcool: player.getAlcool()){
            alcool.update(dt);
        }
        for(Contaminado contaminado : creator.getContaminados()) {
            contaminado.update(dt);
            contaminado.setAlvoPosition(player.getX(),player.getY());
            for (Virus virus : contaminado.getVirus()){
                virus.update(dt);
            }
        }
        for(Spray spray: creator.getSprays()) {
            spray.update(dt);
            if (spray.isDestroyed()){
                hud.addSpray();
                creator.getSprays().removeValue(spray,true);
                gameMain.playInfo();
            }
        }
        for(Door door: creator.getDoors()) {
            door.update(dt);
        }
        for (TapWater tap : creator.getTaps()){
            tap.update(dt);
        }

        for (Virus virus: creator.getStaticVirus()){
            virus.update(dt);
            if (virus.isDestroyed()){
                creator.getStaticVirus().removeValue(virus,true);
            }
        }
        //update our gamecam with correct coordinates after changes
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;
        gamecam.update();
        hud.update(dt);

        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);
    }
    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        for(Spray spray: creator.getSprays()) {
            spray.dispose();
        }
        for(Contaminado contaminado: creator.getContaminados()) {
            contaminado.dispose();
            for (Virus virus : contaminado.getVirus()){
                virus.dispose();
            }
        }
        for (Alcool alcool: player.getAlcool()){
            alcool.dispose();
        }
        for (Door door : creator.getDoors()){
            door.dispose();
        }
        for (TapWater tap : creator.getTaps()){
            tap.dispose();
        }
        for (Virus virus : creator.getStaticVirus()){
            virus.dispose();
        }
    }
    public GameMain getGameMain(){return gameMain;}
    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }
    public Hud getHud(){return hud;}
    public Bart getPlayer(){return player;}

    public void handleInput(float dt){

        //control our player using immediate impulses
        if(player.currentState != Bart.State.DEAD) {
            float vel = 0.5f;
            //player.setAttack(false);
                 if (Gdx.input.isKeyJustPressed(Input.Keys.G)){
                     for (Spray spray : creator.getSprays()){
                         if (spray.getHabilited()){
                             spray.setToDestroy(true);
                         }
                     }
                     for (Door door : creator.getDoors()){
                         if (door.getHabilited())door.setToDestroy(true);
                     }
                     for (TapWater tap : creator.getTaps()){
                         if (tap.getHabilited()){
                             tap.setToDestroy(true);
                             if (hud.getHealth()<100) {
                                 hud.plusHealth();
                                 gameMain.playLife();
                             }
                         }
                     }
                     gameMain.playInteract();
                 }
                if (Gdx.input.isKeyJustPressed(Input.Keys.F)){
                //player.b2body.applyForceToCenter(0,0.1f,true);
                    if (hud.getSpraysQuantities()>0){
                        player.fireAlcool();
                        hud.lessSpray();
                        gameMain.playShot();
                    }
                     player.setAttack(true);
                }else if (!(player.getState() == Bart.State.ATTACK)){
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        //player.b2body.applyForceToCenter(0,0.1f,true);
                        player.b2body.setLinearVelocity(0, vel);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        // player.b2body.applyForceToCenter(0,-0.1f,true);
                        player.b2body.setLinearVelocity(0, -vel);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                        // player.b2body.applyForceToCenter(0.1f,0,true);
                        player.b2body.setLinearVelocity(vel, 0);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                        // player.b2body.applyForceToCenter(-0.1f,0,true);
                        player.b2body.setLinearVelocity(-vel, 0);
                    } else {
                        player.b2body.setLinearVelocity(0, 0);
                    }
                }

                /*if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    //player.b2body.applyForceToCenter(0,0.1f,true);
                    gamecam.zoom += 1 / GameMain.PPM;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                    //player.b2body.applyForceToCenter(0,0.1f,true);
                    gamecam.zoom -= 1 / GameMain.PPM;
                }*/
            }
    }
}
