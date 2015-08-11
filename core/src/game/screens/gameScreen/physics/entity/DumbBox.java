package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.physics.CollisionHandler;
import game.util.Colours;
import game.util.Draw;
import game.util.TextWisp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class DumbBox extends Entity{

	static float size = Main.p2m(8);
	static PolygonShape box = new PolygonShape();
	static{box.setAsBox(size, size);}
	
	public DumbBox(int x, int y) {
		setHP(20);
		bod = GameScreen.get().makeBody(Main.p2m(x), Main.p2m(y), box, .2f, 5, 10000, 1, Mask.enemy, (short)(Mask.player|Mask.border));
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
			}

			@Override
			public void damage(int damage) {	
				defaultShake(damage);
				defaultDamage(damage);
			}
			
			@Override
			public String toString() {
				return "enemy";
			}
		});
		
	}
	
	@Override
	public void postAct(float delta) {
	}	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.red);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				getX(), getY(), 
				Main.m2p(size*2), 
				Main.m2p(size*2), bod.getAngle());
	}
	
}
