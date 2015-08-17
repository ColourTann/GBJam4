package game.screens.testScreens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

import game.util.Fonts;
import game.util.Screen;

public class StartScreen extends Screen{

	private static StartScreen self;
	public static StartScreen get(){
		if(self==null) self= new StartScreen();
		return self;
	}
	
	@Override
	public void preDraw(Batch batch) {
	}

	@Override
	public void postDraw(Batch batch) {
		Fonts.font.draw(batch, "Press esc to get to the menu", getX()+getWidth()/2, getY()+getHeight()/2, 0, Align.center, false);
	}

	@Override
	public void preTick(float delta) {
	}

	@Override
	public void postTick(float delta) {
	}

	@Override
	public void keyDown(int keycode) {
	}

}
