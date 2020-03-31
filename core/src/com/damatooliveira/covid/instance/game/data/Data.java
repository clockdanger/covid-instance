package com.damatooliveira.covid.instance.game.data;

import com.badlogic.gdx.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Data implements Serializable {
	
	public static final long serialVersionUID = 1L;

	private ArrayList<GamePlayer> playersAndScore;
	public Data() {
		playersAndScore = new ArrayList<>();
	}
	
	public boolean addScore(String name, int level, int h, int m, int s) {

		if ( playersAndScore.size() == 10 ) {
			GamePlayer p = getTopDown().get(playersAndScore.size() - 1);
			GamePlayer p2 = new GamePlayer(name, level, h, m, s);

			if ( getBesters(p.getLevel(), getTotalTime(p)) > getBesters(level, getTotalTime(p2)) ) {
				return false;
			} else {
				playersAndScore.remove(p);
			}
		}
		
		this.playersAndScore.add(new GamePlayer(name, level, h, m, s));
		return true;
	}

	public ArrayList<GamePlayer> getPlayersAndScore() {
		return playersAndScore;
	}

	public ArrayList<GamePlayer> getTopDown() {

		ArrayList<GamePlayer> listAtual = (ArrayList<GamePlayer>) getPlayersAndScore().clone();
		ArrayList<GamePlayer> newList = new ArrayList<>();
		double[] bersters = new double[listAtual.size()];

		for ( int i = 0; i < listAtual.size(); i++) {
			bersters[i] = getBesters(listAtual.get(i).getLevel(), getTotalTime(listAtual.get(i)));
		}

		Arrays.sort(bersters);
		for ( int i = bersters.length - 1; i >= 0; i--) {

			for ( int j = listAtual.size() -1 ; j >= 0; j--) {
				if ( bersters[i] == getBesters(listAtual.get(j).getLevel(), getTotalTime(listAtual.get(j))) ) {
					newList.add(listAtual.get(j));
					listAtual.remove(listAtual.get(j));
				}
			}
		}


		return newList;

	}

	private long getTotalTime(GamePlayer player){
		long l = player.getHora()*3600 +
				player.getMinuto()*60+
				player.getSegundo();
		return l;
	}

	private double getBesters(int level, long totalTime) {

		return ( (level * 99) + (totalTime / 36000f) );

	}
}

