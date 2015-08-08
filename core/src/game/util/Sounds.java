package game.util;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

public class Sounds {


	public static AssetManager am= new AssetManager();

	public static void setup(){
		//sfx//
		makeSound("sfx/win.wav", Sound.class);
		
		//music//
		makeSound("sfx/ambience.ogg", Music.class);
		
		//stuff to attempt to load sounds properly//
		am.finishLoading();
		Array<Sound> sounds = new Array<Sound>();
		am.getAll(Sound.class, sounds);
		for(Sound s:sounds)s.play(0);
		Array<Music> musics = new Array<Music>();
		am.getAll(Music.class, musics);
		for(Music m:musics){
			m.play();
			m.setVolume(1);
			m.stop();
		}
	}
	
	public static <T> T get(String name, Class<T> type){
		name="sfx/"+name;
		if(type==Sound.class) name=name+".wav";
		if(type==Music.class) name=name+".ogg";
		return am.get(name, type);
	}
	
	private static void makeSound(String path, Class type){
		am.load(path, type);
	}
	
	private static ArrayList<Fader> faders = new ArrayList<Sounds.Fader>();
	
	public static void fade(Music m, float targetVolume, float duration){
		faders.add(new Fader(m, targetVolume, duration));
	}
	
	public static void tickFaders(float delta){
		for(int i=faders.size()-1;i>=0;i--){
			Fader f = faders.get(i);
			f.tick(delta);
			if(f.done)faders.remove(f);
		}
	}
	
	static class Fader{
		float startVolume;
		float targetVolume;
		Music music;
		boolean done;
		float duration;
		float ticks;
		public Fader(Music m, float targetVolume, float duration) {
			this.startVolume=m.getVolume();
			this.targetVolume=targetVolume;
			this.music=m;
			this.duration=duration;
		}
		public void tick(float delta){
			ticks+=delta;
			if(ticks>duration){
				ticks=duration;
				done=true;
			}
			float ratio = ticks/duration;
			float newVolume =startVolume+(targetVolume-startVolume)*ratio;
			System.out.println(newVolume);
			music.setVolume(newVolume);
		}
	}
}
