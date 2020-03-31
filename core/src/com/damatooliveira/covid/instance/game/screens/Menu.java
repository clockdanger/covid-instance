package com.damatooliveira.covid.instance.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.damatooliveira.covid.instance.game.GameMain;

import javax.xml.soap.Text;

public class Menu implements Screen {

    private GameMain gameMain;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;

    private Texture texture;

    private boolean isCallPlayScreen = false;
    private boolean isCallHighScoreScreen = false;
    private boolean isCallControlScreen = false;


    public Menu(GameMain gameMain){
        this.gameMain = gameMain;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        texture = new Texture(Gdx.files.internal("covid_instance.png"));
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameMain.V_WIDTH, GameMain.V_HEIGHT, camera);
        stage = new Stage(viewport);

        // aqui da permissao para a o stage usar o (down, up, checked, over)
        Gdx.input.setInputProcessor(stage);

        // cria um botao, pode criar com um eventListener pode usar um
        // (InputListener, ClickListener...)
        creatTextButton("START", GameMain.V_HEIGHT *0.7f).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if ( button == Input.Buttons.LEFT) {
                    callPlayScreen();
                }

                return true;
            }
        });
        creatTextButton("HIGHSCORE", GameMain.V_HEIGHT*0.6f).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if ( button == Input.Buttons.LEFT) {
                    callHighScoreScreen();
                }

                return true;
            }
        });
        creatTextButton("CONTROLS", GameMain.V_HEIGHT*0.5f).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if ( button == Input.Buttons.LEFT) {
                    callControlScreen();
                }

                return true;
            }
        });

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameMain.gamebatch.setProjectionMatrix(camera.combined);
        gameMain.gamebatch.begin();
        gameMain.gamebatch.draw(texture,0,0, GameMain.V_WIDTH, GameMain.V_HEIGHT);
        gameMain.gamebatch.end();
        stage.act(delta);
        stage.draw();
        if ( isCallPlayScreen  ) {
            gameMain.playClick();
            gameMain.setScreen(new PlayScreen(gameMain));
        }else if ( isCallHighScoreScreen) {
            gameMain.playClick();
            gameMain.setScreen(new HighScore(gameMain));
        }else if ( isCallControlScreen ) {
            gameMain.playClick();
            gameMain.setScreen(new ControlsScreen(gameMain));
        }

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
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
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }

    private TextButton creatTextButton(String str, float y){

        TextButton bnt = new TextButton(str, skin);
        bnt.setPosition( ( GameMain.V_WIDTH - bnt.getWidth() )*0.95f, y);

        stage.addActor(bnt);

        return bnt;
    }

    public void callPlayScreen() {
        isCallPlayScreen = true;
    }
    public void callControlScreen(){
        isCallControlScreen = true;
    }
    public void callHighScoreScreen(){
        isCallHighScoreScreen = true;

    }
}
