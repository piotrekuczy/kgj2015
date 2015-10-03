package pl.edu.piotrekuczy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.edu.piotrekuczy.KgjGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.width = 1024;
//		config.height = 868;
		config.width = 640;
		config.height = 960;
		new LwjglApplication(new KgjGame(), config);
	}
}
