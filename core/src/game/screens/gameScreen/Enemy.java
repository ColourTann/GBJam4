package game.screens.gameScreen;

import game.Main;
import game.util.Colours;
import game.util.Draw;
import game.util.TextWisp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemy extends Actor{

	static float size = Main.p2m(8);

	static PolygonShape box = new PolygonShape();
	Body bod;
	public boolean dead;
	int hp=5;
	public Enemy(int x, int y) {
		bod = GameScreen.get().makeBody(box, 0, 10000, .2f, 1, Main.p2m(x), Main.p2m(y), size);
		bod.setUserData(new CollisionHandler() {
			@Override
			public void handleCollision(Body me, Body them, CollisionHandler other,
					float collisionStrength, Contact contact) {


			}

			@Override
			public void damage(float damage) {
				System.out.println(damage);
				int actualDamage = (int) (damage*2);
				if(actualDamage==0)return;
				GameScreen.get().addParticle(new TextWisp(actualDamage+"", (int)(Main.m2p(bod.getPosition().x)-size/2), (int)(Main.m2p(bod.getPosition().y)-size/2)));
				hp-=actualDamage;
				if(hp<=0){
					remove();
					GameScreen.get().destroyBody(bod);
					dead=true;
				}

			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.red);
		Draw.drawCenteredRotatedScaled(batch, Draw.getSq(), 
				(int)Main.m2p(bod.getPosition().x)-size/2, 
				(int)Main.m2p(bod.getPosition().y)-size/2, 
				Main.m2p(size*2), 
				Main.m2p(size*2), bod.getAngle());
	}

}
