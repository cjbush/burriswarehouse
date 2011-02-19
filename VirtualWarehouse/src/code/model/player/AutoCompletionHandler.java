package code.model.player;

import java.util.ArrayList;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import code.util.Coordinate;

public class AutoCompletionHandler{
	private ArrayList<Coordinate> path;
	private Coordinate start;
	private Coordinate finish;
	private int counter;
	private Player player;
	private boolean active;
	private boolean walking;
	
	public AutoCompletionHandler(Player player, ArrayList<Coordinate> path, Coordinate start, Coordinate finish){
		this.start = start;
		this.finish = finish;
		this.path = path;
		this.counter = path.indexOf(start);
		this.player = player;
		this.walking = false;
	}
	
	public boolean isActive(){return active;}
	public void activate(){active = true;}
	public void deactivate(){active = false;}
	
	public void update(){
		if(!active) return;
		
		Coordinate c = path.get(this.counter);
		
		float x = c.getX();
		float z = c.getZ();
		
		float playerX = player.getLocalTranslation().getX();
		float playerZ = player.getLocalTranslation().getY();
		
		float playerDirection = -(FastMath.atan2(playerZ - z, playerX - x));
		player.setLocalRotation(new Quaternion().fromAngleAxis(playerDirection, Vector3f.UNIT_Y));
		
		if((Math.abs(playerX - x) < 0.1f) && (Math.abs(playerZ - z) < 0.1f)){
			this.counter++;
		}
		else{
			if ((playerX < x) && (Math.abs(playerX - x) > .07f)) {
				walking = true;
				player.setLocalTranslation(playerX + .035f, .1f, playerZ);

			} else if ((playerX > x) && (Math.abs(playerX - x) > .07f)) {
				walking = true;
				player.setLocalTranslation(playerX - .035f, .1f, playerX);
			}

			if ((playerZ < z) && Math.abs(playerZ - z) > .07f) {
				walking = true;
				player.setLocalTranslation(playerX, .1f, playerZ + .035f);
			} else if ((playerZ > z) && Math.abs(playerZ - z) > .07f) {
				walking = true;
				player.setLocalTranslation(playerX, .1f, playerZ - .035f);
			}
		}
		if(counter == path.indexOf(finish)){
			deactivate();
		}
		if(!walking){
			player.stationaryAnim();
		}
		else{
			player.translateForwardAnim();
		}
	}
}
