package game;

import game.screens.pause.InputBlocker;
import game.screens.pause.PauseScreen;
import game.screens.testScreens.FontScreen;
import game.screens.testScreens.GameScreen;
import game.screens.testScreens.StartScreen;
import game.util.Colours;
import game.util.Fonts;
import game.util.Screen;
import game.util.Slider;
import game.util.Sounds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class Main extends ApplicationAdapter {
	public static int width=400,height=320;
	SpriteBatch batch;
	Stage stage;
	OrthographicCamera cam;
	public static TextureAtlas atlas;
	public static Main self;
	public static int scale=1;
	Screen currentScreen;
	Screen previousScreen;
	public enum MainState{Normal, Paused}
	@Override
	public void create () {
		self=this;
		
		Sounds.setup();
		Fonts.setup();
		
		
		
		atlas= new TextureAtlas(Gdx.files.internal("atlas_image.atlas"));
		stage = new Stage();
		cam =(OrthographicCamera) stage.getCamera();
		batch = (SpriteBatch) stage.getBatch();
		Gdx.input.setInputProcessor(stage);
		
		
		setScreen(new StartScreen());
//		setScreen(new FontScreen());
		


		stage.addListener(new InputListener(){
			public boolean keyDown (InputEvent event, int keycode) {
				
				switch(keycode){
				case Keys.ESCAPE:
					toggleMenu();
					return false;
				}
				
				return true;
			}

			
		});
	}
	
	public void setScale(int scale){
		Main.scale=scale;
		int newWidth = width*scale;
		int newHeight= height*scale;
		Gdx.graphics.setDisplayMode(newWidth, newHeight, false);
		stage.getViewport().update(newWidth, newHeight);
	}
	
	public void toggleMenu() {
		if(state!=MainState.Paused){
			stage.addActor(InputBlocker.get());
			stage.addActor(PauseScreen.get());
			setState(MainState.Paused);
		}
		else {
			InputBlocker.get().remove();
			PauseScreen.get().remove();
			setState(MainState.Normal);
		}

	}
	
	
	
	private MainState state=MainState.Normal;
	public MainState getState(){
		return state;
	}
	public void setState(MainState state){
		this.state=state;
	}
	public enum TransitionType{LEFT};
	public void setScreen(Screen screen, TransitionType type, Interpolation interp, float speed){
		if(screen==currentScreen)return;
		setScreen(screen);
		switch(type){
		case LEFT:
			screen.setPosition(Main.width, 0);
//			Action a = ;
			screen.addAction(Actions.moveTo(0, 0, speed, interp));
			previousScreen.addAction(Actions.moveTo(-Main.width, 0, speed, interp));
			break;
		}
		previousScreen.addAction(Actions.after(Actions.removeActor()));
	}
	
	public void setScreen(Screen screen){
		previousScreen=currentScreen;
		currentScreen=screen;
		stage.addActor(screen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(Colours.dark.r, 1, Colours.dark.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
		stage.draw();
		batch.begin();
		drawFPS(batch);
		batch.end();
	}
	
	public void drawFPS(Batch batch){
		Fonts.font.setColor(Colours.green);
		Fonts.font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 0, stage.getHeight());
	}
	
	
	public void update(float delta){
		Sounds.tickFaders(delta);
		stage.act(delta);
	}

}
