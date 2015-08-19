package game.screens.pause;

import game.Main;
import game.Main.TransitionType;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map.MapType;
import game.screens.testScreens.FontScreen;
import game.screens.testScreens.StartScreen;
import game.util.Border;
import game.util.MenuItem;
import game.util.MenuScreen;
import game.util.Screen;
import game.util.Slider;
import game.util.Sounds;
import game.util.TextBox;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class PauseScreen extends Group{
	private static int w=140,h=120;
	private static PauseScreen self;
	MenuScreen menuScreen;
	public static PauseScreen get(){
		if(self==null)self=new PauseScreen();
		return self;
	}


	private PauseScreen(){
		setSize(w,h);
		setPosition(Main.width/2-w/2, Main.height/2-h/2);
		int width = (w-TextBox.gap*4)/3;
		//		addTransitionButton("boring", StartScreen.get(), (width+TextBox.gap)*0,0, width);
		//		addTransitionButton("clicking", GameScreen.get(), (width+TextBox.gap)*1,0, width);
		//		addTransitionButton("fonts", FontScreen.get(), (width+TextBox.gap)*2,0, width);
		//
		//		int numScales=5;
		//		width= (w-TextBox.gap*(numScales+1))/numScales;
		//		for(int i=0;i<numScales;i++) addScaleButton(i+1, (width+TextBox.gap)*i, 50, width);
		//
		//		Slider.SFX.setPosition(w/2-Slider.SFX.getWidth()/2, 15);
		//		addActor(Slider.SFX);
		//
		//		Slider.music.setPosition(w/2-Slider.SFX.getWidth()/2, 60);
		//		addActor(Slider.music);

		menuScreen=new MenuScreen(10,4);

		int levelY=50;
		int gap =3;
		for(int y=0;y<2;y++){
			final MapType type =y==0?MapType.demolish:MapType.targets;
			TextBox tb = new TextBox(type.toString(), 53);
			tb.setPosition(gap+2, gap+y*19+levelY);
			addActor(tb);
			for(int x = 0 ;x<4;x++){
				final int level = x;
				MenuItem m = new MenuItem((x+1)+"", x+2, y, 16);
				m.setPosition(getWidth()-(19*4)-gap+x*19, gap+y*19+levelY);
				m.addClickAction(new Runnable() {
					@Override
					public void run() {
						Main.self.setScreen(GameScreen.get(), TransitionType.LEFT, Interpolation.pow2Out, .3f);
						GameScreen.get().setMap(type, level);
						Main.self.toggleMenu(false);
					}
				});	
				menuScreen.addItem(m);
			}
		}

		int xGap=10;
		for(int x = 1;x<=9;x+=2){
			final int scale = x;
			MenuItem m = new MenuItem("x"+x, x/2, 2, 20);
			m.addClickAction(new Runnable() {
				@Override
				public void run() {
					Main.self.setScale(scale);
				}
			});
			menuScreen.addItem(m);
			m.setPosition(xGap+(scale/2)*(getWidth()-xGap*2)/5, getHeight()-gap*2-m.getHeight());
			addActor(m);
		}

		menuScreen.setPosition(0, 0);
		addActor(menuScreen);
		
		int multiplayerY=24;
		
		MenuItem linkPic = new MenuItem(Main.atlas.findRegion("link"), 3, 3);
		linkPic.setPosition(getWidth()/2, gap+multiplayerY, Align.center);
		menuScreen.addItem(linkPic);
		linkPic.addClickAction(new Runnable() {
			@Override
			public void run() {
				Main.self.setScreen(GameScreen.get(), TransitionType.LEFT, Interpolation.pow2Out, .3f);
				GameScreen.get().setMap(MapType.ball, 0);
				Main.self.toggleMenu(false);
			}
		});
		addActor(linkPic);
	}

	private void addScaleButton(final int scale, int x, int y, int width){
		TextBox t = new TextBox("X "+scale, width);
		t.makeMouseable();
		t.addClickAction(new Runnable() {
			@Override
			public void run() {
				Main.self.setScale(scale);
			}
		});
		t.setPosition(TextBox.gap+x, -TextBox.gap+(int)(getHeight()-t.getHeight()-y));
		addActor(t);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Border.draw(batch, getX(), getY(), getWidth(), getHeight(), false);
		super.draw(batch, parentAlpha);
	}

	public void keyDown(int keycode) {
		int x =0;
		int y =0;
		switch(keycode){
		case Keys.A:
		case Keys.LEFT:
			x=-1;
			break;
		case Keys.D:
		case Keys.RIGHT:
			x=1;
			break;
		case Keys.S:
		case Keys.DOWN:
			y=-1;
			break;
		case Keys.W:
		case Keys.UP:
			y=1;
			break;
			
		case Keys.Z:
		case Keys.X:
			menuScreen.select();
			break;
		}
		
		menuScreen.navigateMenu(x, y);
	}


	MenuItem currentItem;



}
