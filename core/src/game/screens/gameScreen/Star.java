package game.screens.gameScreen;

import game.Main;
import game.util.Colours;
import game.util.Draw;
import game.util.Particle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Star extends Actor{
	
	public Star() {
		setSize(1, 1);
		setPosition(Particle.rand(0, Main.width), Particle.rand(0, Main.height));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.light);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		super.draw(batch, parentAlpha);
	}

}
