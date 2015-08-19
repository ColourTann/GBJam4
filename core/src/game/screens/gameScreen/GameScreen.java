package game.screens.gameScreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

import game.Main;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.map.Map.MapType;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Colours;
import game.util.Draw;
import game.util.Fonts;
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
	}
	
	
	public void setMap(MapType type, int levelNum){
		if(map!=null){
			map.dispose();
			removeActor(map);
		}
		this.levelNum=levelNum;
		map = new Map(type, levelNum);
		addActor(map);
	}



	@Override
	public void preDraw(Batch batch) {
		
	}
	@Override
	public void postDraw(Batch batch) {
		
		batch.setColor(Colours.green[0]);
		if(map!=null)map.drawStats(batch);
		if(map==null){
			String s = "BASH\nesc to access menu\nx to select";
			Fonts.font.draw(batch, s, Main.width/2, Main.height/2, 0, Align.center, false);
			
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
		
	}

	public void win() {

		
	}



}

