package game.screens.testScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import game.Main;
import game.util.Fonts;
import game.util.Screen;

public class FontScreen extends Screen{

	int fontX, fontY;
	
	private static FontScreen self;
	public static FontScreen get(){
		if(self==null) self= new FontScreen();
		return self;
	}
	
	@Override
	public void preDraw(Batch batch) {
	}

	@Override
	public void postDraw(Batch batch) {
		for(int i=0;i<4;i++){
			Fonts.fontSizes[i].draw(batch, "Different sizes!", getX()+0, getY()-30+(float) (getHeight()-(Math.pow(i, 1.98)+i*1)*5));
			System.out.println(getY()-30+(float) (Gdx.graphics.getHeight()-(Math.pow(i, 1.98)+i*1)*5));
		}
		int x= 10; int y=50;
		Fonts.love.draw(batch, "Font: pixel-love", getX()+x, getY()+y);
		x+=100;
		Fonts.O4B03.draw(batch, "Font: 04B03", getX()+x, y);
		x+=100;
		Fonts.pixelArial.draw(batch, "Font: pixelarial", getX()+x, getY()+y);
		x=10;
		y+=50;
		Fonts.pixelMix.draw(batch, "Font: pixelmix", getX()+x, getY()+y);
		x+=100;
		Fonts.visitor.draw(batch, "Font: visitor", getX()+x, getY()+y);
		x+=100;
		Fonts.pressStartP2.draw(batch, "Font: pressstartp2", getX()+x, getY()+y);
		
		Fonts.font.draw(batch, "another font", getX()+fontX, getY()+fontY);
		
	}

	@Override
	public void preTick(float delta) {
		fontX=Gdx.input.getX();
		fontY=Main.height-Gdx.input.getY();
	}

	@Override
	public void postTick(float delta) {
	}

	@Override
	public void keyDown(int keycode) {
	}
	
	

}
