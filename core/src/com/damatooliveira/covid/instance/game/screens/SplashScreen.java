package com.damatooliveira.covid.instance.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.damatooliveira.covid.instance.game.GameMain;

public class SplashScreen implements Screen {
    private Texture texture;
    private GameMain gameMain;
    private float alpha;
    private float dt;
    private Color color;
    private OrthographicCamera camera;
    public SplashScreen (GameMain gameMain){
        this.gameMain = gameMain;
        alpha = 1;
        dt =0;
        color = gameMain.gamebatch.getColor();
        texture = new Texture("logo_by_flavio.png");
        camera = new OrthographicCamera();

        camera.setToOrtho(false,GameMain.V_WIDTH,GameMain.V_HEIGHT);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dt += delta;
        gameMain.gamebatch.setColor(color);
        gameMain.gamebatch.setProjectionMatrix(camera.combined);
        gameMain.gamebatch.begin();
        gameMain.gamebatch.draw(texture,0,0,camera.viewportWidth,camera.viewportHeight);
        gameMain.gamebatch.end();
        if (dt>1) {
            alpha -= delta;
        }
        color.a = alpha;
        if (alpha<=0){
            color.a = 1;
            gameMain.gamebatch.setColor(color);
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
}
