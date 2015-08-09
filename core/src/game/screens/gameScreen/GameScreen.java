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
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import game.Main;
import game.util.Particle;
import game.util.Screen;

public class GameScreen extends Screen{

	static World world = new World(new Vector2(0,0), false);
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public static GameScreen self;
	Body body;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	public static GameScreen get(){
		if(self==null)self=new GameScreen();
		return self;
	}
	
	public GameScreen() {
		self=this;
		 world.setContactListener(new ContactListener() {

	            @Override
	            public void beginContact(Contact contact) {
	                Fixture fixtureA = contact.getFixtureA();
	                Body bod =contact.getFixtureA().getBody();
	                
	                
	                Fixture fixtureB = contact.getFixtureB();
	                System.out.println(fixtureB.getShape());
//	                fixtureA.getBody().applyForceToCenter(5000, 50, true);
//	                fixtureB.getBody().applyForceToCenter(5000, 50, true);
//	                Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
	            }

	            @Override
	            public void endContact(Contact contact) {
	                Fixture fixtureA = contact.getFixtureA();
	                Fixture fixtureB = contact.getFixtureB();
//	                Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
	            }

				@Override
				public void preSolve(Contact contact, Manifold oldManifold) {
				}

				@Override
				public void postSolve(Contact contact, ContactImpulse impulse) {
				}

	         

	        });
		
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
		
		BodyDef def = new BodyDef();
		def.type=BodyType.StaticBody;
		def.position.set(0, 0);
		PolygonShape bot = new PolygonShape();
		bot.setAsBox(Main.width, 0);
		Body body = world.createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape=bot;
		Fixture fixture = body.createFixture(fixtureDef);
		
		bot.setAsBox(0, Main.p2m(Main.height));
		def.position.set(0,0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		
		def.position.set(Main.p2m(Main.width),0);
		body = world.createBody(def);
		fixtureDef.shape=bot;
		fixture = body.createFixture(fixtureDef);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		
		
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
		debugRenderer.render(world, Main.self.cam.combined);
	}
	@Override
	public void preTick(float delta) {
		world.step(delta, 6, 2);
	}
	@Override
	public void postTick(float delta) {
	}



}
