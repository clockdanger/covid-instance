package com.damatooliveira.covid.instance.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.data.Data;
import com.damatooliveira.covid.instance.game.data.GameData;
import com.damatooliveira.covid.instance.game.data.GamePlayer;

import java.io.IOException;
import java.util.ArrayList;

public class HighScore implements Screen {
    private GameMain gameMain;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Texture texture;
    private Skin skin;
    private Stage stage;
    private Table table;
    private boolean isCallMenuScreen;
    private ArrayList<GamePlayer> gamePlayers;
    public HighScore(final GameMain gameMain){
        this.gameMain = gameMain;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        texture = new Texture(Gdx.files.internal("covid_instance.png"));
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameMain.V_WIDTH, GameMain.V_HEIGHT, camera);
        stage = new Stage(viewport);
        isCallMenuScreen = false;
        table = new Table();
        table.center();
        table.setFillParent(true);
        gamePlayers = new ArrayList<GamePlayer>();
        loadGamePlayers();
        for (int i = 0; i < gamePlayers.size(); i++){
            creatLabel(gamePlayers.get(i).getName() +
                            " - [Lvl] - "+gamePlayers.get(i).getLevel()
                            +" "+ "- [Survival Time] - : "+
                    gamePlayers.get(i).getHora()+"h "+
                    gamePlayers.get(i).getMinuto()+"m "+
                    gamePlayers.get(i).getSegundo()+"s "
                    );
        }

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        creatTextButton("BACK", GameMain.V_HEIGHT *0.1f).addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if ( button == Input.Buttons.LEFT) {
                    gameMain.playClick();
                    callMenuScreen();
                }
                return true;
            }
        });
    }
    @Override
    public void show() {
        loadGamePlayers();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameMain.gamebatch.begin();
        //  gameMain.gamebatch.draw(texture,0,0,camera.viewportWidth,camera.viewportHeight);
        gameMain.gamebatch.end();

        stage.act(delta);
        stage.draw();
        if ( isCallMenuScreen || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ) {
            gameMain.setScreen(new Menu(gameMain));
        }

    }

    @Override
    public void resize(int width, int height) {

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
        texture.dispose();
    }
    private TextButton creatTextButton(String str, float y){

        TextButton bnt = new TextButton(str, skin);
        bnt.setPosition( ( GameMain.V_WIDTH - bnt.getWidth() ) *0.2f, y);

        stage.addActor(bnt);

        return bnt;
    }
    private Label creatLabel(String str ){

        Label lbl = new Label(str, skin,"little");
        //lbl.setPosition( ( GameMain.V_WIDTH - lbl.getWidth() ) / 2f, y);
        table.add(lbl).center();
        table.row();
        return lbl;
    }


    public void callMenuScreen() {
        isCallMenuScreen = true;
    }
    private void loadGamePlayers(){
        try{
            Data data = (Data) gameMain.getGameData().load();

            if ( data != null ) {
                gamePlayers = data.getTopDown();
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException e2){

        }
    }

}
