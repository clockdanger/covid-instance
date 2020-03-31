package com.damatooliveira.covid.instance.game.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.damatooliveira.covid.instance.game.sprites.Contaminado;

public class ControleContaminado {
    Contaminado contaminado;
    private Vector2 posCont;
    private Vector2 posPlayer;
    private float statetime;
    private Vector2 diference;

    public ControleContaminado(Contaminado contaminado) {
        this.contaminado = contaminado;
        posCont = new Vector2();
        posPlayer = new Vector2();
        diference = new Vector2();
        statetime = 0;
        posCont.set(contaminado.getX(), contaminado.getY());
        posPlayer.set(contaminado.getAlvoPosition());
    }

    public void movementStandart(float dt) {
        statetime += dt;
        if (MathUtils.ceil(statetime) >= MathUtils.random(5, 25)) {
            switch (MathUtils.random(1, 5)) {
                case 1:
                    contaminado.b2body.setLinearVelocity(-0.1f, 0);
                    break;
                case 2:
                    contaminado.b2body.setLinearVelocity(0, -0.1f);
                    break;
                case 3:
                    contaminado.b2body.setLinearVelocity(0.1f, 0);
                    break;
                case 4:
                    contaminado.b2body.setLinearVelocity(0, -0.1f);
                    break;
                case 5:
                    contaminado.fireVirus();
                    break;
            }
            statetime = 0;
        }
    }

    public void persecutionPoint(float dt) {
        statetime += dt;
        posCont.set(contaminado.getX(), contaminado.getY());
        posPlayer.set(contaminado.getAlvoPosition());
        diference.set(posPlayer.x - posCont.x, posPlayer.y - posCont.y);
        Vector2 velocity = new Vector2(0, 0);
        if (diference.x > 0) {
            //Gdx.app.log("l",diference.x +" e "+diference.y);
            //anda para direita
            velocity.x = 0.2f;
        } else if (diference.x < 0) {
            //anda para esquerda
            velocity.x = -0.2f;
        }
        if (diference.y > 0 && !(MathUtils.ceil(diference.y * 100) == 0)) {
            //anda para cima
            velocity.y = 0.2f;
        } else if (diference.y < 0 && !(MathUtils.floor(diference.y * 100) == 0)) {
            //anda para baixo
            velocity.y = -0.2f;
        }
        if (MathUtils.round(diference.x * 100) == 0) {
            velocity.x = 0;
        }
        if (MathUtils.round(diference.y * 100) == 0) {
            velocity.y = 0;
        }
        contaminado.b2body.setLinearVelocity(velocity.x, velocity.y);

        if (MathUtils.ceil(statetime) >= MathUtils.random(5, 25)) {
            if (MathUtils.random(0, 5) == 5) {
                contaminado.fireVirus();
                statetime = 0;
            }
        }
    }
}
