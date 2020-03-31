package com.damatooliveira.covid.instance.game.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.damatooliveira.covid.instance.game.GameMain;
import com.damatooliveira.covid.instance.game.screens.GameOver;

public class WorldLevelManagement {

    private TmxMapLoader maploader;
    private TiledMap map;
    private int level;
    public WorldLevelManagement(int level, GameMain gameMain){
        this.level = level;
        maploader = new TmxMapLoader();
        switch (level){
            case 1:
                map = maploader.load("covid_instance_map/map01.tmx");
                break;
            case 2:
                map = maploader.load("covid_instance_map/map02.tmx");
                break;
            case 3:
                map = maploader.load("covid_instance_map/map03.tmx");
                break;
            case 4:
                break;
        }
    }
    public TiledMap getWorldMap(){
        return map;
    }
}
