package game.screens.gameScreen.map;

import game.Main;
import game.util.Colours;
import game.util.Draw;
import game.util.Particle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Star extends Actor{
	
	public Star() {
		setSize(1, 1);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.green[0]);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
		super.draw(batch, parentAlpha);
	}

}
