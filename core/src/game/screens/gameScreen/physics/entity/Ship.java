package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.controller.Controller;
import game.screens.testScreens.Orbiter;
import game.util.Colours;
import game.util.Draw;
import game.util.Noise;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Entity{

	static PolygonShape box = new PolygonShape();

	public static float smallSize = Main.p2m(2);
	public static float bigSize = Main.p2m(3f);
	int dist = (int) Main.p2m(35);


	Body hull;
	Body spike;
	public Ship(float x, float y, boolean player){
setHP(15);
		box.setAsBox(smallSize, smallSize);
		
		short mask = player?(short)(Mask.border|Mask.enemy|Mask.projectile):(short)(Mask.player|Mask.border);
		short category = player?Mask.player:Mask.enemy;
		
		hull=GameScreen.self.makeBody(Main.p2m(x), Main.p2m(y), box, 1.5f, 2, .1f, .6f, category, mask);
		bod=hull;
		hull.setUserData(new CollisionHandler(hull) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {

			}

			@Override
			public void damage(int damage) {
				defaultDamage(damage);
				defaultShake(damage*2);
			}

			@Override
			public String toString() {
				return "ship body";
			}
		});


//		mask = player?(short)(Mask.enemy|Mask.projectile):(short)(Mask.player);
		box.setAsBox(bigSize*2, bigSize/2);
		spike=GameScreen.self.makeBody(Main.p2m(x)+dist, Main.p2m(y), box, .2f, 0, .1f, .6f,  category, mask);
		spike.setUserData(new CollisionHandler(spike) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				other.damage((int)(collisionStrength*2));
			}

			@Override
			public void damage(int damage) {

			}

			@Override
			public String toString() {
				return "ship tail";
			}
		});
		spike.setBullet(true);
		otherBod=spike;
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.initialize(hull, spike, hull.getPosition(), spike.getPosition());		
		jointDef.bodyA=hull;
		jointDef.bodyB=spike;
		jointDef.initialize(hull, spike, hull.getPosition(), spike.getPosition());
		GameScreen.world.createJoint(jointDef);


		if(player){
			setController(new Controller() {
				@Override
				public void act(float delta) {
					int dx=0, dy=0;
					int accel=15*2;
					if(Gdx.input.isKeyPressed(Keys.W)||Gdx.input.isKeyPressed(Keys.UP)) dy=1;
					if(Gdx.input.isKeyPressed(Keys.S)||Gdx.input.isKeyPressed(Keys.DOWN)) dy=-1;
					if(Gdx.input.isKeyPressed(Keys.A)||Gdx.input.isKeyPressed(Keys.LEFT)) dx=-1;
					if(Gdx.input.isKeyPressed(Keys.D)||Gdx.input.isKeyPressed(Keys.RIGHT)) dx=1;
					hull.applyForce(dx*accel, dy*accel, hull.getPosition().x, hull.getPosition().y, true);
				}
			});
		}
		else{
			setController(new Controller() {
				float noiseOffset = (float) (Math.random()*1000);
				float noiseAmp=8;
				float playerAmp=2;
				float freq=100f;
		
				@Override
				public void act(float delta) {
					Entity player = GameScreen.getPlayer();
					Vector2 dv =player.bod.getPosition().sub(bod.getPosition()).nor();
					bod.applyForceToCenter(
							dv.x*playerAmp+(float)Noise.noise((Main.ticks+noiseOffset)*freq)*noiseAmp, 
							dv.y*playerAmp+(float) Noise.noise((Main.ticks+100+noiseOffset)*freq)*noiseAmp, true);
				}
				
			});
		}
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.light);
		drawBod(batch, hull, smallSize, smallSize);
		Draw.drawLine(batch, 
				getX(), getY(), 
				(int)Main.m2p(spike.getPosition().x), (int)Main.m2p(spike.getPosition().y), 
				1);
		batch.setColor(Colours.red);
		drawBod(batch, spike, bigSize*2, bigSize/2);

		super.draw(batch, parentAlpha);
	}

	public static float getVelocity(Body aBod){
		Vector2 vel =aBod.getLinearVelocity();
		return (float) Math.sqrt(vel.x*vel.x+vel.y*vel.y);
	}

	void drawBod(Batch batch, Body aBod, float w, float h){
		w=Main.m2p(w); h= Main.m2p(h);
		float vel = getVelocity(aBod);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				(int)Main.m2p(aBod.getPosition().x), 
				(int)Main.m2p(aBod.getPosition().y), 
				w*2, 
				h*2, aBod.getAngle());
	}


	@Override
	public void postAct(float delta) {
	}

}
