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
		
		//TODO: Remove Test Code
		this.path = new ArrayList<Coordinate>();
		this.path.add(new Coordinate(10.0f, -5.0f));
		this.path.add(new Coordinate(2.5f, -5.0f));
		this.path.add(new Coordinate(2.5f, -9.17f));
		this.start = this.path.get(0);
		this.finish = this.path.get(2);		
		
		//this.counter = path.indexOf(this.start);
		this.counter = 0;
		this.player = player;
		this.walking = false;
		this.active = false;
	}
	
	public boolean isActive(){return this.active;}
	public void activate(){
		this.active = true;
		player.setLocalTranslation(start.getX(), .1f, start.getZ());
		this.counter++;
	}
	
	public void deactivate(){this.active = false;}
	
	public void update(){
		if(!active) return;
		
		Coordinate c = path.get(this.counter);
		
		float x = c.getX();
		float z = c.getZ();
		
		float myX = player.getLocalTranslation().getX();
		float myZ = player.getLocalTranslation().getZ();
		
		float playerDirection = -(FastMath.atan2(myZ - z, myX - x));
		player.setLocalRotation(new Quaternion().fromAngleAxis(playerDirection, Vector3f.UNIT_Y));
		
		walking = false;
		
		if ((Math.abs(myX - x) < .1f) && (Math.abs(myZ - z) < .1f)) {
			this.counter++;
		} else {

			if ((myX < x) && (Math.abs(myX - x) > .07f)) {
				walking = true;
				player.setLocalTranslation(myX + .035f, .1f, myZ);

			} else if ((myX > x) && (Math.abs(myX - x) > .07f)) {
				walking = true;
				player.setLocalTranslation(myX - .035f, .1f, myZ);
			}

			if ((myZ < z) && Math.abs(myZ - z) > .07f) {
				walking = true;
				player.setLocalTranslation(myX, .1f, myZ + .035f);
			} else if ((myZ > z) && Math.abs(myZ - z) > .07f) {
				walking = true;
				player.setLocalTranslation(myX, .1f, myZ - .035f);
			}
		}
		if(counter >= path.indexOf(finish)){
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
