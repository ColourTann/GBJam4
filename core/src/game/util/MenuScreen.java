package game.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MenuScreen extends Group{

	MenuItem currentItem;
	MenuItem[][] items;
	public MenuScreen(int w, int h) {
		items = new MenuItem[w][h];
	}

	public void addItem(MenuItem item){
		if(currentItem==null){
			currentItem=item;
			item.select();
		}
		addActor(item);
		items[item.x][item.y]=item;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	public void navigateMenu(int dx, int dy){
		navigateTo(findItem(currentItem.x+dx, currentItem.y+dy, dx));
	}


	public MenuItem findItem(int fromX, int fromY, int dx){
		if(fromY>items[0].length-1||fromY<0) return findItem(fromX, fromY>0?0:items[0].length-1, 0);	
		if(dx!=0){
			for(int newX=fromX;newX>=0&&newX<items.length;newX+=dx){
				MenuItem potential = items[newX][fromY];
				if(potential!=null)return potential;
			}
			
			return findItem(dx==1?0:items.length-1, fromY, dx);
		}
		if(dx==0){
			
			for(int change=0;true;change++){
				for(int i=-1;i<=1;i+=2){
					int newX = fromX + i*change;
					if(newX>=items.length||newX<0)continue;
					MenuItem potential = items[newX][fromY];
					if(potential!=null)return potential;
				}
			}
		}

		return null;


	}

	private void navigateTo(MenuItem item){
		if(item==null||item==currentItem)return;
		currentItem.deselect();
		currentItem=item;
		currentItem.select();
	}

	public void select() {
		currentItem.r.run();
	}

}
