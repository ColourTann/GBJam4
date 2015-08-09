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
import game.util.Particle;
import game.util.Screen;

public class GameScreen extends Screen{

	static World world = new World(new Vector2(0,0), false);
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public static GameScreen self;
	Body body;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	Enemy currentEnemy;
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
		
		
		addEnemy();
		for(int i=0;i<50;i++)addActor(new Star());

		Ship s =new Ship(Main.p2m(50), Main.p2m(50));
		addActor(s);

		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				for(int i=0;i<0;i++){


					Ship s =new Ship(Main.p2m((int)x), Main.p2m((int)y));
					addActor(s);

				}
				return false;
			}

		});


		CollisionHandler edgeHandler = new CollisionHandler() {

			@Override
			public void handleCollision(Body me, Body them,
					CollisionHandler other, float collisionStrength,
					Contact contact) {
			}

			@Override
			public void damage(float damage) {
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
	static PolygonShape box = new PolygonShape();
	public Body makeBody(Shape shape, float linearDampening, float friction, float density, float restitution, float x, float y, float size){
		def.type=BodyType.DynamicBody;
		def.linearDamping=linearDampening;
		fixDef.shape=box;
		fixDef.density=density;
		fixDef.friction=friction;
		fixDef.restitution=restitution;
		def.position.set(x,y);
		def.angularDamping=2;
		box.setAsBox(size, size);
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
	@Override
	public void preTick(float delta) {
		world.step(delta, 6, 2);
		for(Body b:toDestroy){
			world.destroyBody(b);
			System.out.println("dest");
		}
		toDestroy.clear();
		if(currentEnemy.dead)addEnemy();
		
	}
	@Override
	public void postTick(float delta) {
		
	}

	public void addEnemy() {
		currentEnemy=new Enemy((int)Particle.rand(10,  Main.width-10), (int)Particle.rand(10, Main.height-10));
		addActor(currentEnemy);
	}



}
