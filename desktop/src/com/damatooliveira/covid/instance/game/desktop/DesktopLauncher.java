package com.damatooliveira.covid.instance.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.damatooliveira.covid.instance.game.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width  = 1024;
		config.height = 512;
		config.title = "Covid Instance";
		config.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new GameMain(), config);
	}
	/*gradlew desktop:dist*/
}
