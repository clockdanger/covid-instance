package com.damatooliveira.covid.instance.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.sprites.Bart;
import com.damatooliveira.covid.instance.game.sprites.Contaminado;
import com.damatooliveira.covid.instance.game.sprites.interatives.Door;
import com.damatooliveira.covid.instance.game.sprites.interatives.PhaseShift;
import com.damatooliveira.covid.instance.game.sprites.interatives.TapWater;
import com.damatooliveira.covid.instance.game.sprites.itens.Alcool;
import com.damatooliveira.covid.instance.game.sprites.itens.Spray;
import com.damatooliveira.covid.instance.game.sprites.itens.Virus;

public class WorldListener implements ContactListener {
    @Override
        public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){

            case GameMain.BART_BIT | GameMain.CONT_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.BART_BIT) {
                    ((Bart) fixA.getUserData()).setHit(true);
                }else{
                    ((Bart)fixB.getUserData()).setHit(true);
                }
                break;
            case GameMain.BART_BIT | GameMain.VIRUS_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.BART_BIT) {
                    ((Bart) fixA.getUserData()).hitInBody();
                    ((Virus) fixB.getUserData()).setToDestroy(true);
                }else {
                    ((Bart) fixB.getUserData()).hitInBody();
                    ((Virus) fixA.getUserData()).setToDestroy(true);
                }
                break;
            case GameMain.CONT_BIT | GameMain.WALL_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.CONT_BIT) {
                    ((Contaminado) fixA.getUserData()).reverseVelocity(true);
                    ((Contaminado) fixA.getUserData()).setPersecetution(false);
                }else{
                    ((Contaminado) fixB.getUserData()).reverseVelocity(true);
                    ((Contaminado) fixB.getUserData()).setPersecetution(false);
                }
                break;
            case GameMain.CONT_BIT | GameMain.OBJ_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.CONT_BIT) {
                    ((Contaminado) fixA.getUserData()).setPersecetution(false);
                    ((Contaminado) fixA.getUserData()).reverseVelocity(true);
                }else {
                    ((Contaminado) fixB.getUserData()).reverseVelocity(true);
                    ((Contaminado) fixB.getUserData()).setPersecetution(false);
                }
                break;
            case GameMain.CONT_BIT | GameMain.ALC_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.CONT_BIT) {
                    ((Contaminado) fixA.getUserData()).reverseVelocity(true);
                    ((Alcool) fixB.getUserData()).setToDestroy(true);
                }else {
                    ((Contaminado) fixB.getUserData()).reverseVelocity(true);
                    ((Alcool) fixA.getUserData()).setToDestroy(true);
                }
                break;
            case GameMain.VIRUS_BIT | GameMain.ALC_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.VIRUS_BIT) {
                    ((Virus) fixA.getUserData()).setToDestroy(true);
                    ((Alcool) fixB.getUserData()).setToDestroy(true);
                }else {
                    ((Virus) fixB.getUserData()).setToDestroy(true);
                    ((Alcool) fixA.getUserData()).setToDestroy(true);
                }
                break;
            case GameMain.BART_BIT | GameMain.SPRAY_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.SPRAY_BIT) {
                    ((Spray) fixA.getUserData()).setHabilited(true);
                }else {
                    ((Spray) fixB.getUserData()).setHabilited(true);
                }
                break;

            case GameMain.BART_BIT | GameMain.DOOR_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.DOOR_BIT) {
                    ((Door) fixA.getUserData()).setHabilited(true);
                }else {
                    ((Door) fixB.getUserData()).setHabilited(true);
                }
                break;
            case GameMain.BART_BIT | GameMain.TAP_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.TAP_BIT) {
                    ((TapWater) fixA.getUserData()).setHabilited(true);
                }else {
                    ((TapWater) fixB.getUserData()).setHabilited(true);
                }
                break;
            case GameMain.BART_BIT | GameMain.PHASE_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.PHASE_BIT) {
                    ((PhaseShift) fixA.getUserData()).setToDestroy(true);
                }else {
                    ((PhaseShift) fixB.getUserData()).setToDestroy(true);
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef){
            case GameMain.BART_BIT | GameMain.SPRAY_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.SPRAY_BIT) {
                    ((Spray) fixA.getUserData()).setHabilited(false);
                }else {
                    ((Spray) fixB.getUserData()).setHabilited(false);
                }
                break;
            case GameMain.BART_BIT | GameMain.DOOR_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.DOOR_BIT) {
                    ((Door) fixA.getUserData()).setHabilited(false);
                    ((Door) fixA.getUserData()).setToDestroy(false);
                }else {
                    ((Door) fixB.getUserData()).setHabilited(false);
                    ((Door) fixB.getUserData()).setToDestroy(false);

                }
                break;
            case GameMain.BART_BIT | GameMain.TAP_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.TAP_BIT) {
                    ((TapWater) fixA.getUserData()).setHabilited(false);
                    ((TapWater) fixA.getUserData()).setToDestroy(false);
                }else {
                    ((TapWater) fixB.getUserData()).setHabilited(false);
                    ((TapWater) fixB.getUserData()).setToDestroy(false);
                }
                break;
            case GameMain.BART_BIT | GameMain.CONT_BIT:
                if(fixA.getFilterData().categoryBits == GameMain.BART_BIT) {
                    ((Bart) fixA.getUserData()).setHit(false);
                }else{
                    ((Bart)fixB.getUserData()).setHit(false);
                }
                break;
            }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
