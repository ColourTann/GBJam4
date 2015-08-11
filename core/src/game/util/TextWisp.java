package game.util;

import java.lang.reflect.GenericArrayType;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

public class TextWisp extends Particle{
	public String text;
	public TextWisp(String text, int x, int y) {
		this.x=x;
		this.y=y;
		this.text=text;
		setupLife(.5f);
	}
	static float speed=8;
	@Override
	public void tick(float delta) {
		y+=delta*speed;
	}
	
	public void refresh(){
		setupLife(.5f);
	}
	
	public void setText(String text){
		this.text=text;
		refresh();
	}

	@Override
	public void draw(Batch batch) {
		Fonts.font.setColor(Colours.light);
		Fonts.font.draw(batch, text, x, y, 0, Align.center, false);
	}
	

}
