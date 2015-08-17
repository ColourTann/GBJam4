package game.screens.gameScreen;

import java.util.ArrayList;
import java.util.Currency;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

import game.Main;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.CollisionHandler;
import game.screens.gameScreen.physics.entity.Archer;
import game.screens.gameScreen.physics.entity.Mask;
import game.screens.gameScreen.physics.entity.DumbBox;
import game.screens.gameScreen.physics.entity.Entity;
import game.screens.gameScreen.physics.entity.Ship;
import game.util.Draw;
import game.util.Particle;
import game.util.Screen;

public class GameScreen extends Screen{

	
	
	
	
	public static GameScreen self;
	Map map;
	public static GameScreen get(){
		if(self==null)self=new GameScreen();
		return self;
	}

	public GameScreen() {
		setSize(500, 500);
		self=this;
		map = new Map();
		addActor(map);

		
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.


	}
	
	


	@Override
	public void preDraw(Batch batch) {
		
	}
	@Override
	public void postDraw(Batch batch) {
//		
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



}

