package game.screens.gameScreen;

import game.Main;
import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Actor{
	
	static PolygonShape box = new PolygonShape();
	static FixtureDef fixtureDef = new FixtureDef();
	static BodyDef def = new BodyDef();
	public static float smallSize = Main.p2m(2);
	public static float bigSize = Main.p2m(3f);
	int dist = (int) Main.p2m(35);
	static{
		def.type=BodyType.DynamicBody;
		fixtureDef.shape = box;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.6f; 
		def.linearDamping=5;
	}
	
	Body bod;
	Body bod2;
	
	public Ship(float x, float y){
//		def.position.set(x, y);
//		def.linearDamping=2;
//		box.setAsBox(smallSize, smallSize);
//		bod = GameScreen.world.createBody(def);
//		Fixture fixture = bod.createFixture(fixtureDef);
//		
//		def.position.set(x+dist,y);
//		def.linearDamping=0;
//		box.setAsBox(bigSize, bigSize);
//		fixtureDef.density = 0.2f; 
//		bod2 = GameScreen.world.createBody(def);
//		Fixture f1= bod2.createFixture(fixtureDef);
//		

		
		bod=GameScreen.self.makeBody(box, 2, .1f, .5f, .6f, x, y, smallSize);
		bod2=GameScreen.self.makeBody(box, 0, .1f, .2f, .6f, x+dist, y, bigSize);

		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.initialize(bod, bod2, bod.getPosition(), bod2.getPosition());
		
	
		
		jointDef.bodyA=bod;
		jointDef.bodyB=bod2;
		
		jointDef.initialize(bod, bod2, bod.getPosition(), bod2.getPosition());
		
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
			bod.applyForce(dx*accel, dy*accel, bod.getPosition().x, bod.getPosition().y, true);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.light);
		drawBod(batch, bod, smallSize);
		Draw.drawLine(batch, 
				(int)Main.m2p(bod.getPosition().x), (int)Main.m2p(bod.getPosition().y), 
				(int)Main.m2p(bod2.getPosition().x), (int)Main.m2p(bod2.getPosition().y), 
				1);
		batch.setColor(Colours.red);
		drawBod(batch, bod2, bigSize);
		
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
