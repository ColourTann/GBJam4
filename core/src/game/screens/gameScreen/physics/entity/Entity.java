package game.screens.gameScreen.physics.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import game.Main;
import game.screens.gameScreen.GameScreen;
import game.screens.gameScreen.map.Map;
import game.screens.gameScreen.physics.entity.controller.Controller;
import game.screens.testScreens.Orbiter;
import game.util.TextWisp;

public abstract class Entity extends Actor{
	Controller controller;
	TextWisp currentWistp;
	Body bod;
	Body otherBod;
	public boolean dead;
	int hp=500;

	int maxHp;
	
	public void setController(Controller controller){
		this.controller=controller;
	}
	
	public void act(float delta){
		if(dead)return;
		if(controller!=null)controller.act(delta);
		updatePosition();
		postAct(delta);
	}
	
	public void updatePosition(){
		if(bod!=null)setPosition(Main.m2p(bod.getPosition().x), Main.m2p(bod.getPosition().y));
	}
	public abstract void postAct(float delta);
	
	public void defaultDamage(int damage){
		if(dead)return;
		if(damage==0)return;
		for(int i=0;i<Math.min(25, Math.pow(damage/2, 2.3)/2);i++){
			GameScreen.get().addParticle(new Orbiter(Main.m2p(bod.getPosition().x), Main.m2p(bod.getPosition().y)));
		}
		

		if(currentWistp!=null&&currentWistp.ratio>0){
			currentWistp.setText(""+(Integer.parseInt(currentWistp.text)+damage));
		}
		else {

			currentWistp=new TextWisp(damage+"", (int)getX(), (int)getY());
			GameScreen.get().addParticle(currentWistp);
		}
		hp-=damage;
		if(hp<=0) kill();
	}
	
	public void kill(){
		remove();
		Map.self.destroyBody(bod);
		if(otherBod!=null)Map.self.destroyBody(otherBod);
		dead=true;
	}
	
	public void moveTowardsPlayer(float force){
		if(dead)return;
		Entity player = Map.getPlayer();
		Vector2 dv =player.bod.getPosition().sub(bod.getPosition()).nor();
		bod.applyForceToCenter(dv.x*force, dv.y*force, true);
	}
	
	

	public void setHP(int hp){
		this.hp=hp;
		this.maxHp=hp;
	}
	
	
	
	
}
