package pl.edu.piotrekuczy;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Killer {

	private Vector2 killerPos;
	private float killerRadius;
	private Circle killerCircle;
	private boolean up;
	
	
	public Killer(Vector2 killerPos, float killerRadius, boolean up) {
		
		this.up = up;
		this.killerPos = killerPos;
		this.killerRadius = killerRadius;
		killerCircle = new Circle(killerPos, killerRadius);
		
	}


	public Vector2 getKillerPos() {
		return killerPos;
	}


	public void setKillerPos(Vector2 killerPos) {
		this.killerPos = killerPos;
	}


	public float getKillerRadius() {
		return killerRadius;
	}


	public void setKillerRadius(float killerRadius) {
		this.killerRadius = killerRadius;
	}


	public boolean isUp() {
		return up;
	}


	public void setUp(boolean up) {
		this.up = up;
	}

}
