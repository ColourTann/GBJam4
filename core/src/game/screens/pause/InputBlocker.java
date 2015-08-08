package game.screens.pause;

import game.Main;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class InputBlocker extends Actor{
	private InputBlocker() {
		setSize(Main.width, Main.height);
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return false;
			}
		});
	}
	private static InputBlocker self;
	public static Actor get() {
		if(self==null)self=new InputBlocker();
		return self;
	}
}
