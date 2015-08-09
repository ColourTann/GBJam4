package game.screens.gameScreen;

import game.Main;
import game.screens.testScreens.Orbiter;
import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Actor{
	
	static PolygonShape box = new PolygonShape();
	
	public static float smallSize = Main.p2m(2);
	public static float bigSize = Main.p2m(3f);
	int dist = (int) Main.p2m(35);
	
	
	Body hull;
	Body spike;
	
	public Ship(float x, float y){
	
		hull=GameScreen.self.makeBody(box, 2, .1f, .5f, .6f, x, y, smallSize);
		spike=GameScreen.self.makeBody(box, 0, .1f, .2f, .6f, x+dist, y, bigSize);

		
		hull.setUserData(new CollisionHandler() {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				
			}

			@Override
			public void damage(float damage) {
			}
		});
		
		spike.setUserData(new CollisionHandler() {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				float speed = collisionStrength;
				GameScreen.get().shake(speed*2);
				for(int i=0;i<Math.pow(speed, 3)/2;i++)GameScreen.get().addParticle(new Orbiter(Main.m2p(me.getPosition().x), Main.m2p(me.getPosition().y)));
				System.out.println("hi");
				other.damage(speed);
			}

			@Override
			public void damage(float damage) {
			}
		});
		
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.initialize(hull, spike, hull.getPosition(), spike.getPosition());
		
	
		
		jointDef.bodyA=hull;
		jointDef.bodyB=spike;
		
		jointDef.initialize(hull, spike, hull.getPosition(), spike.getPosition());
		
		GameScreen.world.createJoint(jointDef);
	}
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		int dx=0, dy=0;
		int accel=15;
		if(Gdx.input.isKeyPressed(Keys.W)) dy=1;
		if(Gdx.input.isKeyPressed(Keys.S)) dy=-1;
		if(Gdx.input.isKeyPressed(Keys.A)) dx=-1;
		if(Gdx.input.isKeyPressed(Keys.D)) dx=1;
			hull.applyForce(dx*accel, dy*accel, hull.getPosition().x, hull.getPosition().y, true);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.light);
		drawBod(batch, hull, smallSize);
		Draw.drawLine(batch, 
				(int)Main.m2p(hull.getPosition().x), (int)Main.m2p(hull.getPosition().y), 
				(int)Main.m2p(spike.getPosition().x), (int)Main.m2p(spike.getPosition().y), 
				1);
		batch.setColor(Colours.red);
		drawBod(batch, spike, bigSize);
		
		super.draw(batch, parentAlpha);
	}
	
	static float getVelocity(Body aBod){
		Vector2 vel =aBod.getLinearVelocity();
		return (float) Math.sqrt(vel.x*vel.x+vel.y*vel.y);
	}
	
	void drawBod(Batch batch, Body aBod, float size){
		float vel = getVelocity(aBod);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				(int)Main.m2p(aBod.getPosition().x)-size/2, 
				(int)Main.m2p(aBod.getPosition().y)-size/2, 
				Main.m2p(size*2), 
				Main.m2p(size*2), aBod.getAngle());
	}

}
