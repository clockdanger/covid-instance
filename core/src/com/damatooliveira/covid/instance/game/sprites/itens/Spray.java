package com.damatooliveira.covid.instance.game.sprites.itens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;

public class Spray extends Sprite {
    public World world;
    public Body b2body;

    private Texture sprite;
    private TextureRegion region_stand;

    private float stateTimer;
    private boolean destroy;
    private boolean setToDestroy;
    private boolean habilited;


    public Spray(PlayScreen playScreen, float x, float y){
        this.world = playScreen.getWorld();
        destroy = false;
        setToDestroy = false;
        habilited = false;

        stateTimer = 0;
        sprite = new Texture(Gdx.files.internal("itens_particles.png"));
        region_stand = new TextureRegion(sprite,2*sprite.getWidth()/4,0,
                sprite.getWidth()/4,sprite.getHeight());
        defineSpray(x,y);
        setBounds(0,0,32/GameMain.PPM, 32/GameMain.PPM);
        setRegion(region_stand);
        setPosition(b2body.getPosition().x - getWidth()/2,
                b2body.getPosition().y - getHeight()/2);
    }

    public void defineSpray(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4/ GameMain.PPM,8/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.SPRAY_BIT;
        fixdef.filter.maskBits = GameMain.BART_BIT;
        fixdef.shape = shape;
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
