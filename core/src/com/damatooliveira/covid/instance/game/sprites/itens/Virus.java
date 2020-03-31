package com.damatooliveira.covid.instance.game.sprites.itens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;
import com.damatooliveira.covid.instance.game.sprites.Contaminado;

public class Virus extends Sprite {
    public World world;
    public Body b2body;

    private Texture sprite;
    private TextureRegion region_stand;

    private float stateTimer;
    private boolean destroy;
    private boolean setToDestroy;
    public Virus(Contaminado contaminado){
        this.world = contaminado.world;
        destroy = false;
        stateTimer = 0;
        sprite = new Texture(Gdx.files.internal("itens_particles.png"));
        region_stand = new TextureRegion(sprite,sprite.getWidth()/4,0,
                sprite.getWidth()/4,sprite.getHeight());
        float velocity = 0.6f;

        switch (contaminado.standingState){
            case WALK_UP:
                defineSpray((contaminado.getX()+contaminado.getWidth()/2)*GameMain.PPM,
                        (contaminado.getY()+contaminado.getHeight())*GameMain.PPM);
                b2body.setLinearVelocity(0,velocity);
                break;
            case WALK_DOWN:
                defineSpray((contaminado.getX()+contaminado.getWidth()/2)*GameMain.PPM,
                        (contaminado.getY())*GameMain.PPM);
                b2body.setLinearVelocity(0,-velocity);
                break;
            case WALK_R:
                defineSpray((contaminado.getX()+contaminado.getWidth())*GameMain.PPM,
                        (contaminado.getY()+contaminado.getHeight()/2)*GameMain.PPM);
                b2body.setLinearVelocity(velocity,0);
                break;
            case WALK_L:
                defineSpray((contaminado.getX())*GameMain.PPM,
                        (contaminado.getY()+contaminado.getHeight()/2)*GameMain.PPM);
                b2body.setLinearVelocity(-velocity,0);
                break;
            default:
                defineSpray((contaminado.getX()+contaminado.getWidth()/2)*GameMain.PPM,
                        (contaminado.getY()+contaminado.getHeight()/2)*GameMain.PPM);
                break;

        }
        setBounds(0,0,16/ GameMain.PPM, 16/GameMain.PPM);
        setRegion(region_stand);
    }
    //alternative options for virus in scene
    public Virus(PlayScreen playScreen, float x, float y){
        this.world = playScreen.getWorld();
        destroy = false;
        stateTimer = 0;
        sprite = new Texture(Gdx.files.internal("itens_particles.png"));
        region_stand = new TextureRegion(sprite,sprite.getWidth()/4,0,
                sprite.getWidth()/4,sprite.getHeight());

        defineSpray(x, y);
        setBounds(0,0,16/ GameMain.PPM, 16/GameMain.PPM);
        setRegion(region_stand);
    }

    public  void update(float dt){
        stateTimer +=dt;
        //o box2d tem seu centro à direita em baixo
        if(setToDestroy && !destroy) {
            world.destroyBody(b2body);
            destroy = true;
        }
        setPosition(b2body.getPosition().x - getWidth()/2,
                b2body.getPosition().y - getHeight()/2);
    }
    public void defineSpray(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8/ GameMain.PPM,8/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.VIRUS_BIT;
        fixdef.filter.maskBits = GameMain.WALL_BIT | GameMain.ALC_BIT|
                GameMain.OBJ_BIT |GameMain.BART_BIT |GameMain.DOOR_BIT;
        fixdef.shape = shape;
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
    public  void setVoidVelocity(){
        b2body.setLinearVelocity(0,0);
    }
}
