package game.screens.gameScreen.map;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.Ball;
import game.screens.gameScreen.physics.entity.DumbBox;
import game.screens.gameScreen.physics.entity.Entity;
import game.screens.gameScreen.physics.entity.Mask;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Colours;
import game.util.Draw;
import game.util.Fonts;
import game.util.Particle;
import game.util.Sounds;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.plaf.multi.MultiPanelUI;

import org.omg.IOP.MultipleComponentProfileHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;





public class Map extends Group{
	public enum MapType{demolish, targets, ball};
	public World world;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	Body body;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	Entity currentEnemy;
	public Ship currentPlayer;
	public static Map self;
	CutoffLine currentLine;
	CutoffLine targetLine;
	public MapType type;
	Ball ball;
	public int targetsLeft;
	Ship[] players = new Ship[2];
	public Map(MapType type, int mapNumber) {
		switch(type){
		case demolish:
			world= new World(new Vector2(0,-10), false);
			break;
		case targets:
		case ball:
			world= new World(new Vector2(0, 0), false);
			break;
		default:
			break;
		}
		this.type=type;
		self=this;
		Pixmap p = Draw.getPixmap(new Texture(Gdx.files.internal("map/"+type+mapNumber+".png")));
		setSize(p.getWidth()*16, p.getHeight()*16);
		
	
		
		
		int numStars = (int) (getWidth()*getHeight()/1000);
		for(int i=0;i<numStars;i++){
			Star s = new Star();
			addActor(s);
			s.setPosition(Particle.rand(0, getWidth()), Particle.rand(0, getHeight()));
		}

		for(int x=0;x<p.getWidth();x++){
			for(int y=0;y<p.getWidth();y++){
				if(p.getPixel(x, y)==-1682173953){
					switch(type){
					case demolish:

						break;
					case targets:
						targetsLeft++;
						break;
					default:
						break;

					}
					addEntity(new DumbBox(8+16*x, 16*(p.getHeight()-y)-8, type));					
				}
			}
		}
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

		if(type==MapType.demolish){
			addLines();
		}

	


		setupBorders();
		
		
		switch(type){
		case ball:
			float gap =30;
			ball = new Ball((int)getWidth()/2, (int)getHeight()/2); 
			addEntity(ball);
			Ship p1 =new Ship(ball.getX()-gap, ball.getY()+gap, 1);
			addActor(p1);
			players[0]=p1;
			Ship p2 =new Ship(ball.getX()+gap, ball.getY()-gap, 2);
			addActor(p2);
			players[1]=p2;
			break;
		case demolish:
		case targets:
			Ship s =new Ship(40, 150, 0);
			addActor(s);
			currentPlayer=s;
			break;
		default:
			break;
		
		}

	}

	private void addLines() {
		currentLine = new CutoffLine(Colours.green[1]);
		addActor(currentLine);

		targetLine = new CutoffLine(Colours.green[2]);
		targetLine.setPosition(0, 55);
		addActor(targetLine);
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
		//bot//
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
		def.position.set(0, Main.p2m(getHeight()));
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
	boolean victory;
	public void act(float delta) {
		world.step(timeStep, 6, 2);
		for(Body b:toDestroy){
			world.destroyBody(b);
		}	
		super.act(delta);
		toDestroy.clear();
		ticks+=delta;

		for(int i=entities.size()-1;i>=0;i--){
			Entity e = entities.get(i);
			if(e.dead){
				entities.remove(e);
				removeActor(e);
			}
		}

		if(type==MapType.demolish) checkLines();

		switch(type){
		case ball:
			Vector2 targetVector = new Vector2(Main.width/2- ball.getX(), Main.height/2-ball.getY());
			targetVector.x=Math.min(0, Math.max(-getWidth()+Main.width, targetVector.x));
			targetVector.y=Math.min(0, Math.max(-getHeight()+Main.height, targetVector.y));
			setPosition((int)targetVector.x, (int)targetVector.y);
			break;
		case demolish:
		case targets:
			float lerpFactor=.1f;
			Vector2 playerVector = new Vector2(Main.width/2-currentPlayer.getX(), Main.height/2-currentPlayer.getY());
			targetVector = new Vector2(getX()+(playerVector.x-getX())*lerpFactor, getY()+(playerVector.y-getY())*lerpFactor);
			targetVector.x=Math.min(0, Math.max(-getWidth()+Main.width, targetVector.x));
			targetVector.y=Math.min(0, Math.max(-getHeight()+Main.height, targetVector.y));
			setPosition((int)targetVector.x, (int)targetVector.y);
			break;
		default:
			break;
		
		}
		

		if(!victory){
			victory=checkVictory();
			if(victory)win.play(Sounds.volume);
		}

	};



	private boolean checkVictory() {
		switch(type){
		case demolish:
			return currentLine.getY()<=targetLine.getY();
		case targets:
			return targetsLeft==0;
		default:
			return false;

		}
	}

	private void checkLines() {
		int max = 0;
		for(Entity e: entities){
			if (e instanceof DumbBox){
				DumbBox db = (DumbBox)e;
				int top = db.getRotatedTop();
				if(top>max)max=top;
			}
		}
		currentLine.setPosition(0, max);

		if(currentLine.getY()<+targetLine.getY()){
			GameScreen.get().win();
		}
	}

	static Vector2 dist = new Vector2();
	static Vector2 ballPos = new Vector2();
	static Vector2 shipPos = new Vector2();
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(victory){
			Fonts.font.draw(batch, "Victory!\n Push start (esc)", Main.width/2, Main.height/2, 0, Align.center, false); 
		}
		
		if(type==MapType.ball){
			ballPos.x=-getX()+Main.width/2; ballPos.y=-getY()+Main.height/2;
			int i=0;
			for(Ship s:players){
				i++;
				shipPos.x=s.getX(); shipPos.y=s.getY();
				dist=shipPos.cpy().sub(ballPos);

				
				float xMultiplier = dist.y/(Main.height/2);
				
				float yMultiplier = dist.x/(Main.width/2);
				float multiplier = 0;
				if(Math.abs(xMultiplier)>Math.abs(yMultiplier)){
					multiplier=xMultiplier;
				}
				else multiplier = yMultiplier;
				
				
				multiplier=1/multiplier;
				if(Math.abs(multiplier)>1)continue;
	
				
				multiplier*=.93f;
		
				int x =(int) (Main.width/2+dist.x*Math.abs(multiplier));
				int y =(int) (Main.height/2+dist.y*Math.abs(multiplier));
				x++;y++;
				
				batch.setColor(Colours.green[2]);
				Draw.drawCenteredRotatedScaled(batch, Draw.getSq(),x,y, 9, 9, 0, true);
				Fonts.font.draw(batch, ""+i, x, y+5,0, Align.center, false);
				
			}
		}
	};

	ArrayList<Body> toDestroy = new ArrayList<Body>();
	public void destroyBody(Body b){
		toDestroy.add(b);
		targetsLeft--;
	}

	static BodyDef def = new BodyDef();
	static FixtureDef fixDef = new FixtureDef();

	public Body makeBody(float x, float y, Shape shape, float density,  float linearDampening, float friction, float restitution){
		return makeBody(x, y, shape, density, linearDampening, friction, restitution, Mask.border, Mask.border, BodyType.DynamicBody);
	}

	public Body makeBody(float x, float y, Shape shape, float density,  float linearDampening, float friction, float restitution, short categoryBits, short maskBits, BodyType type){
		def.type=type;
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
		bod.createFixture(fixDef);
		return bod;
	}

	ArrayList<Entity> entities = new ArrayList<>();
	public void addEntity(Entity e){
		entities.add(e);
		addActor(e);
	}






	public static Ship getPlayer() {
		return self.currentPlayer;
	}

	public void dispose() {
		world.dispose();
	}

	public void drawStats(Batch batch) {
		switch(type){
		case demolish:

			break;
		case targets:
			Fonts.font.draw(batch, "Targets left: "+targetsLeft, 0, Main.height);
			break;
		case ball:
			String s = score[0]+"";
			if(!players[0].hasMoved) s+= "  wasd";
			Fonts.font.draw(batch, s, 2, Main.height);
			s=score[1]+"";
			if(!players[1].hasMoved) s= "udlr  "+s;
			Fonts.font.draw(batch, s, Main.width, Main.height, 0, Align.right, false);
			break;
		
		default:
			break;

		}
	}
	static int[] score = new int[]{0,0};

	static Sound win = Sounds.get("win", Sound.class);
	public void goal(int pNum) {
		win.play(Sounds.volume);
		score[pNum]=score[pNum]+1;
		GameScreen.get().setMap(MapType.ball, 0);
	}

}
