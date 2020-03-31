package com.damatooliveira.covid.instance.game.sprites.interatives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;

public class TapWater extends Sprite {
    public World world;
    public Body b2body;

    private Texture sprite;
    private Array<TextureRegion> regions;

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> closeWater;
    private Animation <TextureRegion> water;
    private float stateTimer;
    private boolean destroy;
    private boolean setToDestroy;
    private boolean habilited;
    private float x;
    private float y;
    public TapWater(PlayScreen playScreen, float x, float y){
        this.world = playScreen.getWorld();
        destroy = false;
        setToDestroy = false;
        habilited = false;
        stateTimer = 0;
        sprite = new Texture(Gdx.files.internal("torneira.png"));
        regions = new Array<TextureRegion>();
        for (int j =0; j<2; j++) {
            for (int i = 0; i < 2; i++) {
                regions.add(new TextureRegion(sprite, 32 - 32 * i, 32 - j * 32, 32, 32));
            }
        }
        closeWater = new Animation<TextureRegion>(0.2f,regions);
        regions.clear();
        regions.add(new TextureRegion(sprite,  0, 32, 32, 32));
        regions.add(new TextureRegion(sprite, 32, 32, 32, 32));
        water = new Animation<TextureRegion>(0.2f,regions);
        water.setPlayMode(Animation.PlayMode.LOOP);
        regions.clear();
        for (int j =0; j<2;j++){
            for (int i =0; i<2; i++) {
                regions.add(new TextureRegion(sprite, i * 32, j*32, 32, 32));
            }
        }
        animation = new Animation<TextureRegion>(0.2f, regions);
        this.x = x;
        this.y = y;
        defineTap(x,y);
        setBounds(0,0,32/ GameMain.PPM, 32/GameMain.PPM);
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
        setPosition(b2body.getPosition().x - getWidth()/2,
                b2body.getPosition().y - getHeight()/2);

        setRegion(getFrame(stateTimer));

    }
    public TextureRegion getFrame(float dt){
        TextureRegion region = new TextureRegion();
        if (setToDestroy){
            if (stateTimer>=animation.getAnimationDuration()) {
                return water.getKeyFrame(dt, true);
            }else {
                return animation.getKeyFrame(dt);
            }
        }else{
            return closeWater.getKeyFrame(dt);
        }
    }
    public void defineTap(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16/ GameMain.PPM,16/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.TAP_BIT;
        fixdef.filter.maskBits = GameMain.BART_BIT | GameMain.ALC_BIT;
        fixdef.shape = shape;
        fixdef.isSensor = true;
        b2body.createFixture(fixdef).setUserData(this);
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
