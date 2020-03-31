package com.damatooliveira.covid.instance.game.sprites.interatives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;

public class Door extends Sprite {
    public World world;
    public Body b2body;

    private Texture sprite;
    private Array<TextureRegion> regions;

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> closeDoors;
    private float stateTimer;
    private boolean destroy;
    private boolean setToDestroy;
    private boolean redefine;
    private boolean habilited;
    private float x;
    private float y;
    public Door(PlayScreen playScreen,float x, float y){
        this.world = playScreen.getWorld();
        destroy = false;
        setToDestroy = false;
        redefine = false;
        habilited = false;
        stateTimer = 0;
        sprite = new Texture(Gdx.files.internal("porta.png"));
        regions = new Array<TextureRegion>();
        for (int i =0; i<3;i++){
            regions.add(new TextureRegion(sprite,128-i*64,0,64,64));
        }
        closeDoors = new Animation<TextureRegion>(0.1f,regions);
        regions.clear();
        for (int i =0; i<3;i++){
            regions.add(new TextureRegion(sprite,i*64,0,64,64));
        }
        animation = new Animation<TextureRegion>(0.1f, regions);
        this.x = x;
        this.y = y;
        defineDoor(x,y);
        setBounds(0,0,64/ GameMain.PPM, 64/GameMain.PPM);
        setRegion(regions.get(0));
    }
    public  void update(float dt){
        stateTimer +=dt;
        //o box2d tem seu centro à direita em baixo
        if((setToDestroy) && !destroy) {
            destroy = true;
            stateTimer = 0;
        }else if (!setToDestroy && destroy){
            destroy = false;
            stateTimer =0;
        }
        b2body.getFixtureList().first().setSensor(setToDestroy);

        setPosition(b2body.getPosition().x - getWidth()/2,
                b2body.getPosition().y - getHeight()/2);

        setRegion(getFrame(stateTimer));
    }
    public TextureRegion getFrame(float dt){
        TextureRegion region = new TextureRegion();
        if (setToDestroy){
            return animation.getKeyFrame(dt);
        }else{
            return closeDoors.getKeyFrame(dt);
        }
    }
    public void defineDoor(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32/ GameMain.PPM,16/ GameMain.PPM,
                new Vector2(0,16/GameMain.PPM),0);
        fixdef.filter.categoryBits = GameMain.DOOR_BIT;
        fixdef.filter.maskBits = GameMain.VIRUS_BIT|
                GameMain.BART_BIT |GameMain.CONT_BIT | GameMain.ALC_BIT;
        fixdef.shape = shape;
        b2body.createFixture(fixdef).setUserData(this);
        destroy =false;
        setToDestroy = false;
        redefine = false;
    }
    public boolean isDestroyed(){
        return destroy;
    }
    public void dispose(){
        sprite.dispose();
    }
    public void setToDestroy(boolean d){
        setToDestroy = d;
    }

    public void setHabilited(boolean h){
        habilited = h;
    }
    public boolean getHabilited(){
        return habilited;
    }
}
