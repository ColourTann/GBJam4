package game.screens.gameScreen.map;

import game.util.Colours;
import game.util.Draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CutoffLine extends Actor{

	public CutoffLine(Color color) {
		setColor(color);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor());
		Draw.fillRectangle(batch, getX(), getY(), getX()+500, 1);
		super.draw(batch, parentAlpha);
	} 
	
	
}
