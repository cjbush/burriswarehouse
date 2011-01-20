package code.model.player;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

/**
 * A Third Person Input Handler.
 * @author VirtualVille Team (Richard Bradt, Gabe Greve, Eric Smith, and Ben Wiley)
 * Modified by VirtualWarehouse team
 */
public class PlayerHandler extends InputHandler {
	
	// Directions
	public static final int DIRECTION_NONE = 0;
	public static final int DIRECTION_FORWARD = 1;
	public static final int DIRECTION_BACKWARD = 2;
	
	// Rotations
	public static final int ROTATION_NONE = 0;
	public static final int ROTATION_RIGHT = 1;
	public static final int ROTATION_LEFT = 2;
	
	// Speeds
	public static final float DEFAULT_SPEED = 1f;
	public static final float DEFAULT_TURN_SPEED = 2;
	
	protected float speed;
	protected float turnSpeed;
	protected boolean canWalk;
	
	// Temporary variables to handle rotation
    protected static final Matrix3f incr = new Matrix3f();
    protected static final Matrix3f tempMa = new Matrix3f();
    protected static final Matrix3f tempMb = new Matrix3f();
    public static final Vector3f UP = new Vector3f(0, 1, 0);
	
	protected Player player;
	
	protected boolean walking = false;
	
	protected int rotation;
	protected int direction;
	
	//Controller variables
	private Controller gamePad;
	
	private Component dPad;
	private Component leftStickX;
	private Component leftStickY;
	
	private Component rightStickX;
	private Component rightStickY;
	private Component a;
	private Component b;
	private Component x;
	private Component y;
	
	private Component lt;
	private Component rt;
	private Component lb;
	private Component rb;
	private Component start;
	private Component back;
	
	private boolean enabled;
	
	/**
	 * A third person input handler with default parameters to control the specified player.
	 * @param player
	 */
	public PlayerHandler(Player player) {
        this.player = player;
        
        speed = DEFAULT_SPEED;
        turnSpeed = DEFAULT_TURN_SPEED;
        
        KeyBindingManager.getKeyBindingManager().set("up", KeyInput.KEY_W);
        KeyBindingManager.getKeyBindingManager().set("down", KeyInput.KEY_S);
        KeyBindingManager.getKeyBindingManager().set("left", KeyInput.KEY_A);
        KeyBindingManager.getKeyBindingManager().set("right", KeyInput.KEY_D);
        KeyBindingManager.getKeyBindingManager().set("switch", KeyInput.KEY_K);
        KeyBindingManager.getKeyBindingManager().set("enter_exit_vehicle", KeyInput.KEY_T);
		KeyBindingManager.getKeyBindingManager().set("pickup_place_product", KeyInput.KEY_Y);
        
        ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] controllers = ce.getControllers();
		for(Controller c : controllers){
			if(c.getType() == Controller.Type.GAMEPAD){
				gamePad = c;
				enabled = true;
				break;
			}
		}
		
		if(gamePad == null){
			enabled = false;
			return;
		}
		
		System.out.println("Controller initialized.");
		
		dPad = gamePad.getComponent(Identifier.Axis.POV);
		leftStickX = gamePad.getComponent(Identifier.Axis.X);
		leftStickY = gamePad.getComponent(Identifier.Axis.Y);
		rightStickX = gamePad.getComponent(Identifier.Axis.RX);
		rightStickY = gamePad.getComponent(Identifier.Axis.RY);
		a = gamePad.getComponent(Identifier.Button._0);
		b = gamePad.getComponent(Identifier.Button._1);
		x = gamePad.getComponent(Identifier.Button._2);
		y = gamePad.getComponent(Identifier.Button._3);
		
		System.out.println("Controller buttons mapped.");
    }
	
    public void update(float time) {
       /* if (!isEnabled()) {
        	walking = false;
        	return;
        }*/
        
        super.update(time);
        
        rotation = ROTATION_NONE;
        direction = DIRECTION_NONE;
        
        walking = false;
        
        float dPadData = 0;
		float aData = 0;
		float bData = 0;
		float xData = 0;
		float yData = 0;
        
        if(gamePad != null){
    		gamePad.poll();
	    	dPadData = dPad.getPollData();
			aData = a.getPollData();
			bData = b.getPollData();
			xData = x.getPollData();
			yData = y.getPollData();
        }
        
        // First check forward or backward
        if(KeyBindingManager.getKeyBindingManager().isValidCommand("up", true) || (dPadData <= 0.375 && dPadData > 0)) {
            direction = DIRECTION_FORWARD;
            if(dPadData == 0){}
            else if(dPadData == 0.25){
            	rotation = ROTATION_NONE;
            }
            else if(dPadData <= 0.125){
            	rotation = ROTATION_LEFT;
            }
            else if(dPadData <= 0.375){
            	rotation = ROTATION_RIGHT;
            }
            walking = true;
        }
        
        /*else if(KeyBindingManager.getKeyBindingManager().isValidCommand("down", true)) {
            direction = DIRECTION_BACKWARD;
            walking = true;
        }*/
        
        // Next check turning (rotation)
        if(KeyBindingManager.getKeyBindingManager().isValidCommand("right", true) || (dPadData <= 0.625 && dPadData > 0.375)) {
            rotation = ROTATION_RIGHT;
            walking = true;
        }
        
        else if(KeyBindingManager.getKeyBindingManager().isValidCommand("left", true) || (dPadData <= 1.0 && dPadData > 0.375)) {
        	rotation = ROTATION_LEFT;
            walking = true;
        }
        
        if(aData != 0){
        	direction = DIRECTION_NONE;
        	rotation = ROTATION_NONE;
        	player.checkVehicleEnterExit(true);
        }
        
        
        if(!walking){
			//System.out.println("Stationary.");
			player.stationaryAnim();
		}
		else{
			move(time, direction, rotation);
			if(direction == DIRECTION_FORWARD){
				player.translateForwardAnim();
			}
			else if(direction == DIRECTION_BACKWARD){
				player.translateBackwardAnim();
			}
		}
    }
    
	public void move(float time, int direction, int rotation) {
		//Vector3f pos = player.getLocalTranslation();
		//System.out.println("Player is currently at: "+pos.x+" "+pos.y+" "+pos.z);
		if(direction == DIRECTION_FORWARD) {
			// Move forward
			player.getLocalTranslation().addLocal(player.getLocalRotation().getRotationColumn(0)
				.multLocal(0 - (speed * time)));
			
			// Rotate
			switch(rotation) {
			case ROTATION_RIGHT:
				incr.fromAngleNormalAxis(0 - (turnSpeed * time), UP);
				break;
			case ROTATION_LEFT:
				incr.fromAngleNormalAxis(turnSpeed * time, UP);
				break;				
			}
		}
		
		else if(direction == DIRECTION_BACKWARD) {
			// Move backward
			player.getLocalTranslation().addLocal(player.getLocalRotation().getRotationColumn(0)
					.multLocal(0 + (speed * time)));
			
			// Rotate
			switch(rotation) {
			case ROTATION_RIGHT:
				incr.fromAngleNormalAxis(turnSpeed * time, UP);
				break;
			case ROTATION_LEFT:
				incr.fromAngleNormalAxis(0 - (turnSpeed * time), UP);
				break;				
			}
		}
		
		else {
			// Just Rotate
			switch(rotation) {
			case ROTATION_RIGHT:
				incr.fromAngleNormalAxis(0 - (turnSpeed * time), UP);
				break;
			case ROTATION_LEFT:
				incr.fromAngleNormalAxis(turnSpeed * time, UP);
				break;				
			}
		}
		
		if(rotation != ROTATION_NONE && !player.inVehicle()) {
			player.getLocalRotation().fromRotationMatrix(
					incr.mult(player.getLocalRotation().toRotationMatrix(
							tempMa), tempMb));
			player.getLocalRotation().normalize();
		}
	}
}
