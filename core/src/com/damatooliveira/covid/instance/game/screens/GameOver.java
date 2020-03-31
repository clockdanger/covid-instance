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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.data.GamePlayer;
import com.damatooliveira.covid.instance.game.scene.Hud;

public class GameOver implements Screen {
    private GameMain gameMain;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;

    private boolean isCallMenuScreen = false;
    private boolean isSave = false;
    private String name;
    private TextField field;
    private GamePlayer gp;
    private Texture texture;

    public GameOver(GameMain gameMain, GamePlayer gp){
        this.gameMain = gameMain;
        this.gp = gp;
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameMain.V_WIDTH, GameMain.V_HEIGHT, camera);
        stage = new Stage(viewport);
        texture = new Texture(Gdx.files.internal("game_over_cv.png"));
        // aqui da permissao para a o stage usar o (down, up, checked, over)
        Gdx.input.setInputProcessor(stage);

        // cria um botao, pode criar com um eventListener pode usar um
        // (InputListener, ClickListener...)
        creatTextButton("SAVE", GameMain.V_HEIGHT *0.8f).addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if ( button == Input.Buttons.LEFT) {
                    callMenuScreen();

                }

                return true;
            }
        });
        field = createTextField("your name"); //GameMain.V_HEIGHT * 0.6f

        ImageButton img = new ImageButton(skin);
        img.setPosition( (GameMain.V_WIDTH - img.getWidth()) /2f, field.getY() - field.getHeight() /2f);
        stage.addActor(img);
        stage.addActor(field);
    }

    public GameOver(GameMain main) {
        this(main, null);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameMain.gamebatch.begin();
        gameMain.gamebatch.draw(texture,0,0,camera.viewportWidth,camera.viewportHeight);
        gameMain.gamebatch.end();

        stage.act(delta);
        stage.draw();

        if (isCallMenuScreen) {

            gp.setName(field.getText());
            gameMain.getGameData().save(
                    gp.getName(),
                    gp.getLevel(),
                    gp.getHora(),
                    gp.getMinuto(),
                    gp.getSegundo());
            Hud.worldLevel = 1;
            gameMain.setScreen(new Menu(gameMain));
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

    private TextField createTextField(String message) {
        TextField field = new TextField("", skin);
        field.setPosition( ( GameMain.V_WIDTH - field.getWidth() ) / 2f,
                (GameMain.V_HEIGHT - field.getHeight()) / 2f);
        field.setMessageText(message);
        field.setMaxLength(8);
        field.setAlignment(Align.center);

        return field;
    }

    private TextButton creatTextButton(String str, float y){

        TextButton bnt = new TextButton(str, skin);
        bnt.setPosition( ( GameMain.V_WIDTH - bnt.getWidth() )*0.9f, y);

        stage.addActor(bnt);

        return bnt;
    }

    public void callMenuScreen() {
        isCallMenuScreen = true;
        gameMain.playClick();
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public void setName(String name) {
        this.name = name;
    }
}
