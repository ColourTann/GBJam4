package game.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuItem extends TextBox{
	int x,y;
	public MenuItem(String text, int x, int y, int width) {
		super(text, width);
		setup(x,y);
	}
	
	public MenuItem(TextureRegion tr, int x, int y){
		super(tr);
		setup(x,y);
	}
	
	private void setup(int x, int y) {
		this.x=x; this.y=y;
		setWrapped(false);
	}

	HashMap<Integer, MenuItem> directionMap = new HashMap<>();
	public void connect(int x, int y, MenuItem item){
		directionMap.put(x*10+y, item);
	}
	
	public MenuItem getConnected(int x, int y){
		return directionMap.get(x*10+y);
	}

	public void select() {
		moused=true;
	}
	
	public void deselect() {
		moused=false;
	}

	

}
