package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.controller.Controller;
import game.screens.testScreens.Orbiter;
import game.util.Colours;
import game.util.Draw;
import game.util.Noise;
import game.util.Sounds;
import game.util.TextWisp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Entity{

	static PolygonShape box = new PolygonShape();

	public static float smallSize = Main.p2m(2);
	public static float bigSize = Main.p2m(3f);
	int dist = (int) Main.p2m(33);


	Body hull;
	Body spike;
	int playerNum;
	static Sound hitBlock = Sounds.get("hitblock", Sound.class);
	public Ship(float x, float y, final int player){
		this.playerNum=player;
		box.setAsBox(smallSize, smallSize);

		short mask = (short)(Mask.border|Mask.enemy|Mask.projectile|Mask.player);
		short category =Mask.player;

		hull=Map.self.makeBody(Main.p2m(x), Main.p2m(y), box, 1.5f, 2, .1f, .6f, category, mask, BodyType.DynamicBody);
		bod=hull;
		hull.setUserData(new CollisionHandler(hull) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {


			}

			@Override
			public void damage(int damage, short mask) {
				if(invincibleTicks>0)return;
				if((mask&Mask.player)>0){
					if(damage>2){
						defaultShake(10);
						invincible(1.5f);
					}
				}
			}

			@Override
			public String toString() {
				return "ship body";
			}
		});

		box.setAsBox(bigSize*2, bigSize/2);
		spike=Map.self.makeBody(Main.p2m(x), Main.p2m(y)+(player<=1?dist:-dist), box, .2f, 0, .1f, .6f,  category, mask, BodyType.DynamicBody);
		
		spike.setUserData(new CollisionHandler(spike) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				if(collisionStrength>1){
					hitBlock.play(Sounds.volume);
				}
				other.damage((int)(collisionStrength*4), (short)(Mask.enemy|Mask.player));
			}

			@Override
			public void damage(int damage, short mask) {

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
		Map.self.world.createJoint(jointDef);


		setController(new Controller() {
			@Override
			public void act(float delta) {
				if(hull==null||invincibleTicks>0)return;
				int dx=0, dy=0;
				int accel=28;

				boolean p1 = player<=1;
				boolean p2 = player%2==0; 
				boolean up =(Gdx.input.isKeyPressed(Keys.W)&&p1)||(Gdx.input.isKeyPressed(Keys.UP)&&p2); 
				boolean down=Gdx.input.isKeyPressed(Keys.S)&&p1||Gdx.input.isKeyPressed(Keys.DOWN)&&p2;
				boolean left =Gdx.input.isKeyPressed(Keys.A)&&p1||Gdx.input.isKeyPressed(Keys.LEFT)&&p2;
				boolean right =Gdx.input.isKeyPressed(Keys.D)&&p1||Gdx.input.isKeyPressed(Keys.RIGHT)&&p2;

				int i=0;
				for(com.badlogic.gdx.controllers.Controller c:Controllers.getControllers()){
					i++;
					if(!((p1&&i==1)||(p2&&i==2)))continue;
					
					
					float cutoff=.5f;
					if(c.getAxis(1)>cutoff){
						right=true;
					}
					if(c.getAxis(1)<-cutoff){
						left=true;
					}
					if(c.getAxis(0)>cutoff){
						down=true;
					}
					if(c.getAxis(0)<-cutoff){
						up=true;
					}

				}


				if(up) dy++;
				if(down) dy--;
				if(left) dx--;
				if(right) dx++;
				
				if(up||left||right||down){
					hasMoved=true;
				}
				
				hull.applyForce(dx*accel, dy*accel, hull.getPosition().x, hull.getPosition().y, true);
			}
		});



	}

	float invincibleTicks;

	public boolean hasMoved;
	static Sound hurt = Sounds.get("hurt", Sound.class);
	private void invincible(float seconds) {
		hurt.play(Sounds.volume);
		invincibleTicks=seconds;
		GameScreen.get().addParticle(new TextWisp("Stunned", (int)(getX()+getParent().getX()), (int)(getY()+getParent().getY())));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(invincibleTicks%.1f>.05f)return;
		batch.setColor(Colours.green[1]);
		if(hull!=null){
			Draw.drawLine(batch, 
					getX(), getY(), 
					(int)Main.m2p(spike.getPosition().x), (int)Main.m2p(spike.getPosition().y), 
					1);

			batch.setColor(playerNum<=1?Colours.green[0]:Colours.green[2]);
			drawBod(batch, hull, smallSize, smallSize);
		}
		batch.setColor(Colours.green[2]);
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
		invincibleTicks-=delta;
	}



	public int getHP() {
		return hp;
	}

}
