package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
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
		setSize(16, 16);
		setHP(2000);
		int i = +5;
		System.out.println(+5+(+(+i)));
		bod = Map.self.makeBody(Main.p2m(x), Main.p2m(y), box, .04f, 1.3f, 100, 1, Mask.enemy, (short)(Mask.player|Mask.border|Mask.enemy));
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				other.damage((int)(collisionStrength*2), Mask.player);
			}

			@Override
			public void damage(int damage, short mask) {
				if((mask&Mask.enemy)>0){
				defaultShake(damage);
				}
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


	public int getRotatedTop(){
/*
 * x2 = x0+(x-x0)*cos(theta)+(y-y0)*sin(theta)
y2 = y0-(x-x0)*sin(theta)+(y-y0)*cos(theta)
 */
		float max = 0;
		for(int x = 0; x<=1;x++){
			for(int y=0;y<=1;y++){
				float preX = x*getWidth();
				float preY = y*getHeight();
				float newY = getY()+
						(float) (getHeight()/2-(preX-getWidth()/2)*Math.sin(bod.getAngle()) + 
								(preY-getHeight()/2)*Math.cos(bod.getAngle()));
				if(newY>max)max=newY;

			}
		}
		return (int)(max-getHeight()/2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.green[2]);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				getX(), getY(), 
				Main.m2p(size*2)+1, 
				Main.m2p(size*2)+1, bod.getAngle());
	}

}
