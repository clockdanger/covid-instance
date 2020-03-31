package com.damatooliveira.covid.instance.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.PlayScreen;
import com.damatooliveira.covid.instance.game.sprites.Contaminado;
import com.damatooliveira.covid.instance.game.sprites.interatives.Door;
import com.damatooliveira.covid.instance.game.sprites.interatives.PhaseShift;
import com.damatooliveira.covid.instance.game.sprites.interatives.TapWater;
import com.damatooliveira.covid.instance.game.sprites.itens.Spray;
import com.damatooliveira.covid.instance.game.sprites.itens.Virus;

public class WorldCreator {

    private Array<Contaminado> contaminados;
    private Array<Spray> sprays;
    private Array <Rectangle> cortinas;
    private Array <Door> doors;
    private Array <TapWater> taps;
    private Array  <Virus> virus;
    private Array <PhaseShift> phaseShifts;
    public WorldCreator(PlayScreen playScreen){

        World world = playScreen.getWorld();
        TiledMap map = playScreen.getMap();
        //colisores
        //configurações do corpo
        BodyDef bdef = new BodyDef();
        //verificar a disposição dos conteiners
        PolygonShape polyshap = new PolygonShape();
        //configurações dos objetos montados ouu imóveis
        FixtureDef fixdef = new FixtureDef();
        //corpo à ser adicionado ao mundo
        Body body;

        contaminados = new Array<Contaminado>();
        sprays = new Array<Spray>();
        cortinas = new Array<Rectangle>();
        doors = new Array<Door>();
        taps = new Array<TapWater>();
        virus = new Array<Virus>();
        phaseShifts = new Array<PhaseShift>();

        //para todos object no layer 3 (chão) que tenha tipo de retângulo faça
       for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/GameMain.PPM,
                    (rectangle.getY() + rectangle.getHeight()/2)/GameMain.PPM);

            //adicionando objeto ao mundo
            body = world.createBody(bdef);
            polyshap.setAsBox((rectangle.getWidth()/2)/GameMain.PPM, (rectangle.getHeight()/2)/GameMain.PPM);
            fixdef.shape = polyshap;
            fixdef.filter.categoryBits = GameMain.WALL_BIT;
       ///     fixdef.filter.maskBits = GameMain.BART_BIT;
            body.createFixture(fixdef);
        }
       //para todos object no layer 4 (cômodos) que tenha tipo de retângulo faça
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/GameMain.PPM,
                    (rectangle.getY() + rectangle.getHeight()/2)/GameMain.PPM);

            //adicionando objeto ao mundo
            body = world.createBody(bdef);
            polyshap.setAsBox((rectangle.getWidth()/2)/GameMain.PPM, (rectangle.getHeight()/2)/GameMain.PPM);
            fixdef.shape = polyshap;
            fixdef.filter.categoryBits = GameMain.OBJ_BIT;
            ///     fixdef.filter.maskBits = GameMain.BART_BIT;
            body.createFixture(fixdef);
        }
       //get our contaminateds in map and add to world
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
           contaminados.add(new Contaminado(playScreen,rectangle.getX(),rectangle.getY()));
        }
        //get our itens in map and add to world
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            sprays.add(new Spray(playScreen,(rectangle.getX() + rectangle.getWidth()/2),
                    (rectangle.getY() + rectangle.getHeight()/2)));
        }
        //get our cortines rects
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            rectangle.set(rectangle.getX()/GameMain.PPM,rectangle.getY()/GameMain.PPM,
                    rectangle.getWidth()/GameMain.PPM,rectangle.getHeight()/GameMain.PPM);
            cortinas.add(rectangle);
        }
        //set our doors
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            doors.add(new Door(playScreen,(rectangle.getX() + rectangle.getWidth()/2),
                    (rectangle.getY() + rectangle.getHeight()/2)));
        }
        //set our taps
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            taps.add(new TapWater(playScreen,(rectangle.getX() + rectangle.getWidth()/2),
                    (rectangle.getY() + rectangle.getHeight()/2)));
        }
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            virus.add(new Virus(playScreen,(rectangle.getX() + rectangle.getWidth()/2),
                    (rectangle.getY() + rectangle.getHeight()/2)));
        }
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            String name = ((RectangleMapObject) object).getName();
            int phase = Integer.parseInt(name);
            //tipo de corpo estático
            phaseShifts.add(new PhaseShift(playScreen,phase,(rectangle.getX() + rectangle.getWidth()/2),
                    (rectangle.getY() + rectangle.getHeight()/2)));
            Gdx.app.log("Name ",name);
            System.out.println(phase);
        }
        //player position
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            //tipo de corpo estático
            playScreen.getPlayer().b2body.setTransform((rectangle.getX() + rectangle.getWidth()/2)/GameMain.PPM,
                    (rectangle.getY() + rectangle.getHeight()/2)/GameMain.PPM,0);
        }
    }

    public Array<Contaminado> getContaminados() {
        return contaminados;
    }
    public Array<Spray> getSprays() {
        return sprays;
    }
    public Array<Rectangle> getCortinasRect() {
        return cortinas;
    }
    public Array<Door> getDoors(){
        return doors;
    }
    public Array<TapWater> getTaps(){
        return taps;
    }
    public Array<PhaseShift> getPhaseShifts(){
        return phaseShifts;
    }
    public Array <Virus> getStaticVirus(){
        return virus;
    }
}
