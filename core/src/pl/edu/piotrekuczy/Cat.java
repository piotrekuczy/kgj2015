package pl.edu.piotrekuczy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class Cat {

	TextureAtlas atlas;
	SkeletonJson json;
	SkeletonData skeletonData;
	Skeleton skeleton;
	Animation runAnimation;
	float animationTime = 0;

	Vector2 catPos;
	Vector2 velocity;
	float catRad = 40f;

	private Circle catCircle;

	public Cat(Vector2 catPos) {

		this.catPos = catPos;
		catCircle = new Circle(catPos.x, catPos.y+30, catRad);

		// spine

		atlas = new TextureAtlas(Gdx.files.internal("spine/ingame/cat.atlas"));
		json = new SkeletonJson(atlas);
		skeletonData = json.readSkeletonData(Gdx.files.internal("spine/ingame/cat.json"));
		skeleton = new Skeleton(skeletonData);
		runAnimation = skeletonData.findAnimation("run");

		skeleton.getRootBone().setScale(0.7f);
		skeleton.setPosition(catPos.x, catPos.y);
		// skeleton.setColor(new Color(0, 1, 1, 1));
		skeleton.updateWorldTransform();

	}

	public void update() {
//		System.out.println("cat update");
		animationTime += Gdx.graphics.getDeltaTime();
		runAnimation.apply(skeleton, 0, animationTime, true, null);
		skeleton.updateWorldTransform();

	}

	public void draw() {
//		System.out.println("cat draw");
	}

	public Circle getCatCircle() {
		return catCircle;
	}

	public void setCatCircle(Circle catCircle) {
		this.catCircle = catCircle;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getCatRad() {
		return catRad;
	}

	public void setCatRad(float catRad) {
		this.catRad = catRad;
	}

	public Skeleton getSkeleton() {
		return skeleton;
	}

	public void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

}
