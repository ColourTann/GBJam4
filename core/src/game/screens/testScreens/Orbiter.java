package game.screens.testScreens;

import com.badlogic.gdx.graphics.g2d.Batch;

import game.Main;
import game.util.Draw;
import game.util.Particle;

public class Orbiter extends Particle{

	Orbiter(float x, float y) {
		setupLife(.5f);
		this.x=x;
		this.y=y;
		float angle = (float) (Math.random()*Math.PI*2);
		float magnitude = (float) (Math.random()*800);
		dx=(float) (Math.sin(angle)*magnitude);
		dy=(float) (Math.cos(angle)*magnitude);
		dead=false;	
	}
	
	float drag=.05f;
	@Override
	public void tick(float delta) {
		dx *=Math.pow(drag, delta);
		dy *=Math.pow(drag, delta);
		x+=dx*delta;
		y+=dy*delta;
		
	}

	@Override
	public void draw(Batch batch) {
		batch.setColor(1,1,1,ratio);
		Draw.drawCenteredScaled(batch, Draw.getSq(), x, y, 10, 10);
	}

}
