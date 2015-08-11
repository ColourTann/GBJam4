package game.screens.gameScreen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

import game.Main;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.Archer;
import game.screens.gameScreen.physics.entity.Mask;
import game.screens.gameScreen.physics.entity.DumbBox;
import game.screens.gameScreen.physics.entity.Entity;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Particle;
import game.util.Screen;

public class GameScreen extends Screen{

	
	
	
	public static World world = new World(new Vector2(0,0), false);
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public static GameScreen self;
	Body body;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	Entity currentEnemy;
	Entity currentPlayer;
	public static GameScreen get(){
		if(self==null)self=new GameScreen();
		return self;
	}

	public GameScreen() {
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
		
		
		for(int i=0;i<50;i++)addActor(new Star());
		
		Ship s =new Ship(50, 50, true);
		addActor(s);
		currentPlayer=s;


		addEnemy();
		
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				for(int i=0;i<0;i++){


				}
				return false;
			}

		});


		CollisionHandler edgeHandler = new CollisionHandler(null) {

			@Override
			public void handleCollision(Body me, Body them,
					CollisionHandler other, float collisionStrength,
					Contact contact) {
			}

			@Override
			public void damage(int damage) {
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
		bot.setAsBox(Main.width, 0);
		Body body = world.createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape=bot;
		Fixture fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);
		
		bot.setAsBox(Main.width, 0);
		def.position.set(0, Main.p2m(Main.height));
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);
		
		bot.setAsBox(0, Main.p2m(Main.height));
		def.position.set(0,0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);

		def.position.set(Main.p2m(Main.width),0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		body.setUserData(edgeHandler);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.


	}
	
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
	


	@Override
	public void preDraw(Batch batch) {
		
	}
	@Override
	public void postDraw(Batch batch) {
//		debugRenderer.render(world, Main.self.cam.combined);
	}
	
	Array<Body> bodies;
	private static float timeStep=1/60f;
	float ticks=0;
	float ticksPerEnemy=1.4f;
	@Override
	public void preTick(float delta) {
		world.step(timeStep, 6, 2);
		for(Body b:toDestroy){
			world.destroyBody(b);
		}
		
		toDestroy.clear();
		ticks+=delta;
//		while(ticks>=ticksPerEnemy){
//			addEnemy();
//			ticks-=ticksPerEnemy;
//		}
		if(currentEnemy.dead)addEnemy();
	}
	@Override
	public void postTick(float delta) {
		for(int i=entities.size()-1;i>=0;i--){
			Entity e = entities.get(i);
			if(e.dead){
				entities.remove(e);
				removeActor(e);
			}
		}
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
		int x = (int)Particle.rand(gap,  Main.width-gap);
		int y = (int)Particle.rand(gap, Main.height-gap);
		if(rand>.7) return new Ship(x,y,false);
		if(rand>.4) return new Archer(x, y);
		return new DumbBox(x,y);
	}
	
	

	public static Entity getPlayer() {
		return self.currentPlayer;
	}



}

