package game.util;


import java.util.ArrayList;

import game.Main;
import game.Main.MainState;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class Screen extends Group{


	private float shakeMagnitude=0;
	private static float shakeFrequency=100;
	private static float shakeDrag=.005f;
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Particle> newParticles = new ArrayList<Particle>();
	public Screen() {
		setSize(Main.width, Main.height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		preDraw(batch);
		super.draw(batch, parentAlpha);
		
		postDraw(batch);

	}
	public abstract void preDraw(Batch batch);
	public abstract void postDraw(Batch batch);

	@Override
	public void act(float delta) {
		if(Main.self.getState()==MainState.Paused)return;
		
		setPosition(getX()+(float)(Math.sin(Main.ticks*shakeFrequency)*shakeMagnitude), 
				getY()+(float) (Math.cos((Main.ticks+100)*shakeFrequency)*shakeMagnitude));
		
		shakeMagnitude*=Math.pow(shakeDrag, delta);
		
		tickParticles(delta);
		preTick(delta);
		super.act(delta);
		postTick(delta);
		setPosition((int)getX(), (int)getY());
	}

	public abstract void preTick(float delta);
	public abstract void postTick(float delta);

	public void addParticle(Particle p){
		newParticles.add(p);
	}
	private void tickParticles(float delta) {
		particles.addAll(newParticles);
		for(Particle p: newParticles) addActor(p);
		newParticles.clear();
		for(int i=particles.size()-1;i>=0;i--){
			Particle p = particles.get(i);
			if(p.dead){
				p.remove();
				particles.remove(p);
			}
		}
	}



	public void shake(float magnitiude){ //pop pop//
		shakeMagnitude+=magnitiude;
	}

	public abstract void keyDown(int keycode);
}
