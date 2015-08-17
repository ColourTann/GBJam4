package game.screens.gameScreen.map;

import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CutoffLine extends Actor{

	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Colours.green[1]);
		Draw.fillRectangle(batch, getX(), getY(), getX()+500, 1);
		super.draw(batch, parentAlpha);
	} 
	
	
}
