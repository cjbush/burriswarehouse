package code.model.player;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

/**
 * A controller interface (used with an XBox 360 controller)
 * This was never fully implemented
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class ControllerHandler extends PlayerHandler{
	private Controller gamePad;
	private boolean enabled;
	
	private static final int BLANK = 0;
	
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
	
	public ControllerHandler(Player player){
		super(player);
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
	
	public void update(float time){
		super.update(time);
		
		if(gamePad != null){ 
			
			gamePad.poll();
		
			float dPadData = dPad.getPollData();
			float aData = a.getPollData();
			float bData = b.getPollData();
			float xData = x.getPollData();
			float yData = y.getPollData();
			
			if(aData != BLANK){
				//Button A handler
				player.checkVehicleEnterExit(false);
			}
			//System.out.println(dPadData);
			if(!walking){
				if(dPadData == 0){
					walking = false;
				}
				else if(dPadData <= 0.125){
					direction = DIRECTION_FORWARD;
					rotation = ROTATION_LEFT;
					walking = true;
				}
				else if(dPadData <= 0.25){
					direction = DIRECTION_FORWARD;
					walking = true;
				}
				else if(dPadData <= 0.375){
					direction = DIRECTION_FORWARD;
					rotation = ROTATION_RIGHT;
					walking = true;
				}
				else if(dPadData <= 0.5){
					rotation = ROTATION_RIGHT;
					walking = true;
				}
				else if(dPadData <= 0.625){
					direction = DIRECTION_BACKWARD;
					rotation = ROTATION_RIGHT;
					walking = true;
				}
				else if(dPadData <= 0.75){
					direction = DIRECTION_BACKWARD;
					walking = true;
				}
				else if(dPadData <= 0.875){
					direction = DIRECTION_BACKWARD;
					rotation = ROTATION_LEFT;
					walking = true;
				}
				else if(dPadData <= 1.0){
					rotation = ROTATION_LEFT;
					walking = true;
				}
			}
		}
		
		if(!walking){
			//System.out.println("Stationary.");
			player.stationaryAnim();
		}
		else{
			super.move(time, direction, rotation);
			if(direction == DIRECTION_FORWARD){
				super.player.translateForwardAnim();
			}
			else if(direction == DIRECTION_BACKWARD){
				player.translateBackwardAnim();
			}
		}
	}
}
