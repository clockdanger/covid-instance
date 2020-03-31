package com.damatooliveira.covid.instance.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.damatooliveira.covid.instance.game.GameMain;

public class Hud {

    private Array<Label> labels;
    private FitViewport viewport;
    public Stage stage;

    private static int sprayQuant = 0;
    private float timeCount;
    private static int timerS;
    private static int timerM;
    private static int timeH;
    public static int health =100;

    public static int worldLevel = 1;

    private Texture texture;
    private Array<TextureRegion> regions;
    private GameMain gameMain;
    public Hud(GameMain gameMain) {
        //define our tracking variables
        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(GameMain.V_WIDTH, GameMain.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, gameMain.gamebatch);
        timerS = 0;
        timerM = 0;
        timeH = 0;

        texture = new Texture(Gdx.files.internal("itens_particles.png"));
        regions = new Array<TextureRegion>();
        regions.add(new TextureRegion(texture,64,0,32,32));
        regions.add(new TextureRegion(texture,96,0,32,32));
        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        labels = new Array<Label>();
        //define ur labels using the String, and a Label style consisting of a font and color
        //spray
        labels.add(new Label(Integer.toString(sprayQuant), new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        //time
        labels.add(new Label(String.format("%2d", timeH) + "h "
                +String.format("%2d", timerM) + "m " + String.format("%02d", timerS) + "s",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        //alcool
        labels.add(new Label(String.format("%3d", health) + "%", new Label.LabelStyle(new BitmapFont(), Color.GREEN)));
        //lvl
        labels.add(new Label("Lvl "+String.format("%2d", worldLevel), new Label.LabelStyle(new BitmapFont(), Color.GOLD)));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(labels.get(0)).expandX().padTop(10);
        table.add(labels.get(1)).expandX().padTop(10);
        table.add(labels.get(2)).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(labels.get(3)).expandX();
        //add our table to the stage
        stage.addActor(table);
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            timerS++;
            if (timerS >= 60) {
                timerM++;
                timerS = 0;
                if (timerM>=60){
                    timeH++;
                    timerM = 0;
                }
            }
            timeCount = 0;
            labels.get(1).setText(String.format(String.format("%2d", timeH) + "h "
                    +"%2d", timerM) + "m " + String.format("%02d", timerS) + "s");
        }
    }

    public void hudDraw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(regions.get(0),
                (labels.get(0).getX()-regions.get(0).getRegionWidth())
                ,(labels.get(0).getY()-regions.get(0).getRegionHeight()/4)
                ,regions.get(0).getRegionWidth(),
                regions.get(0).getRegionHeight());
        spriteBatch.draw(regions.get(1),
                (labels.get(2).getX()-regions.get(1).getRegionWidth()*0.8f)
                ,(labels.get(2).getY()),0,0
                ,regions.get(1).getRegionWidth(),
                regions.get(1).getRegionHeight(),
                0.5f,0.5f,0);

        spriteBatch.end();
    }

    public void addSpray() {
        sprayQuant+=5;
        labels.get(0).setText(Integer.toString(sprayQuant));
    }

    public void lessSpray() {
        sprayQuant--;
        labels.get(0).setText(Integer.toString(sprayQuant));
    }

    public void plusHealth() {
        health += 10;
        labels.get(2).setText(String.format("%3d", health) + "%");
    }

    public void lessHealth() {
        health -= 10;
        labels.get(2).setText(String.format("%3d", health) + "%");
    }

    public int getHealth() {
        return health;
    }

    public int[] getTime() {
        int [] integers = new int[]{timeH,timerM,timerS};
        return integers;
    }
    public int getSpraysQuantities(){
        return sprayQuant;
    }
    public int getWorldLevel(){
        return worldLevel;
    }
    public void setWorldLevel(int wvl){
        worldLevel = wvl;
        labels.get(3).setText("Lvl "+String.format("%2d", worldLevel));
    }
    public void setHealth(){
        health = 100;
    }
}
