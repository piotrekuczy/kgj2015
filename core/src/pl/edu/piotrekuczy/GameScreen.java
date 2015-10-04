package pl.edu.piotrekuczy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class GameScreen implements Screen, InputProcessor {

	KgjGame game;

	// renderers
	SpriteBatch batch;
	ShapeRenderer shpr = new ShapeRenderer();
	SkeletonRenderer sr;
	SkeletonRendererDebug dr;

	// camera & vport
	OrthographicCamera camera;
	Viewport viewport;
	Boolean fullscreen = false;
	private float rotationSpeed = 0.5f;

	// bitmaps
	Texture gameMask;
	Texture worlds;

	// fonts
	GlyphLayout glay;
	BitmapFont font;

	// killer
	int nowyX;
	private Array<Killer> killers;

	Killer sampleKiller;
	// number of killers
	int numberOfKillers;

	// cat
	Cat myCat;
	float catVel = 300f;
	float jumpForce = 800f;
	int catStartXright = 0;
	int catStartXleft = 700;
	int ghostOffset = 150;

	// gameplay variables
	// gravity
	float myGravity = -330f;
	// floor number
	int floor;
	// booleans
	boolean jump = false;
	boolean gameOver = false;

	// timers
	float scoreTimer;

	// SPINE gameOver

	TextureAtlas overAtlas;
	SkeletonJson overJson;
	SkeletonData overSkeletonData;
	Skeleton overSkeleton;
	Animation overInAnimation, overIdleAnimation, overOutAnimation;
	float animationTime = 0;
	AnimationState state;
	
	public GameScreen(KgjGame game) {
		this.game = game;
	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(this);

		System.out.println("show method of gamescreen");
		batch = new SpriteBatch();
		camera = new OrthographicCamera(640, 960);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		viewport = new FitViewport(640, 960, camera);
		camera.update();

		// bitmaps
		gameMask = new Texture(Gdx.files.internal("bitmaps/gameMask.png"));
		gameMask.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		worlds = new Texture(Gdx.files.internal("bitmaps/worlds.png"));
		worlds.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// fonts
		font = new BitmapFont(Gdx.files.internal("fonts/mariofont.fnt"));
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(0.99f);
		glay = new GlyphLayout(font, "score");
		// glay = new GlyphLayout(font, "score", new Color(1, 1, 1, 1), 300,
		// Align.left, true);

		// spine
		sr = new SkeletonRenderer();
		dr = new SkeletonRendererDebug();
		dr.setBoundingBoxes(false);
		dr.setRegionAttachments(false);

		// spine

		overAtlas = new TextureAtlas(Gdx.files.internal("spine/gui/gameover.atlas"));
		overJson = new SkeletonJson(overAtlas);
		overSkeletonData = overJson.readSkeletonData(Gdx.files.internal("spine/gui/gameover.json"));
		overSkeleton = new Skeleton(overSkeletonData);
		overInAnimation = overSkeletonData.findAnimation("in");
		overIdleAnimation = overSkeletonData.findAnimation("idle");
		overOutAnimation = overSkeletonData.findAnimation("out");
		overSkeleton.getRootBone().setScale(1f);
		overSkeleton.setPosition(310, -50);

		overSkeleton.updateWorldTransform();
		AnimationStateData stateData = new AnimationStateData(overSkeletonData);
		state = new AnimationState(stateData);
		state.setAnimation(0, "in", false);
		state.addAnimation(0, "idle",false, 0);
		state.addAnimation(0, "out",false, 0);
		
		// array of killers
		killers = new Array<Killer>();

		// make cat outside a screen
		myCat = new Cat(new Vector2(-200, 200), 0, 0, 0);

		// random floor
		newFloor();
	}

	public void newFloor() {
		if (!gameOver) {
			if (killers.size > 0) {
				killers.clear();
			}
			// System.out.println("KILLERS ARRAY SIZE= " + killers.size);
			// random floor
			floor = MathUtils.random(0, 3);
			// floor = 0;
			switch (floor) {
			case 0:
				// losuj czy mają biec z lewej do prawej czy z prawej do lewej
				int random = MathUtils.random(0, 1);
				if (random == 0) {
					// losowanie czy z lewej do prawej
					myCat.setCatPos(new Vector2(catStartXright, 10));
					myCat.getVelocity().x = catVel;
					myCat.getSkeleton().setFlipX(false);
					myCat.getGhostSkeleton().setFlipX(false);
					myCat.setGhostOffset(ghostOffset);
				} else {
					// czy z prawej do lewej
					myCat.setCatPos(new Vector2(catStartXleft, 10));
					myCat.getVelocity().x = -catVel;
					myCat.getSkeleton().setFlipX(true);
					myCat.getGhostSkeleton().setFlipX(true);
					myCat.setGhostOffset(-ghostOffset);
				}
				myCat.setMyGravity(myGravity);
				generateKillers(10 + 10);
				break;
			case 1:
				// losuj czy mają biec z lewej do prawej czy z prawej do lewej
				int random1 = MathUtils.random(0, 1);
				if (random1 == 0) {
					// losowanie czy z lewej do prawej
					myCat.setCatPos(new Vector2(catStartXright, 230));
					myCat.getVelocity().x = catVel;
					myCat.getSkeleton().setFlipX(false);
					myCat.getGhostSkeleton().setFlipX(false);
					myCat.setGhostOffset(ghostOffset);
				} else {
					// czy z prawej do lewej
					myCat.setCatPos(new Vector2(catStartXleft, 230));
					myCat.getVelocity().x = -catVel;
					myCat.getSkeleton().setFlipX(true);
					myCat.getGhostSkeleton().setFlipX(true);
					myCat.setGhostOffset(-ghostOffset);
				}
				myCat.setMyGravity(myGravity);
				generateKillers(230 + 10);
				break;
			case 2:
				// losuj czy mają biec z lewej do prawej czy z prawej do lewej
				int random2 = MathUtils.random(0, 1);
				if (random2 == 0) {
					// losowanie czy z lewej do prawej
					myCat.setCatPos(new Vector2(catStartXright, 450));
					myCat.getVelocity().x = catVel;
					myCat.getSkeleton().setFlipX(false);
					myCat.getGhostSkeleton().setFlipX(false);
					myCat.setGhostOffset(ghostOffset);
				} else {
					// czy z prawej do lewej
					myCat.setCatPos(new Vector2(catStartXleft, 450));
					myCat.getVelocity().x = -catVel;
					myCat.getSkeleton().setFlipX(true);
					myCat.getGhostSkeleton().setFlipX(true);
					myCat.setGhostOffset(-ghostOffset);
				}
				myCat.setMyGravity(myGravity);
				generateKillers(450 + 10);
				break;
			case 3:
				// losuj czy mają biec z lewej do prawej czy z prawej do lewej
				int random3 = MathUtils.random(0, 1);
				if (random3 == 0) {
					// losowanie czy z lewej do prawej
					myCat.setCatPos(new Vector2(catStartXright, 670));
					myCat.getVelocity().x = catVel;
					myCat.getSkeleton().setFlipX(false);
					myCat.getGhostSkeleton().setFlipX(false);
					myCat.setGhostOffset(ghostOffset);
				} else {
					// czy z prawej do lewej
					myCat.setCatPos(new Vector2(catStartXleft, 670));
					myCat.getVelocity().x = -catVel;
					myCat.getSkeleton().setFlipX(true);
					myCat.getGhostSkeleton().setFlipX(true);
					myCat.setGhostOffset(-ghostOffset);
				}
				myCat.setMyGravity(myGravity);
				generateKillers(670 + 10);
				break;
			}
		}
	}

	public void generateOneKiller(int floor, int kilerId) {
		// ZROBIC random czy kolec gora czy kolec dol i zrobc dwie opcje
		int old_position = 0;
		int position = 50;
		for (int i = 1; i <= kilerId; i++) {
			int random = MathUtils.random(0, 1);
			// int position = MathUtils.random(i* 4/kilerId *100, i * 4/kilerId
			// * 100 + 20);
			if (random == 0)
				old_position = position + 100;
			else
				old_position = position + 10;
			position = MathUtils.random(old_position, old_position + 100);
			if (position < 540) {
				// System.out.println(position);
				if (random == 0) {
					// spel wystajacy z dolu
					killers.add(new Killer(new Vector2(position, floor - 1), 5, false));
				} else {
					// spole zwisajace z gory
					killers.add(new Killer(new Vector2(position, floor + 200), 5, true));
				}
			}
		}
	}

	public void generateKillers(int floor) {
		numberOfKillers = MathUtils.random(0, 7);
		// System.out.println("NUMBER OF KILLERS = " + (numberOfKillers + 1));
		generateOneKiller(floor, 7);
		// switch (numberOfKillers) {
		// case 0:
		//
		// break;
		// case 1:
		// generateOneKiller(floor, 4 );
		// break;
		// case 2:
		// generateOneKiller(floor, 4 );
		// break;
		// case 3:
		// generateOneKiller(floor, 4 );
		// break;
		// }
	}

	@Override
	public void render(float delta) {
		if (!gameOver) {
			scoreTimer += 0.1f;
			scoreTimer = (float) ((double) Math.round(scoreTimer * 10d) / 10d);
		}
		// System.out.println(scoreTimer);
		glay.setText(font, scoreTimer + "", new Color(1, 1, 1, 1), 800, Align.left, true);

		cameraInput();

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		dr.getShapeRenderer().setProjectionMatrix(camera.combined);

		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		shpr.begin(ShapeType.Filled);
		shpr.setColor(1, 0, 0, 1);
		shpr.rect(0, 0, 640, 960);
		shpr.end();
		// batch
		batch.begin();
		// worlds
		batch.draw(worlds, 0, 0);
		// spine
		sr.draw(batch, myCat.getGhostSkeleton());
		sr.draw(batch, myCat.getSkeleton());
		// screen mask
		batch.draw(gameMask, 0, 0);
		batch.end();

		// spine dabug
		// dr.draw(myCat.getSkeleton());

		// update cat logic
		myCat.update(delta);

		shpr.setProjectionMatrix(batch.getProjectionMatrix());

		// cat collision object
//		shpr.begin(ShapeType.Line);
//		shpr.setColor(1, 1, 1, 1f);
//		shpr.circle(myCat.getCatCircle().x, myCat.getCatCircle().y, myCat.getCatRad());
//		shpr.end();

		// killers
		shpr.begin(ShapeType.Filled);
		for (Killer killer : killers) {
			if (killer.isUp()) {
				shpr.setColor(0, 0, 0, 1f);
				// jesli killera up jest true to rysuj go do gory
				shpr.triangle(killer.getKillerPos().x, killer.getKillerPos().y, killer.getKillerPos().x + 50,
						killer.getKillerPos().y, killer.getKillerPos().x + 25, killer.getKillerPos().y - 50);
			} else {
				shpr.setColor(0, 0, 0, 1f);
				// jesli killera up to false to rysuj go do dolu
				shpr.triangle(killer.getKillerPos().x, killer.getKillerPos().y, killer.getKillerPos().x + 50,
						killer.getKillerPos().y, killer.getKillerPos().x + 25, killer.getKillerPos().y + 50);
			}
		}
		shpr.end();

		// killers collision objects
//		shpr.begin(ShapeType.Line);
//		shpr.setColor(1, 1, 1, 1f);
//		for (Killer killer : killers) {
//			shpr.circle(killer.getKillerCircle().x + 25, killer.getKillerCircle().y, killer.getKillerRadius());
//		}
//		shpr.end();

		// zaslaniacze
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shpr.begin(ShapeType.Filled);
		shpr.setColor(0, 0, 0, 0.8f);
		switch (floor) {
		case 0:
			// zasłon wszystko poza 0
			shpr.rect(0, 220, 640, 760);
			break;
		case 1:
			// zasłon wszystko poza 0
			shpr.rect(0, 0, 640, 225);
			shpr.rect(0, 440, 640, 760);
			break;
		case 2:
			// zasłon wszystko poza 0
			shpr.rect(0, 0, 640, 440);
			shpr.rect(0, 660, 640, 760);
			break;
		case 3:
			// zasłon wszystko poza 0
			shpr.rect(0, 0, 640, 660);
			break;
		}

		shpr.end();

		// score
		batch.begin();
		font.draw(batch, glay, (viewport.getWorldWidth() / 2) - 100, 940);
		batch.end();
		
		// gameover
		if(gameOver){
			
		state.update(Gdx.graphics.getDeltaTime());
		state.apply(overSkeleton);
		overSkeleton.updateWorldTransform();
		batch.begin();
		sr.draw(batch, overSkeleton);
		batch.end();
		
			if (state.getCurrent(0) == null) {
				gameOver = false;
				game.getMenuscreen().setBestScore(scoreTimer);
				scoreTimer = 0;
				game.setScreen(game.getMenuscreen());
				
			}
		}
		
		checkcollisions();
		checkBounds();

	}

	public void checkBounds() {
		if (!gameOver) {
			if (myCat.getVelocity().x > 0) {
				if (myCat.getCatPos().x >= 700) {
					// System.out.println("END");
					newFloor();
				}
			} else {
				if (myCat.getCatPos().x <= -100) {
					// System.out.println("END");
					newFloor();
				}
			}
		}
	}

	public void checkcollisions() {
		for (Killer killer : killers) {
			if (myCat.getCatCircle().overlaps(killer.getKillerCircle())) {
				gameOver();
			}
		}
	}

	public void gameOver() {
		gameOver = true;
//		System.out.println("GAME OVER!");
		// usun killery
		if (killers.size > 0) {
			killers.clear();
		}
		// schowaj kota
		myCat.setCatPos(new Vector2(-200, -200));
		myCat.setVelocity(new Vector2(0, 0));
		// pokaz gameover
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	// ------- INPUT PROCESSOR

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
		if (!myCat.isJump()) {
			// System.out.println("TACZDALN");
			jump = true;
			myCat.getVelocity().y = jumpForce;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// System.out.println("TACZUP");
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

}
