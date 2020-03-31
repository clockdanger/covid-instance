package com.damatooliveira.covid.instance.game.data;

import java.io.Serializable;

public class GamePlayer implements Serializable {

    public static final long serialVersionUID = 1L;

    private int hora, minuto, segundo;
    private String name;
    private int level;

    public GamePlayer(String name, int level, int hora, int minuto, int segundo) {
        this.name = name;
        this.level = level;
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }

    // Getters and Setters
    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
