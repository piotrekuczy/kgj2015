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

	TextureAtlas catAtlas, ghostAtlas;
	SkeletonJson catJson, ghostJson;
	SkeletonData catSkeletonData, ghostSkeletonData;
	Skeleton catSkeleton, ghostSkeleton;
	Animation catRunAnimation, ghostRunAnimation;
	float animationTime = 0;

	Vector2 catPos;
	int catGround;
	Vector2 velocity;
	float catRad = 40f;
	int ghostOffset;
	float myGravity;
	boolean jump;

	private Circle catCircle;

	public Cat(Vector2 catPos, float vel, int ghostOffset, float myGravity) {

		this.catPos = catPos;
		catGround = (int) catPos.y;
		this.velocity = new Vector2(vel, 0);
		catCircle = new Circle(catPos.x, catPos.y + 30, catRad);

		this.ghostOffset = ghostOffset;
		this.myGravity = myGravity;

		// spine

		// cat
		catAtlas = new TextureAtlas(Gdx.files.internal("spine/ingame/cat.atlas"));
		catJson = new SkeletonJson(catAtlas);
		catSkeletonData = catJson.readSkeletonData(Gdx.files.internal("spine/ingame/cat.json"));
		catSkeleton = new Skeleton(catSkeletonData);
		catRunAnimation = catSkeletonData.findAnimation("run");
		catSkeleton.getRootBone().setScale(0.7f);
		catSkeleton.setPosition(catPos.x, catPos.y);
		// skeleton.setColor(new Color(0, 1, 1, 1));
		catSkeleton.updateWorldTransform();

		// ghost
		ghostAtlas = new TextureAtlas(Gdx.files.internal("spine/ingame/ghost.atlas"));
		ghostJson = new SkeletonJson(ghostAtlas);
		ghostSkeletonData = ghostJson.readSkeletonData(Gdx.files.internal("spine/ingame/ghost.json"));
		ghostSkeleton = new Skeleton(ghostSkeletonData);
		ghostRunAnimation = ghostSkeletonData.findAnimation("fly");
		ghostSkeleton.getRootBone().setScale(0.7f);
		ghostSkeleton.setPosition(catPos.x - ghostOffset, catPos.y + 20);
		// ghostSkeleton.setColor(new Color(0, 1, 1, 1));
		ghostSkeleton.updateWorldTransform();

	}

	public void update(float delta) {

		animationTime += Gdx.graphics.getDeltaTime();
		// cat
		catRunAnimation.apply(catSkeleton, 0, animationTime, true, null);
		catSkeleton.updateWorldTransform();
		// ghost
		ghostRunAnimation.apply(ghostSkeleton, 0, animationTime, true, null);
		ghostSkeleton.updateWorldTransform();

		// cat
		catPos.x += velocity.x * delta;
		catPos.y += velocity.y * delta;
		catCircle.x = catPos.x;
		catCircle.y = catPos.y + 30;
		catSkeleton.setPosition(catPos.x, catPos.y);
		// ghost
		ghostSkeleton.setPosition(catPos.x - ghostOffset, catPos.y + 20 * delta);

		// gravity
		velocity.y += myGravity * delta * 10;
		
		// ground check
		if (catPos.y <= catGround){
			catPos.y = catGround;
			velocity.y = 0;
			jump = false;
		}
		if (catPos.y > catGround){
			jump = true;
		}

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
		return catSkeleton;
	}

	public void setSkeleton(Skeleton skeleton) {
		this.catSkeleton = skeleton;
	}

	public Skeleton getGhostSkeleton() {
		return ghostSkeleton;
	}

	public void setGhostSkeleton(Skeleton ghostSkeleton) {
		this.ghostSkeleton = ghostSkeleton;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public Vector2 getCatPos() {
		return catPos;
	}

	public void setCatPos(Vector2 catPos) {
		this.catPos = catPos;
		catGround = (int) catPos.y;
		System.out.println("cat pos setted");
	}

	public int getGhostOffset() {
		return ghostOffset;
	}

	public void setGhostOffset(int ghostOffset) {
		this.ghostOffset = ghostOffset;
	}

	public float getMyGravity() {
		return myGravity;
	}

	public void setMyGravity(float myGravity) {
		this.myGravity = myGravity;
	}

}
