package game.screens.gameScreen.map;

import game.Main;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.Archer;
import game.screens.gameScreen.physics.entity.DumbBox;
import game.screens.gameScreen.physics.entity.Entity;
import game.screens.gameScreen.physics.entity.Mask;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Colours;
import game.util.Draw;
import game.util.Particle;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;





public class Map extends Group{
	public static World world = new World(new Vector2(0,-10), false);
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	Body body;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	Entity currentEnemy;
	Entity currentPlayer;
	public static Map self;
	CutoffLine line;
	public Map() {
		setSize(500, 500);
		self=this;
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				Body bodyA = fixtureA.getBody();
				Body bodyB = fixtureB.getBody();

				CollisionHandler handlerA =(CollisionHandler) bodyA.getUserData();
				CollisionHandler handlerB =(CollisionHandler) bodyB.getUserData();

				handlerA.handleCollision(bodyA, bodyB, handlerB, impulse.getNormalImpulses()[0], contact);
				handlerB.handleCollision(bodyB, bodyA, handlerA, impulse.getNormalImpulses()[0], contact);
			}
		});


		for(int i=0;i<500;i++){
			Star s = new Star();
			addActor(s);
			s.setPosition(Particle.rand(0, getWidth()), Particle.rand(0, getHeight()));
		}
		line = new CutoffLine();
		line.setPosition(0, 35);
		addActor(line);

		Ship s =new Ship(50, 150, true);
		addActor(s);
		currentPlayer=s;
		Pixmap p = Draw.getPixmap(new Texture(Gdx.files.internal("map/1.png")));

		for(int x=0;x<p.getWidth();x++){
			for(int y=0;y<p.getWidth();y++){
				if(p.getPixel(x, y)==-1682173953){
					addEntity(new DumbBox(8*x, 16*(p.getHeight()-y)-8));
				}
			}
		}

		for(int i=0;i<0;i++)addEnemy();

		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				for(int i=0;i<0;i++){


				}
				return false;
			}

		});
		setupBorders();
	
	}
	
	private void setupBorders(){
		CollisionHandler edgeHandler = new CollisionHandler(null) {

			@Override
			public void handleCollision(Body me, Body them,
					CollisionHandler other, float collisionStrength,
					Contact contact) {
			}

			@Override
			public void damage(int damage, short mask) {
				defaultShake(Math.min(4, damage));
			}

			@Override
			public String toString() {
				return "edge";
			}


		};
		BodyDef def = new BodyDef();
		def.type=BodyType.StaticBody;
		def.position.set(0, 0);
		PolygonShape bot = new PolygonShape();
		bot.setAsBox(getWidth(), 0);
		Body body = world.createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape=bot;
		Fixture fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);

		bot.setAsBox(Main.p2m(getWidth()), 0);
		def.position.set(0, Main.p2m(getWidth()));
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);

		bot.setAsBox(0, Main.p2m(getHeight()));
		def.position.set(0,0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);

		def.position.set(Main.p2m(getWidth()),0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);
	}
	
	Array<Body> bodies;
	private static float timeStep=1/60f;
	float ticks=0;
	float ticksPerEnemy=1.4f;
	public void act(float delta) {
		world.step(timeStep, 6, 2);
		for(Body b:toDestroy){
			world.destroyBody(b);
		}	
		super.act(delta);
		toDestroy.clear();
		ticks+=delta;
//		while(ticks>=ticksPerEnemy){
//			addEnemy();
//			ticks-=ticksPerEnemy;
//		}
		if(currentEnemy!=null&&currentEnemy.dead)addEnemy();
		for(int i=entities.size()-1;i>=0;i--){
			Entity e = entities.get(i);
			if(e.dead){
				entities.remove(e);
				removeActor(e);
			}
		}
		
		float lerpFactor=.1f;
		Vector2 playerVector = new Vector2(Main.width/2-currentPlayer.getX(), Main.height/2-currentPlayer.getY());
		Vector2 targetVector = new Vector2(getX()+(playerVector.x-getX())*lerpFactor, getY()+(playerVector.y-getY())*lerpFactor);
		targetVector.x=Math.min(0, Math.max(-getWidth()+Main.width, targetVector.x));
		targetVector.y=Math.min(0, Math.max(-getHeight()+Main.height, targetVector.y));
		
		int max = 0;
		for(Entity e: entities){
			if (e instanceof DumbBox){
				DumbBox db = (DumbBox)e;
				int top = db.getRotatedTop();
				if(top>max)max=top;
			}
		}
		line.setPosition(0, max);
		
		
		
//		Vector2 targetVector = new Vector2(getWidth()/2-currentPlayer.getX(), getHeight()/2-currentPlayer.getY());
		
		
		setPosition((int)targetVector.x, (int)targetVector.y);
	};
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.setColor(Colours.green[2]);

//		debugRenderer.render(world, Main.self.cam.combined);
		
	};
	
	ArrayList<Body> toDestroy = new ArrayList<Body>();
	public void destroyBody(Body b){
		toDestroy.add(b);
	}

	static BodyDef def = new BodyDef();
	static FixtureDef fixDef = new FixtureDef();

	public Body makeBody(float x, float y, Shape shape, float density,  float linearDampening, float friction, float restitution){
		return makeBody(x, y, shape, density, linearDampening, friction, restitution, Mask.border, Mask.border);
	}

	public Body makeBody(float x, float y, Shape shape, float density,  float linearDampening, float friction, float restitution, short categoryBits, short maskBits){
		def.type=BodyType.DynamicBody;
		def.linearDamping=linearDampening;
		fixDef.shape=shape;
		fixDef.density=density;
		fixDef.friction=friction;
		fixDef.restitution=restitution;
		fixDef.filter.maskBits=maskBits;
		fixDef.filter.categoryBits=categoryBits;
		def.position.set(x,y);
		def.angularDamping=2;
		Body bod =world.createBody(def);
		Fixture fixture = bod.createFixture(fixDef);
		return bod;
	}

	ArrayList<Entity> entities = new ArrayList<>();
	public void addEntity(Entity e){
		entities.add(e);
		addActor(e);
	}



	public void addEnemy() {
		Entity e = getRandomEnemy();
		currentEnemy=e;
		addEntity(e);

	}



	public Entity getRandomEnemy(){
		double rand = Math.random();
		float gap = 50;
		int x = (int)Particle.rand(gap,  getWidth()-gap);
		int y = (int)Particle.rand(gap, getHeight()-gap);
		x=(int) (getWidth()/2); y=(int) (getHeight()/2);
		if(rand>.99) return new Ship(x,y,false);
		if(rand>.99) return new Archer(x, y);
		return new DumbBox(x,y);
	}



	public static Entity getPlayer() {
		return self.currentPlayer;
	}

}
