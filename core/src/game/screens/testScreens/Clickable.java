package game.screens.testScreens;

import game.Main;
import game.util.Colours;
import game.util.Draw;
import game.util.Particle;
import game.util.Slider;
import game.util.Sounds;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class Clickable extends Actor{
	boolean clicked;
	
	public Clickable() {
		setColor(Colours.light);
		setSize((float)Math.random()*50+10, (float)Math.random()*50+10);
		setPosition((float)Math.random()*Main.width, (float)Math.random()*Main.height);
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				clicked=true;
				addAction(Actions.moveBy(Particle.rand(-100, 100), Particle.rand(-100, 100), .5f, Interpolation.pow2Out));
				setColor(Colours.dark);
				addAction(Actions.color(Colours.light, .5f, Interpolation.pow2Out));
				toFront();
				for(int i=0;i<100;i++)GameScreen.get().addParticle(new Orbiter(getX(Align.center), getY(Align.center)));
				Sounds.get("win", Sound.class).play(Slider.SFX.getValue());
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				clicked=false;
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor());
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		super.draw(batch, parentAlpha);
	}

}
