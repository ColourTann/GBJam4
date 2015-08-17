package game.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Particle extends Actor{
	public boolean dead;
	public float x,y,dx,dy,angle,ratio;
	private float life, startLife;
	public abstract void tick(float delta);
	public abstract void draw(Batch batch, float parentAlpha);
	public void act(float delta){
		tickLife(delta);
		tick(delta);
	}
	protected void setupLife(float life){
		startLife=life;
		this.life=life;
		this.ratio=1;
	}
	protected void tickLife(float delta){
		life-=delta;
		if(life<=0){
			dead=true;
			life=0;
		}
		ratio=life/startLife;
	}
	public static float rand(float min, float max){
		return (float)(Math.random()*(max-min)+min);
	}
}
