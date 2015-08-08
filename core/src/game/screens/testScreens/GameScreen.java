package game.screens.testScreens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pools;

import game.Main;
import game.util.Colours;
import game.util.Draw;
import game.util.Fonts;
import game.util.Screen;

public class GameScreen extends Screen{
	
	private static GameScreen self;
	public static GameScreen get(){
		if(self==null) self= new GameScreen();
		return self;
	}
	
	public GameScreen() {
		setSize(Main.width, Main.height);
		for(int i=0;i<30;i++)addActor(new Clickable());
	}
	
	@Override
	public void preDraw(Batch batch) {
		batch.setColor(Colours.brown);
		Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void postDraw(Batch batch) {
	}

	@Override
	public void preTick(float delta) {
	}

	@Override
	public void postTick(float delta) {
	}

	


	
}
