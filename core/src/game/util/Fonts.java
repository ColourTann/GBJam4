package game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Fonts {
	
	public static GlyphLayout bounds = new GlyphLayout();
	
	public static BitmapFont love; // size 8 //
	public static BitmapFont pixelMix; // size 8 //
	public static BitmapFont O4B03; // size 8 //
	public static BitmapFont pressStartP2; // size 8 //
	public static BitmapFont pixelArial; // size 8 //
	public static BitmapFont visitor; // size 10 //
	
	public static BitmapFont font;
	public static BitmapFont largeFont;
	
	public static BitmapFont[] fontSizes = new BitmapFont[10];
	
	public static void setup(){
		love = loadFont("pixel-love");
		pixelMix = loadFont("pixelmix");
		O4B03 = loadFont("04B_03");
		pressStartP2 = loadFont("PressStartP2");
		pixelArial = loadFont("pixelarial");
		visitor = loadFont("visitor");
		
		font = pixelMix;
		largeFont=loadFont("pixel-love", 4);
		
		for(int i=0;i<fontSizes.length;i++){
			fontSizes[i]=loadFont("pixelmix", i+1);
		}
	}
	
	private static BitmapFont loadFont(String name){
		return loadFont(name,1);
	}
	
	private static BitmapFont loadFont(String name, int scale){
		BitmapFont f = new BitmapFont(Gdx.files.internal("fonts/"+name+".fnt"));
		f.getData().setScale(scale);
		f.setUseIntegerPositions(true);
		return f;
	}
}
