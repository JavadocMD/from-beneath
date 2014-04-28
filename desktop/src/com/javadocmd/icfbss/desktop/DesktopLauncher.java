package com.javadocmd.icfbss.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.javadocmd.icfbss.IcfbssGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		 
		//config.x = 1705;
		//config.y = 25;
		config.width = 1440;
		config.height = 900;
		
		config.fullscreen = false;
		
		config.foregroundFPS = 60;
		
		new LwjglApplication(new IcfbssGame(), config);
	}
}
