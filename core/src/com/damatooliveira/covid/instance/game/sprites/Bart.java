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
import com.damatooliveira.covid.instance.game.screens.PlayScreen;
import com.damatooliveira.covid.instance.game.sprites.itens.Alcool;

public class Bart extends Sprite {
    //for animation
    public enum  State {  STANDING, WALK_UP, WALK_R, WALK_L, WALK_DOWN , ATTACK,DEAD };
    public State currentState;
    public State previousState;
    public State standingState;

    public World world;
    public Body b2body;

    private Texture bart_sprite;
    private Array<TextureRegion> bartRegion_RL;
    private Array<TextureRegion> bartRegion_U;
    private Array<TextureRegion> bartRegion_D;
    private Array<TextureRegion> bartRegion_stand;
    private Array<TextureRegion> bartRegionAttack;


    //for animation
    private Animation<TextureRegion> bartRL;
    private Animation<TextureRegion> bartTop;
    private Animation<TextureRegion> bartDown;

    private float hitTime;
    private float stateTimer;
    private boolean bartIsDead;
    private boolean isHit;
    private boolean attack;

    private Array<Alcool> alcools;

    private PlayScreen playScreen;

    private Vector2 position;


    public Bart(PlayScreen playScreen){
        this.world = playScreen.getWorld();
        this.playScreen = playScreen;
        //for animation
        currentState = State.STANDING;
        previousState = State.STANDING;
        standingState = State.STANDING;

        stateTimer = 0;
        hitTime = 0;
        bartIsDead = false;
        attack = false;
        //for fill texturesRegion
        bart_sprite = new Texture(Gdx.files.internal("bart_covid_instance.png"));
        // bart_sprite = new Texture(Gdx.files.internal("contaminado.png"));
        bartRegion_U = new Array<TextureRegion>();
        bartRegion_D = new Array<TextureRegion>();
        bartRegion_RL = new Array<TextureRegion>();
        bartRegion_stand = new Array<TextureRegion>();
        bartRegionAttack = new Array<TextureRegion>();
        //movement region
        for (int i =1; i < 9; i++){
            bartRegion_U.add(new TextureRegion(bart_sprite,i*32,0,32,48));
            bartRegion_RL.add(new TextureRegion(bart_sprite,i*32,48,32,48));
            bartRegion_D.add(new TextureRegion(bart_sprite,i*32,96,32,48));
        }
        //stand region
        for (int i = 0; i<3; i++){
            bartRegion_stand.add(new TextureRegion(bart_sprite,0,i*48,32,48));
        }
        //attack region
        for (int i = 0; i<3; i++){
            bartRegionAttack.add(new TextureRegion(bart_sprite,i*32,144,32,48));
        }

        bartTop = new Animation<TextureRegion>(0.1f,bartRegion_U);
        bartDown = new Animation<TextureRegion>(0.1f,bartRegion_D);
        bartRL = new Animation<TextureRegion>(0.1f,bartRegion_RL);

        alcools = new Array<Alcool>();
        position = new Vector2();

        defineBart();
        setBounds(0,0,32/GameMain.PPM, 48/GameMain.PPM);

    }
    public  void update(float dt){
        //o box2d tem seu centro à direita em baixo
        setPosition(b2body.getPosition().x - getWidth() / 2,
                b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        for(Alcool  ball : alcools) {
            ball.update(dt);
            if(ball.isDestroyed())
                alcools.removeValue(ball, true);
        }
        if (isHit){
            if (hitTime >= 2){
                hitInBody();
                hitTime = 0;
            }
            hitTime+=dt;
        }
    }
    public TextureRegion getFrame (float dt){
        currentState = getState();
        TextureRegion region = new TextureRegion();

        switch (currentState){
            case DEAD:
               // region =;
                break;
            case WALK_DOWN:
                region = bartDown.getKeyFrame(stateTimer,true);
                break;
            case WALK_UP:
                region = bartTop.getKeyFrame(stateTimer,true);
                break;
            case WALK_R:
                region = bartRL.getKeyFrame(stateTimer,true);
                break;
            case WALK_L:
                region = bartRL.getKeyFrame(stateTimer,true);
                break;
            case ATTACK:
                switch (standingState){
                    case WALK_UP:
                        region = bartRegionAttack.get(0);
                        break;
                    case WALK_DOWN:
                        region = bartRegionAttack.get(1);
                        break;
                    case WALK_R:
                        region = bartRegionAttack.get(2);
                        if (!region.isFlipX()) region.flip(true,false);
                        break;
                    case WALK_L:
                        region = bartRegionAttack.get(2);
                        if (region.isFlipX()) region.flip(true,false);
                        break;
                    default:
                        region = bartRegionAttack.get(2);
                        break;
                }
                break;
            case STANDING:
                switch (standingState){
                    case WALK_UP:
                        region = bartRegion_stand.get(0);
                        break;
                    case WALK_DOWN:
                        region = bartRegion_stand.get(2);
                        break;
                    case WALK_R:
                        region = bartRegion_stand.get(1);
                        if (!region.isFlipX()) region.flip(true,false);
                        break;
                    case WALK_L:
                        region = bartRegion_stand.get(1);
                        if (region.isFlipX()) region.flip(true,false);
                        break;
                    default:
                        region = bartRegion_stand.get(1);
                        break;
                }
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
        if (currentState == State.ATTACK && stateTimer>=10*dt) attack =false;

        previousState = currentState;
        return  region;
    }
    public State getState() {

        if (bartIsDead) {
            return State.DEAD;
        } else if (attack) {
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
    public void defineBart(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x/ GameMain.PPM,position.y/ GameMain.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fixdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10/ GameMain.PPM,23/ GameMain.PPM);
        fixdef.filter.categoryBits = GameMain.BART_BIT;
        fixdef.filter.maskBits = GameMain.WALL_BIT | GameMain.OBJ_BIT
        |GameMain.VIRUS_BIT |GameMain.CONT_BIT | GameMain.DOOR_BIT | GameMain.PHASE_BIT;
        fixdef.shape = shape;

        PolygonShape shapeSensor = new PolygonShape();
        shapeSensor.setAsBox(24/GameMain.PPM, 36/GameMain.PPM);
        FixtureDef fixSensor = new FixtureDef();
        fixSensor.filter.categoryBits = GameMain.BART_BIT;
        fixSensor.filter.maskBits = GameMain.SPRAY_BIT | GameMain.DOOR_BIT
                | GameMain.TAP_BIT;
        fixSensor.shape = shapeSensor;
        fixSensor.isSensor = true;

        b2body.createFixture(fixdef).setUserData(this);
        b2body.createFixture(fixSensor).setUserData(this);
    }
    public boolean isDead(){
        return  bartIsDead;
    }
    public void hitInBody(){
        if (playScreen.getHud().getHealth()>0){
            playScreen.getHud().lessHealth();
            playScreen.getGameMain().playHit();
        };
    }
    public float getStateTimer(){
        return  stateTimer;
    }
    public void dispose(){
        bart_sprite.dispose();
    }
    public void fireAlcool(){
        alcools.add(new Alcool(this));
    }
    public Array<Alcool> getAlcool(){
        return alcools;
    }
    public void setAttack( boolean a){
        attack = a;
    }
    public void setHit(boolean hit){
        isHit = hit;}
}
