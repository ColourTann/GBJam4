package game;

import game.screens.gameScreen.GameScreen;
import game.screens.pause.InputBlocker;
import game.screens.pause.PauseScreen;
import game.util.Colours;
import game.util.Draw;
import game.util.Fonts;
import game.util.Screen;
import game.util.Sounds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Main extends ApplicationAdapter {
	public static int width=160,height=144;
	SpriteBatch batch;
	Stage stage;
	public OrthographicCamera cam;
	public static TextureAtlas atlas;
	public static Main self;
	public static int scale=5;
	Screen currentScreen;
	Screen previousScreen;
	public enum MainState{Normal, Paused}
	private static float scaleFactor=8;
	public static float ticks=0;
	private static boolean debug=true;
	public static int m2p(float meters){
		return (int) Math.round(meters*scaleFactor);
	}
	
	public static float p2m(float pixels){
		return pixels/scaleFactor;
	}
	
	@Override
	public void create () {
		self=this;

		Sounds.setup();
		Fonts.setup();
		Box2D.init();

		
		
		
		buffer = new FrameBuffer(Format.RGBA8888, Main.width, Main.height, false);
		atlas= new TextureAtlas(Gdx.files.internal("atlas_image.atlas"));
		stage = new Stage(new FitViewport(Main.width, Main.height));
		cam =(OrthographicCamera) stage.getCamera();
		batch = (SpriteBatch) stage.getBatch();
		Gdx.input.setInputProcessor(stage);


		setScreen(GameScreen.get());

		setScale(scale);

		stage.addListener(new InputListener(){
			public boolean keyDown (InputEvent event, int keycode) {

				
				if(getState()==MainState.Paused){
					PauseScreen.get().keyDown(keycode);
				}
				else currentScreen.keyDown(keycode);

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
		System.out.println(newWidth+":"+newHeight);
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
	FrameBuffer buffer;
	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		
		buffer.bind();
		buffer.begin();
		batch.begin();
		batch.setColor(Colours.green[3]);
		batch.setProjectionMatrix(cam.combined);
		Draw.fillRectangle(batch, 0, 0, Main.width, Main.height);
		batch.end();
		stage.draw();
		batch.begin();

		if(Main.debug)drawFPS(batch);
		batch.end();
		buffer.end();

		batch.begin();
		batch.setColor(1,1,1,1);
		buffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Draw.drawRotatedScaledFlipped(batch, buffer.getColorBufferTexture(), 0, 0, 1, 1, 0, false, true);
		batch.end();


		//		stage.draw();
	}

	public void drawFPS(Batch batch){
		Fonts.font.setColor(Colours.green[0]);
		Fonts.font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 0, stage.getHeight());
	}


	public void update(float delta){
		ticks+=delta;
		Sounds.tickFaders(delta);
		stage.act(delta);
	}

}
