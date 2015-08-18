package game.screens.gameScreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;

import game.Main;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Colours;
import game.util.Draw;
import game.util.Screen;

public class GameScreen extends Screen{

	
	
	
	int levelNum=0;
	public static GameScreen self;
	Map map;
	public static GameScreen get(){
		if(self==null)self=new GameScreen();
		return self;
	}

	public GameScreen() {
		setSize(500, 500);
		self=this;
		nextLevel();
	}
	
	
	public void setMap(int levelNum){
		if(map!=null){
			map.dispose();
			removeActor(map);
		}
		this.levelNum=levelNum;
		map = new Map(levelNum);
		addActor(map);
	}

	private void nextLevel() {
		setMap(levelNum+1);
	}

	@Override
	public void preDraw(Batch batch) {
		
	}
	@Override
	public void postDraw(Batch batch) {
		batch.setColor(Colours.green[0]);
		int offset = 2;
		int size = 7;
		int gap = size+3;
		for(int x = 0 ;x<map.currentPlayer.getHP();x++){
			Draw.fillRectangle(batch, Main.width-(size+offset+x*gap), Main.height-offset-size, size, size);
		}
	}
	
	
	@Override
	public void preTick(float delta) {
	}	
		
	@Override
	public void postTick(float delta) {		
	}

	@Override
	public void keyDown(int keycode) {
		if(keycode== Keys.SPACE){
			map.addEnemy();
		}
		
	}

	public void win() {

		nextLevel();
	}



}

