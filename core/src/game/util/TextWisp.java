package game.util;

import java.lang.reflect.GenericArrayType;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

public class TextWisp extends Particle{
	String text;
	public TextWisp(String text, int x, int y) {
		this.x=x;
		this.y=y;
		this.text=text;
		setupLife(.5f);
	}
	static float speed=5;
	@Override
	public void tick(float delta) {
		y+=delta*speed;
	}

	@Override
	public void draw(Batch batch) {
		Fonts.font.setColor(Colours.light);
		Fonts.font.draw(batch, text, x, y, 0, Align.center, false);
	}
	

}
