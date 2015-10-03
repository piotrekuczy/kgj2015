package pl.edu.piotrekuczy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class GameScreen implements Screen {

	KgjGame game;

	SpriteBatch batch;
	ShapeRenderer shpr = new ShapeRenderer();
	OrthographicCamera camera;
	Viewport viewport;

	Boolean fullscreen = false;
	private float rotationSpeed = 0.5f;

	Texture gameMask;
	GlyphLayout glay;
	BitmapFont font;

	SkeletonRenderer sr;
	SkeletonRendererDebug dr;

	Cat myCat;

	int floor;

	public GameScreen(KgjGame game) {
		this.game = game;
	}

	@Override
	public void show() {

		System.out.println("show method of gamescreen");
		batch = new SpriteBatch();
		camera = new OrthographicCamera(640, 960);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		viewport = new FitViewport(640, 960, camera);
		camera.update();
		gameMask = new Texture(Gdx.files.internal("bitmaps/gameMask.png"));
		gameMask.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("fonts/mariofont.fnt"));
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(0.99f);
		glay = new GlyphLayout(font, "score");

		// spine

		sr = new SkeletonRenderer();
		dr = new SkeletonRendererDebug();
		dr.setBoundingBoxes(false);
		dr.setRegionAttachments(false);

		// cat

		// random pos

		floor = MathUtils.random(0, 3);
		System.out.println(floor);

		switch (floor) {
		case 0:
			myCat = new Cat(new Vector2(320, 10));
			break;
		case 1:
			myCat = new Cat(new Vector2(320, 230));
			break;
		case 2:
			myCat = new Cat(new Vector2(320, 450));
			break;
		case 3:
			myCat = new Cat(new Vector2(320, 670));
			break;
		}

	}

	@Override
	public void render(float delta) {
		cameraInput();

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		dr.getShapeRenderer().setProjectionMatrix(camera.combined);

		Gdx.graphics.getGL20().glClearColor(1, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch.begin();
		sr.draw(batch, myCat.getSkeleton());
		batch.draw(gameMask, 0, 0);
		font.draw(batch, glay, (viewport.getWorldWidth() / 2) - (glay.width / 2), 940);
		batch.end();
		// dr.draw(myCat.getSkeleton());
		myCat.update();
		// myCat.draw();

		// render collision circle
		shpr.setProjectionMatrix(batch.getProjectionMatrix());
		shpr.begin(ShapeType.Line);
		shpr.setColor(1, 1, 1, 1f);
		shpr.circle(myCat.getCatCircle().x, myCat.getCatCircle().y, myCat.getCatRad());
		shpr.end();

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
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.rotate(-rotationSpeed, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			camera.rotate(rotationSpeed, 0, 0, 1);
		}

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

}
