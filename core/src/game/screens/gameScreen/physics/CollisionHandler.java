package game.screens.gameScreen.physics;

import game.screens.gameScreen.GameScreen;
import game.util.TextWisp;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public abstract class CollisionHandler {

	protected Body body;
	static float momentumFactor=.9f;
	static float rotationFactor=.2f;
	public CollisionHandler(Body b) {
		this.body=b;
	}
	
	public abstract void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact);
	public abstract void damage(int damage, short mask);
	public void defaultShake(int damage) {
		GameScreen.get().shake(damage/2);
	}
	public abstract String toString();

	public void damage(int damage) {
	}
	
}
