/**
 * 
 */
package code.research.controller;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

/**
 * A test for a controller (not used)
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */
public class ControllerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		
		Controller[] controllers = ce.getControllers();
		
		Controller gamePadContr = null;
		for(Controller c : controllers){
			if(c.getType() == Controller.Type.GAMEPAD){
				gamePadContr = c;
				break;
			}
		}
		
		if(gamePadContr == null){
			throw new NullPointerException("No gamepad found.");
		}
		
		System.out.println("Controller initialized.");
		
		Component dPad = gamePadContr.getComponent(Identifier.Axis.POV);
		Component leftStickX = gamePadContr.getComponent(Identifier.Axis.X);
		Component leftStickY = gamePadContr.getComponent(Identifier.Axis.Y);
		Component a = gamePadContr.getComponent(Identifier.Button._0);
		Component b = gamePadContr.getComponent(Identifier.Button._1);
		Component x = gamePadContr.getComponent(Identifier.Button._2);
		Component y = gamePadContr.getComponent(Identifier.Button._3);
		
		float prevData = 0;
		while(gamePadContr.poll()){
			float dPadData = dPad.getPollData();
			float aData = a.getPollData();
			float leftStickXData = leftStickX.getPollData();
			float leftStickYData = leftStickY.getPollData();
			float lsXDtemp = leftStickXData * 100;
			lsXDtemp = (int)lsXDtemp;
			float lsYDtemp = leftStickYData * 100;
			lsYDtemp = (int)lsYDtemp;
			leftStickXData = lsXDtemp/100;
			leftStickYData = lsYDtemp/100;
			if(dPadData != prevData)
				System.out.println("Data from DPad: "+dPadData);
			if(aData != prevData)
				System.out.println("Data from A Button: "+aData);
			/*if(leftStickXData != prevData)
				System.out.println("Left stick: <"+leftStickXData+", "+leftStickYData+">");*/
		}
	}

}
