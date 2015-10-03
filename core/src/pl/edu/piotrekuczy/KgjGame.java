package pl.edu.piotrekuczy;

import com.badlogic.gdx.Game;

public class KgjGame extends Game {

	GameScreen gamescreen;

	@Override
	public void create() {

		gamescreen = new GameScreen(this);
		setScreen(gamescreen);
	}

}
