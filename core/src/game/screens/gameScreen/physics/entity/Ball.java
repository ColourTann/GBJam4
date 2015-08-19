package game.screens.gameScreen.physics.entity;

import game.Main;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.map.Map.MapType;
import game.screens.gameScreen.physics.CollisionHandler;
import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball extends Entity{
	static float size = Main.p2m(8);
	static CircleShape circle = new CircleShape();
	static TextureRegion tr = Main.atlas.findRegion("circle16");
	static{circle.setRadius(size);}
	public Ball(int x, int y) {
		bod = Map.self.makeBody(Main.p2m(x), Main.p2m(y), circle, .04f, 1.3f, 100, 1, Mask.enemy, (short)(Mask.player|Mask.enemy), BodyType.DynamicBody);
		updatePosition();
		bod.setUserData(new CollisionHandler(bod) {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other, float collisionStrength, Contact contact) {
				//				if(type==MapType.ball)return;
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
		if(getX()<5){
			Map.self.goal(1);
		}
		else if(getX()>Map.self.getWidth()-5){
			Map.self.goal(0);
		}
	}

	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.setColor(Colours.green[1]);
		
		Draw.drawCentered(batch, tr, 
				getX(), getY());
	}
}
