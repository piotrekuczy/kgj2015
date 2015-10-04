package pl.edu.piotrekuczy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

public class MenuScreen implements Screen, InputProcessor {

	KgjGame game;

	// renderers
	SpriteBatch batch;
	SkeletonRenderer sr;

	// camera & vport
	OrthographicCamera camera;
	Viewport viewport;
	Boolean fullscreen = false;
	private float rotationSpeed = 0.5f;

	// bitmaps
	Texture winietka, credits;

	// fonts
	GlyphLayout bestScoreTxt,bestScoreValue;
	BitmapFont font;

	float bestScore;

	// SPINE logomenu

	TextureAtlas logoAtlas;
	SkeletonJson logoJson;
	SkeletonData logoSkeletonData;
	Skeleton logoSkeleton;
	Animation logoInAnimation, logoIdleAnimation, logoOutAnimation;
	float animationTime = 0;
	AnimationState state;

	boolean toTheEnd = false;

	public MenuScreen(KgjGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		toTheEnd = false;
		System.out.println("show method of menuscreen");
		Gdx.input.setInputProcessor(this);
		batch = new SpriteBatch();
		game.getSound().stop();
		game.getSound().loop();
		// bitmaps
		winietka = new Texture(Gdx.files.internal("bitmaps/menuTlo.jpg"));
		winietka.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		credits = new Texture(Gdx.files.internal("bitmaps/credits.png"));
		credits.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// spine
		sr = new SkeletonRenderer();
		camera = new OrthographicCamera(640, 960);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		viewport = new FitViewport(640, 960, camera);
		camera.update();

		// fonts
		font = new BitmapFont(Gdx.files.internal("fonts/mariofont.fnt"));
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(0.4f);
		bestScoreTxt = new GlyphLayout(font, "best score:");
		bestScoreValue = new GlyphLayout(font, ""+bestScore);

		// spine

		logoAtlas = new TextureAtlas(Gdx.files.internal("spine/gui/logodomenu.atlas"));
		logoJson = new SkeletonJson(logoAtlas);
		logoSkeletonData = logoJson.readSkeletonData(Gdx.files.internal("spine/gui/logodomenu.json"));
		logoSkeleton = new Skeleton(logoSkeletonData);
		logoInAnimation = logoSkeletonData.findAnimation("in");
		logoIdleAnimation = logoSkeletonData.findAnimation("idle");
		logoOutAnimation = logoSkeletonData.findAnimation("out");
		logoSkeleton.getRootBone().setScale(1f);
		logoSkeleton.setPosition(325, 90);

		logoSkeleton.updateWorldTransform();
		AnimationStateData stateData = new AnimationStateData(logoSkeletonData);
		state = new AnimationState(stateData);
		state.setAnimation(0, "in", false);
		state.addAnimation(0, "idle", true, 0);
	}

	@Override
	public void render(float delta) {
		cameraInput();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		state.update(Gdx.graphics.getDeltaTime());
		state.apply(logoSkeleton);
		logoSkeleton.updateWorldTransform();
		batch.begin();
		batch.draw(winietka, 0, 0);
		batch.draw(credits, 0, -230);
		sr.draw(batch, logoSkeleton);
		// score
		font.draw(batch, bestScoreTxt, (viewport.getWorldWidth() / 2) - 120, 80);
		font.draw(batch, bestScoreValue, (viewport.getWorldWidth() / 2)-20 , 40);
		batch.end();

		if (toTheEnd && state.getCurrent(0) == null) {
			game.setScreen(game.getGamescreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// wyl loopa
		game.getSound().stop(game.getId1());
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void cameraInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
			fullscreen = !fullscreen;
			if (fullscreen) {
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width,
						Gdx.graphics.getDesktopDisplayMode().height, fullscreen);
			} else {
				Gdx.graphics.setDisplayMode(640, 960, false);
			}
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//			camera.zoom += 0.02;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
//			camera.zoom -= 0.02;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//			camera.translate(3, 0, 0);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//			camera.translate(-3, 0, 0);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//			camera.translate(0, 3, 0);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//			camera.translate(0, -3, 0);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//			camera.rotate(-rotationSpeed, 0, 0, 1);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
//			camera.rotate(rotationSpeed, 0, 0, 1);
//		}

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// System.out.println("KLIK");
		if (!toTheEnd) {
			toTheEnd = true;
			state.setAnimation(0, "out", false);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public float getBestScore() {
		return bestScore;
	}

	public void setBestScore(float bestScore) {
		this.bestScore = bestScore;
	}

}
