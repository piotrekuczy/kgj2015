package pl.edu.piotrekuczy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class KgjGame extends Game {

	GameScreen gamescreen;
	MenuScreen menuscreen;
	IntroScreen introscreen;

	// audio
	Sound sound, sound2;
	long id1, id2;

	@Override
	public void create() {

		menuscreen = new MenuScreen(this);
		gamescreen = new GameScreen(this);
		introscreen = new IntroScreen(this);

		// sound
		sound = Gdx.audio.newSound(Gdx.files.internal("audio/intro2.wav"));
		sound2 = Gdx.audio.newSound(Gdx.files.internal("audio/intro1.wav"));
		id1 = sound.loop();

		setScreen(introscreen);
		// setScreen(menuscreen);
		// setScreen(gamescreen);
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

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public long getId1() {
		return id1;
	}

	public void setId1(long id1) {
		this.id1 = id1;
	}

	public Sound getSound2() {
		return sound2;
	}

	public void setSound2(Sound sound2) {
		this.sound2 = sound2;
	}

	public long getId2() {
		return id2;
	}

	public void setId2(long id2) {
		this.id2 = id2;
	}

}
