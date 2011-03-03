package code.model.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import code.util.Coordinate;
import code.util.DatabaseHandler;

public class AutoCompletionHandler{
	private ArrayList<Coordinate> path;
	private DatabaseHandler db;
	private ResultSet rs;
	private Coordinate start;
	private Coordinate finish;
	private int counter;
	private Player player;
	private boolean active;
	private boolean walking;
	
	private boolean enabled = false;
	
	public static final int FORWARD = 0;
	public static final int ROTLEFT = 1;
	public static final int ROTRIGHT = 2;
	public static final int GETOFFPJ = 3;
	public static final int GETONPJ = 4;
	public static final int PICKUPBOX = 5;
	public static final int PUTDOWNBOX = 6;
	
	public AutoCompletionHandler(Player player, int pickjob, int start, int finish){
		if(!enabled) return;
		this.path = new ArrayList<Coordinate>();
		try{
			db = new DatabaseHandler("joseph.cedarville.edu", "vwburr", "warehouse", "vwburr15");
			rs = db.executeQuery("SELECT * FROM PICKWAYPOINT WHERE pickjob = 1");
			
			while(rs.next()){
				Coordinate c = new Coordinate(rs.getInt(2), rs.getInt(3), rs.getInt(8));
				path.add(c);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//TODO: Remove Test Code
		/*this.path.add(new Coordinate(10.0f, -5.0f));
		this.path.add(new Coordinate(2.5f, -5.0f));
		this.path.add(new Coordinate(2.5f, -9.17f));*/
		this.start = this.path.get(0);
		this.finish = this.path.get(5);		
		
		//this.counter = path.indexOf(this.start);
		this.counter = 0;
		this.player = player;
		this.walking = false;
		this.active = false;
	}
	
	public boolean isActive(){return this.active;}
	public void activate(){
		if(!enabled) return;
		this.active = true;
		if(!player.inVehicle())
			player.setLocalTranslation(start.getX(), .1f, start.getZ());
		else player.getVehicleBeingUsed().setLocalTranslation(start.getX(), .1f, start.getZ());
		this.counter++;
		return;
	}
	
	public void deactivate(){
		if(!enabled) return;
		this.active = false;
		advance();
		return;
	}
	
	public void advance(){
		if(!enabled) return;
		int next = finish.getNext();
		this.start = path.get(next);
		this.finish = path.get(start.getNext()-1);		
		this.counter = path.indexOf(start);
	}
	
	public void update(){
		if(!enabled) return;
		if(!active) return;
		
		player.getDebugHud().setAutoCount(counter);
		
		Coordinate c = path.get(this.counter);
		
		float x = c.getX();
		float z = c.getZ();
		
		float myX, myZ;
		
		if(!player.inVehicle()){
			myX = player.getLocalTranslation().getX();
			myZ = player.getLocalTranslation().getZ();
		}
		else{
			myX = player.getVehicleBeingUsed().getLocalTranslation().getX();
			myZ = player.getVehicleBeingUsed().getLocalTranslation().getZ();
		}
		
		float playerDirection = -(FastMath.atan2(myZ - z, myX - x));
		
		if(!player.inVehicle()){
			player.setLocalRotation(new Quaternion().fromAngleAxis(playerDirection, Vector3f.UNIT_Y));
		}
		else{
			player.getVehicleBeingUsed().setLocalRotation(new Quaternion().fromAngleAxis(playerDirection-1.55f, Vector3f.UNIT_Y));
		}
		
		walking = false;
		
		if ((Math.abs(myX - x) < .1f) && (Math.abs(myZ - z) < .1f)) {
			this.counter++;
		} else if(!player.inVehicle()){

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
		else{
			walking = false;
			if ((myX < x) && (Math.abs(myX - x) > .07f)) {
				player.getVehicleBeingUsed().setLocalTranslation(myX + .035f, .1f, myZ);

			} else if ((myX > x) && (Math.abs(myX - x) > .07f)) {
				player.getVehicleBeingUsed().setLocalTranslation(myX - .035f, .1f, myZ);
			}

			if ((myZ < z) && Math.abs(myZ - z) > .07f) {
				player.getVehicleBeingUsed().setLocalTranslation(myX, .1f, myZ + .035f);
			} else if ((myZ > z) && Math.abs(myZ - z) > .07f) {
				player.getVehicleBeingUsed().setLocalTranslation(myX, .1f, myZ - .035f);
			}
		}
		if(counter > path.indexOf(finish)){
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
