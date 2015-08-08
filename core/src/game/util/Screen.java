package game.util;


import java.util.ArrayList;

import game.Main;
import game.Main.MainState;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class Screen extends Group{
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Particle> newParticles = new ArrayList<Particle>();
	public Screen() {
		setSize(Main.width, Main.height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		preDraw(batch);
		super.draw(batch, parentAlpha);
		drawParticles(batch);
		postDraw(batch);
		
	}
	public abstract void preDraw(Batch batch);
	public abstract void postDraw(Batch batch);
	
	@Override
	public void act(float delta) {
		if(Main.self.getState()==MainState.Paused)return;
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
		newParticles.clear();
		for(int i=particles.size()-1;i>=0;i--){
			Particle p = particles.get(i);
			p.act(delta);
			if(p.dead)particles.remove(p);
		}
	}

	public void drawParticles(Batch batch){
		for(Particle p : particles) p.draw(batch);
	}
}
