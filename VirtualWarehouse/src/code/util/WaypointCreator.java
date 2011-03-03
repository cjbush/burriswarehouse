package code.util;

import java.sql.SQLException;

import code.app.VirtualWarehouse;
import code.app.WarehouseTrainer;
import code.model.player.AutoCompletionHandler;
import code.model.player.Player;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

public class WaypointCreator {
	private DatabaseHandler db;
	private KeyBindingManager k;
	private Player player;
	private int sequence;
	private int section;
	private int action;
	
	public static final int NO_ACTION = -1;
	
	public static boolean enabled;
	
	public WaypointCreator(Player player){
		enabled = VirtualWarehouse.DEBUG_MODE;
		if(!enabled) return;
		k = KeyBindingManager.getKeyBindingManager();
		k.set("addWaypoint", KeyInput.KEY_INSERT);
		k.set("addPickupWaypoint", KeyInput.KEY_HOME);
		k.set("addPutdownWaypoint",KeyInput.KEY_PGUP);
		k.set("addGetOffPJWaypoint", KeyInput.KEY_END);
		k.set("addGetOnPJWaypoint", KeyInput.KEY_PGDN);
		k.set("nextSection", KeyInput.KEY_DELETE);
		db = new DatabaseHandler("joseph.cedarville.edu", "PICKWAYPOINT", "warehouse", "vwburr15");
		sequence = 0;
		section = 1;
		this.player = player;
	}
	
	public void update(){
		if(!enabled) return;
		
		float x, z;
		int action = NO_ACTION;
		
		if(player.inVehicle()){
			x = player.getVehicleBeingUsed().getLocalTranslation().getX();
			z = player.getVehicleBeingUsed().getLocalTranslation().getZ();
		}
		else{
			x = player.getLocalTranslation().getX();
			z = player.getLocalTranslation().getZ();
		}
		
		if(k.isValidCommand("addWaypoint")){
			action = AutoCompletionHandler.FORWARD;
			sequence++;
		}
		if(k.isValidCommand("addPickupWaypoint")){
			action = AutoCompletionHandler.PICKUPBOX;
			sequence++;
		}
		if(k.isValidCommand("addPutdownWaypoint")){
			action = AutoCompletionHandler.PUTDOWNBOX;
			sequence++;
		}
		if(k.isValidCommand("addGetOffPJWaypoint")){
			action = AutoCompletionHandler.GETOFFPJ;
			sequence++;
		}
		if(k.isValidCommand("addGetOnPJWaypoint")){
			action = AutoCompletionHandler.GETONPJ;
			sequence++;
		}
		if(k.isValidCommand("nextSection")){
			section++;
			sequence = 0;
		}
		
		if(action != NO_ACTION){
			String query = "INSERT INTO PICKWAYPOINT(x, z, section, sequence, action) VALUES("+x+", "+z+", "+ section + ", " + sequence + ", " + action +");";
			System.out.println(query);
			/*try {
				db.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
	}
}
