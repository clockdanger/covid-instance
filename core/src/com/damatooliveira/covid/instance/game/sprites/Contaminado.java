package com.damatooliveira.covid.instance.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.control.ControleContaminado;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;
import com.damatooliveira.covid.instance.game.sprites.itens.Virus;

public class Contaminado extends Sprite {
    //for animation
    public enum  State {  STANDING, WALK_UP, WALK_R, WALK_L, WALK_DOWN ,ATTACK, DEAD };
    public State currentState;
    public State previousState;
    public State standingState;

    public World world;
    public Body b2body;

    private Texture contaminado_sprite;
    private Array<TextureRegion> contRegion_RL;
    private Array<TextureRegion> contRegion_U;
    private Array<TextureRegion> contRegion_D;
    private Array<TextureRegion> contRegion_stand;
    private Array<TextureRegion> contRegionAttack;

    //for animation
    private Animation<TextureRegion> contRL;
    private Animation<TextureRegion> contTop;
    private Animation<TextureRegion> contDown;

    private float stateTimer;
    private boolean contIsDead;
    private boolean setToDestroy;
    private boolean attack;
    private boolean persecetution;
    private Vector2 alvo;

    private Array<Virus> virus;

    private ControleContaminado control;

    public Contaminado(PlayScreen playScreen, float x, float y){
        this.world = playScreen.getWorld();
        //for animation
        currentState = State.STANDING;
        previousState = State.STANDING;
        standingState = State.STANDING;

        stateTimer = 0;
        contIsDead = false;
        setToDestroy = false;
        persecetution = true;
        //for fill texturesRegion
        //bart_sprite = new Texture(Gdx.files.internal("bart_covid_instance.png"));
        contaminado_sprite = new Texture(Gdx.files.internal("contaminado.png"));
        contRegion_U = new Array<TextureRegion>();
        contRegion_D = new Array<TextureRegion>();
        contRegion_RL = new Array<TextureRegion>();
        contRegion_stand = new Array<TextureRegion>();
        contRegionAttack =  new Array<TextureRegion>();
        //movement region
        for (int i =1; i < 9; i++){
            contRegion_U.add(new TextureRegion(contaminado_sprite,i*32,0,32,48));
            contRegion_RL.add(new TextureRegion(contaminado_sprite,i*32,48,32,48));
            contRegion_D.add(new TextureRegion(contaminado_sprite,i*32,96,32,48));
        }
        //stand region
        for (int i = 0; i<3; i++){
            contRegion_stand.add(new TextureRegion(contaminado_sprite,0,i*48,32,48));
        }
        //attack region
        for (int i = 0; i<3; i++){
            contRegionAttack.add(new TextureRegion(contaminado_sprite,i*32,144,32,48));
        }

        contTop = new Animation<TextureRegion>(0.1f, contRegion_U);
        contDown = new Animation<TextureRegion>(0.1f, contRegion_D);
        contRL = new Animation<TextureRegion>(0.1f, contRegion_RL);

        alvo = new Vector2();
        virus = new Array<Virus>();

        control = new ControleContaminado(this);

        defineCont(x, y);
        setBounds(0,0,32/GameMain.PPM, 48/GameMain.PPM);
    }
    public  void update(float dt){
        //o box2d tem seu centro à direita em baixo
         for(Virus ball : virus) {
            ball.update(dt);
            if(ball.isDestroyed())
                virus.removeValue(ball, true);
        }
        setPosition(b2body.getPosition().x - getWidth() / 2,
                b2body.getPosition().y - getHeight() / 2);

        if (persecetution && dist() < 200/GameMain.PPM){
            control.persecutionPoint(dt);
        }else {
            control.movementStandart(dt);
            persecetution = true;
        }
        setRegion(getFrame(dt));
    }
    public TextureRegion getFrame (float dt){
        currentState = getState();
        TextureRegion region = new TextureRegion();
        switch (currentState){
            case DEAD:
               // region =;
                break;
            case WALK_DOWN:
                region = contDown.getKeyFrame(stateTimer,true);
                break;
            case WALK_UP:
                region = contTop.getKeyFrame(stateTimer,true);
                break;
            case WALK_R:
                region = contRL.getKeyFrame(stateTimer,true);
                break;
            case WALK_L:
                region = contRL.getKeyFrame(stateTimer,true);
                break;
            case ATTACK:
                switch (standingState){
                    case WALK_UP:
                        region = contRegionAttack.get(0);
                        break;
                    case WALK_DOWN:
                        region = contRegionAttack.get(1);
                        break;
                    case WALK_R:
                        region = contRegionAttack.get(2);
                        if (!region.isFlipX()) region.flip(true,false);
                        break;
                    case WALK_L:
                        region = contRegionAttack.get(2);
                        if (region.isFlipX()) region.flip(true,false);
                        break;
                    default:
                        region = contRegionAttack.get(2);
                        break;
                }
                break;
            case STANDING:
                switch (standingState){
                    case WALK_UP:
                        region = contRegion_stand.get(0);
                        break;
                    case WALK_DOWN:
                        region = contRegion_stand.get(2);
                        break;
                    case WALK_R:
                        region = contRegion_stand.get(1);
                        if (!region.isFlipX()) region.flip(true,false);
                        break;
                    case WALK_L:
                        region = contRegion_stand.get(1);
                        if (region.isFlipX()) region.flip(true,false);
                        break;
                    default:
                        region = contRegion_stand.get(1);
                        break;
                }
                break;
            default:
                region = contRegion_stand.get(1);
                break;
        }
        //invertendo textura
        if ((currentState == State.WALK_R) && !region.isFlipX()){
            region.flip(true,false);
        }else if((currentState == State.WALK_L) && region.isFlipX()){
            region.flip(true,false);
        }
        /*se statetimet = current timer e for igual a previousState
        faça stateTimer + dt, caso não todos ficam iguais a zero*/
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        if (currentState == Contaminado.State.ATTACK && stateTimer>=10*dt) attack =false;
        previousState = currentState;
        return  region;
    }
    public State getState(){
        if (contIsDead){
            return State.DEAD;
        }else if (attack) {
            return State.ATTACK;
        } else if (b2body.getLinearVelocity().y > 0) {
            standingState = State.WALK_UP;
            return State.WALK_UP;
        } else if (b2body.getLinearVelocity().y < 0) {
            standingState = State.WALK_DOWN;
            return State.WALK_DOWN;
        } else if (b2body.getLinearVelocity().x > 0) {
            standingState = State.WALK_R;
            return State.WALK_R;
        } else if (b2body.getLinearVelocity().x < 0) {
            standingState = State.WALK_L;
            return State.WALK_L;
        } else {
            return State.STANDING;
        }
    }
    public void defineCont(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/GameMain.PPM,y/GameMain.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12/ GameMain.PPM,24/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.CONT_BIT;
        fixdef.filter.maskBits = GameMain.WALL_BIT | GameMain.BART_BIT
                | GameMain.OBJ_BIT |GameMain.VIRUS_BIT | GameMain.ALC_BIT |GameMain.CONT_BIT|
                GameMain.DOOR_BIT;
        fixdef.shape = shape;

        b2body.createFixture(fixdef).setUserData(this);

    }
    public boolean isDead(){
        return contIsDead;
    }
    public float getStateTimer(){
        return  stateTimer;
    }
    public void dispose(){
        contaminado_sprite.dispose();
    }
    public void setAlvoPosition(float x,float y){
        alvo.set(x,y);
    }
    public Vector2 getAlvoPosition(){return alvo;}
    public void reverseVelocity(boolean b){
        if ( !persecetution && b && (currentState == State.WALK_L|| currentState == State.WALK_R)) {
            b2body.setLinearVelocity(-b2body.getLinearVelocity().x,b2body.getLinearVelocity().y);
        }
        if ( !persecetution && b && (currentState == State.WALK_UP|| currentState == State.WALK_DOWN)) {
            b2body.setLinearVelocity(b2body.getLinearVelocity().x,-b2body.getLinearVelocity().y);
        }
    }
    public void fireVirus(){
        virus.add(new Virus(this));
        attack = true;
    }
    public Array<Virus> getVirus(){
        return virus;
    }
    public double dist(){
        double dist;

        dist = Math.sqrt(
                Math.pow((double)(alvo.x-this.getX()),2)+
                Math.pow((double) ( alvo.y-this.getY()),2)
        );
        return dist;
    }
    public void setPersecetution(boolean persecetution){
        this.persecetution = persecetution;
    }
}
