package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.map.Map.MapType;
import game.screens.gameScreen.physics.CollisionHandler;
import game.util.Colours;
import game.util.Draw;
import game.util.TextWisp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class DumbBox extends Entity{

	static float size = Main.p2m(8);
	static PolygonShape box = new PolygonShape();
	static{box.setAsBox(size, size);}
	MapType type;
	public DumbBox(int x, int y, final MapType type) {
		this.type=type;
		setSize(16, 16);
		setHP(2000);
		bod = Map.self.makeBody(Main.p2m(x), Main.p2m(y), box, .04f, 1.3f, 100, 1, Mask.enemy, (short)(Mask.player|Mask.border|Mask.enemy), type==MapType.ball?BodyType.StaticBody:BodyType.DynamicBody);
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
//				if(type==MapType.ball)return;
				other.damage((int)(collisionStrength*(type==MapType.ball?.3f:2)), Mask.player);
			}

			@Override
			public void damage(int damage, short mask) {
				if(type==MapType.targets){
					lightUp();
				}
				else if (type==MapType.demolish){ 
					if((mask&Mask.enemy)>0){
						defaultShake(damage);
					}
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

	public boolean lit;
	private void lightUp() {
		if(lit)return;
		lit=true;
		Map.self.targetsLeft--;
	}

	public int getRotatedTop(){
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
		
		batch.setColor(lit?Colours.green[1]:Colours.green[2]);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				getX(), getY(), 
				Main.m2p(size*2)+1, 
				Main.m2p(size*2)+1, bod.getAngle(), true);
	}

}
