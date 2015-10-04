package pl.edu.piotrekuczy;

import com.badlogic.gdx.Game;

public class KgjGame extends Game {

	GameScreen gamescreen;
	MenuScreen menuscreen;
	IntroScreen introscreen;

	@Override
	public void create() {

		menuscreen = new MenuScreen(this);
		gamescreen = new GameScreen(this);
		introscreen = new IntroScreen(this);
		
		setScreen(introscreen);
//		setScreen(menuscreen);
//		setScreen(gamescreen);
	}

	public GameScreen getGamescreen() {
		return gamescreen;
	}

	public void setGamescreen(GameScreen gamescreen) {
		this.gamescreen = gamescreen;
	}

	public MenuScreen getMenuscreen() {
		return menuscreen;
	}

	public void setMenuscreen(MenuScreen menuscreen) {
		this.menuscreen = menuscreen;
	}

}
