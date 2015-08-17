package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.controller.Controller;
import game.util.Colours;
import game.util.Draw;
import game.util.Noise;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Archer extends Entity{

	static float size = Main.p2m(4);
	static PolygonShape box = new PolygonShape();
	static{
		box.setAsBox(size, size);
	}
	public Archer(int x, int y) {
		setHP(2);
		bod = Map.self.makeBody(Main.p2m(x), Main.p2m(y) , box, .2f, 8, 10000, 1, Mask.enemy, (short)(Mask.player|Mask.border|Mask.enemy));
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				other.damage((int)(collisionStrength*2), (short)5);
			}

			@Override
			public void damage(int damage, short mask) {
				defaultShake(damage);
				defaultDamage(damage);
			}

			@Override
			public String toString() {
				return "enemy";
			}
		});
		setController(new Controller() {
			float ticks =0;
			float accel=5f;
			float noiseOffset = (float) (Math.random()*1000);
			float speed=10;
			float freq=1f;
			float secondsPerShot=2f;
			@Override
			public void act(float delta) {
				Entity player = Map.getPlayer();
				ticks+=delta;
				bod.applyForceToCenter((float)Noise.noise((Main.ticks+noiseOffset)*freq)*speed, 
						(float) Noise.noise(Main.ticks+100+noiseOffset*freq)*speed, true);
				while(ticks>secondsPerShot){
					Map.self.addEntity(new Projectile(getX(), getY()));
					ticks-=secondsPerShot;
					bod.applyAngularImpulse(Math.random()>.5?-.5f:.5f, true);
				}
			}
		});
	}


	@Override
	public void postAct(float delta) {



	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.green[1]);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				getX(), getY(), 
				Main.m2p(size*2), 
				Main.m2p(size*2), bod.getAngle());
	}

}
