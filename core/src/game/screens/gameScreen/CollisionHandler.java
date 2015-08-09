package game.screens.gameScreen;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public abstract class CollisionHandler {

	
	public abstract void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact);
	public abstract void damage(float damage);
}
