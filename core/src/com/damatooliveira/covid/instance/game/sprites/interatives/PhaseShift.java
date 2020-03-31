package com.damatooliveira.covid.instance.game.sprites.interatives;

import com.badlogic.gdx.physics.box2d.*;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;

public class PhaseShift {
    private boolean destroy;
    private boolean setToDestroy;
    private Body b2body;
    private World world;
    private int level;
    public PhaseShift(PlayScreen playScreen, int level, float x, float y){
        this.world = playScreen.getWorld();
        this.level = level;
        defineSpray(x, y);
    }
    public void defineSpray(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/ GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32/ GameMain.PPM,18/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.PHASE_BIT;
        fixdef.filter.maskBits = GameMain.BART_BIT;
        fixdef.shape = shape;
        fixdef.isSensor = true;
        b2body.createFixture(fixdef).setUserData(this);
    }
    public void update(float dt){
        if(setToDestroy && !destroy) {
            world.destroyBody(b2body);
            destroy = true;
        }
    }
    public boolean isDestroyed(){
        return destroy;
    }
    public void setToDestroy(boolean d){
        setToDestroy = d;
    }
    public int getParseLevel(){
        return level;
    }
}
