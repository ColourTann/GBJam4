package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.CollisionHandler;
import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Projectile extends Entity{

	static float size = Main.p2m(2);
	static PolygonShape box = new PolygonShape();
	static{box.setAsBox(size, size);}
	
	public Projectile(float x, float y) {
		setHP(1);
		
		bod = Map.self.makeBody(Main.p2m(x), Main.p2m(y), box, .3f, 0, 10000, 1, Mask.projectile, (short)(Mask.player|Mask.projectile));
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				if(dead)return;
				kill();
			}

			@Override
			public void damage(int damage, short mask) {	
				defaultShake(damage);
				defaultDamage(1);
			}
			
			@Override
			public String toString() {
				return "enemy";
			}
		});
		bod.setBullet(true);
		updatePosition();
		moveTowardsPlayer(100);
	}
	
	@Override
	public void postAct(float delta) {
		float gap = size*2;
		if(getX()<-gap||getX()>GameScreen.get().getWidth()+gap||
				getY()<-gap||getY()>GameScreen.get().getHeight()+gap){
			dead=true;
			Map.self.destroyBody(bod);
			bod=null;
			
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(dead)return;
		batch.setColor(Colours.green[2]);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				getX(), getY(), 
				Main.m2p(size*2), 
				Main.m2p(size*2), bod.getAngle());
	}

}
