package game.util;

import java.util.HashMap;

public class MenuItem extends TextBox{
	int x,y;
	public MenuItem(String text, int x, int y, int width) {
		super(text, width);
		this.x=x; this.y=y;
		makeMouseable();
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
