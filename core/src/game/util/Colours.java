package game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Colours {

	// DB16 Palette//
	public static final Color dark;
	public static final Color greenDark;
	public static final Color green;
	public static final Color yellow;
	
	public static final Color brownRusty;
	public static final Color brown;
	public static final Color orange;
	public static final Color red;
	
	public static final Color blueDark;
	public static final Color blue;
	public static final Color blueLight;
	public static final Color pink;
	
	public static final Color greyDark;
	public static final Color grey;
	public static final Color yellowDark;
	public static final Color light;
	
	public static final Color white = new Color(1,1,1,1);
	private static Pixmap p;
	static{
		Texture t = new Texture(Gdx.files.internal("palette.png"));
		p = Draw.getPixmap(t);
		dark = palette(0,0);
		greenDark = palette(0,1);
		green= palette(0,2);
		yellow = palette(0,3);
		
		brownRusty= palette(1,0);
		brown = palette(1,1);
		orange= palette(1,2);
		red = palette(1,3);
		
		blueDark= palette(2,0);
		blue = palette(2,1);
		blueLight= palette(2,2);
		pink = palette(2,3);
		
		greyDark= palette(3,0);
		grey = palette(3,1);
		yellowDark= palette(3,2);
		light = palette(3,3);
	}
	
	public static Color palette(int x, int y){
		return new Color(p.getPixel(x, y));
	}
	
	public static Color withAlpha(Color c, float alpha) {
		return new Color(c.r, c.g, c.b, alpha);
	}

	public static Color shiftedTowards(Color source, Color target, float amount) {
		if (amount > 1)
			amount = 1;
		if (amount < 0)
			amount = 0;
		float r = source.r + ((target.r - source.r) * amount);
		float g = source.g + (target.g - source.g) * amount;
		float b = source.b + (target.b - source.b) * amount;
		return new Color(r, g, b, 1);
	}

	public static Color multiply(Color source, Color target) {
		return new Color(source.r * target.r, source.g * target.g, source.b
				* target.b, 1);
	}

	private static Color make(int r, int g, int b) {
		return new Color((float) (r / 255f), (float) (g / 255f),
				(float) (b / 255f), 1);
	}

	public static Color monochrome(Color c) {
		float brightness = (c.r + c.g + c.b) / 3;
		return new Color(brightness, brightness, brightness, c.a);
	}

	public static boolean equals(Color a, Color b) {
		return a.a == b.a && a.r == b.r && a.g == b.g && a.b == b.b;
	}

	public static boolean wigglyEquals(Color a, Color aa) {
		float r = Math.abs(a.r - aa.r);
		float g = Math.abs(a.g - aa.g);
		float b = Math.abs(a.b - aa.b);
		float wiggle = .01f;
		return r < wiggle && g < wiggle && b < wiggle;
	}
	
	public static void setBatchColour(Batch batch, Color c, float a) {
		batch.setColor(c.r, c.g, c.b, a);
	}
}