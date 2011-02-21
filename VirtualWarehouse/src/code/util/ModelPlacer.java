package code.util;

import java.util.ArrayList;
import java.util.List;

import code.app.VirtualWarehouse;
import code.model.SharedMeshManager;
import code.world.WarehouseWorld;

import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * ALL COMMANDS ON NUMPAD
 * 		2, 6, 4, 8 	Movement
 * 		7, 9		Rotation
 * 		1, 3		Scale
 * 		0, .		Up and Down
 *      n			Switch between models with the same name
 * 		5			Print location, scale, rotation to console 
 * 
 * @author VirtualVille Team (Richard Bradt, Gabe Greve, Eric Smith, and Ben Wiley)
 * Modified by Virtual Warehouse Team
 */

public class ModelPlacer extends SimpleGame{

	
	private static final String MODEL_TO_PLACE = "racksSingle103";
	
	private static final float MOVE_DIST = .05f;
	private static final float SCALE_FACTOR = .01f;
	
	private WarehouseWorld world;
	
	private int currentModelIndex;
	private ArrayList<Spatial> modelsToPlace;
	
	
	public static void main(String[] args) {
		ModelPlacer app = new ModelPlacer();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}
	
	protected void simpleInitGame() {
		
		VirtualWarehouse vw = new VirtualWarehouse(false);
		vw.setSharedMeshManager(new SharedMeshManager());
		world = new WarehouseWorld(vw);
		rootNode.attachChild(world);

		setupInput();
		
	}
	
	private void setupInput() {		
		
		KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        keyboard.set("posX", KeyInput.KEY_NUMPAD8);
        keyboard.set("negX", KeyInput.KEY_NUMPAD2);
        keyboard.set("posZ", KeyInput.KEY_NUMPAD6);
        keyboard.set("negZ", KeyInput.KEY_NUMPAD4);
        keyboard.set("posY", KeyInput.KEY_NUMPAD0);
        keyboard.set("negY", KeyInput.KEY_PERIOD);
        
        keyboard.set("rotRight", KeyInput.KEY_NUMPAD9);
        keyboard.set("rotLeft", KeyInput.KEY_NUMPAD7);
        
        keyboard.set("scaleUp", KeyInput.KEY_NUMPAD3);
        keyboard.set("scaleDown", KeyInput.KEY_NUMPAD1);
        
        keyboard.set("print", KeyInput.KEY_NUMPAD5);
        
        keyboard.set("nextModel", KeyInput.KEY_N);
        
        KeyInputAction nextModel = new KeyInputAction() {
        	public void performAction(InputActionEvent evt) {
        		currentModelIndex += 1;
        		currentModelIndex %= modelsToPlace.size();
        		modelsToPlace.get(currentModelIndex).unlock();
        		System.out.println("Current model=" + currentModelIndex);
			}
        };
        
        KeyInputAction posX = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().x += MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        KeyInputAction negX = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().x -= MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        KeyInputAction posZ = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().z += MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        KeyInputAction negZ = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().z -= MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        KeyInputAction posY = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().y += MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        KeyInputAction negY = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalTranslation().y -= MOVE_DIST;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalTranslation());
				//}
				go++;
			}
        };
        
        input.addAction(nextModel, "nextModel", false);
        input.addAction(posX, "posX", true);
        input.addAction(negX, "negX", true);
        input.addAction(posZ, "posZ", true);
        input.addAction(negZ, "negZ", true);
        input.addAction(posY, "posY", true);
        input.addAction(negY, "negY", true);
        
        KeyInputAction rotRight = new KeyInputAction() {
        	//temporary variables to handle rotation
            private final Matrix3f incr = new Matrix3f();
            private final Matrix3f tempMa = new Matrix3f();
            private final Matrix3f tempMb = new Matrix3f();

            //we are using +Y as our up
            private Vector3f upAxis = new Vector3f(0,1,0);
            
        	int go = 0;
        	
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					incr.fromAngleNormalAxis(-1 * evt.getTime(), upAxis);
					modelsToPlace.get(currentModelIndex).getLocalRotation().fromRotationMatrix(
			                incr.mult(modelsToPlace.get(currentModelIndex).getLocalRotation().toRotationMatrix(tempMa),
			                        tempMb));
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalRotation());
				//}
				go++;
			}
        };
        
        KeyInputAction rotLeft = new KeyInputAction() {
        	//temporary variables to handle rotation
            private final Matrix3f incr = new Matrix3f();
            private final Matrix3f tempMa = new Matrix3f();
            private final Matrix3f tempMb = new Matrix3f();

            //we are using +Y as our up
            private Vector3f upAxis = new Vector3f(0,1,0);
            
        	int go = 0;
        	
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					incr.fromAngleNormalAxis(evt.getTime(), upAxis);
					modelsToPlace.get(currentModelIndex).getLocalRotation().fromRotationMatrix(
			                incr.mult(modelsToPlace.get(currentModelIndex).getLocalRotation().toRotationMatrix(tempMa),
			                        tempMb));
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalRotation());
				//}
				go++;
			}
        };
        
        input.addAction(rotRight, "rotRight", true);
        input.addAction(rotLeft, "rotLeft", true);
        
        KeyInputAction scaleUp = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalScale().x+=SCALE_FACTOR;
					modelsToPlace.get(currentModelIndex).getLocalScale().y+=SCALE_FACTOR;
					modelsToPlace.get(currentModelIndex).getLocalScale().z+=SCALE_FACTOR;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalScale().z);
				//}
				go++;
			}
        };
        
        KeyInputAction scaleDown = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 4 == 0) {
					modelsToPlace.get(currentModelIndex).getLocalScale().x-=SCALE_FACTOR;
					modelsToPlace.get(currentModelIndex).getLocalScale().y-=SCALE_FACTOR;
					modelsToPlace.get(currentModelIndex).getLocalScale().z-=SCALE_FACTOR;
					//System.out.println(modelsToPlace.get(currentModelIndex).getLocalScale().z);
				//}
				go++;
			}
        };
        
        input.addAction(scaleUp, "scaleUp", true);
        input.addAction(scaleDown, "scaleDown", true);
        
        KeyInputAction print = new KeyInputAction() {
        	int go = 0;
			public void performAction(InputActionEvent evt) {
				//if(go % 5 == 0) {
					System.out.println(modelsToPlace.get(currentModelIndex).getName() + " " + currentModelIndex + ": ");
			        System.out.print("\t" + modelsToPlace.get(currentModelIndex).getLocalTranslation().x + " " 
			        										+ modelsToPlace.get(currentModelIndex).getLocalTranslation().y + " "
			        										+ modelsToPlace.get(currentModelIndex).getLocalTranslation().z);
			        System.out.print("\t\t" + modelsToPlace.get(currentModelIndex).getLocalScale().z);
			        System.out.print("\t\t" + modelsToPlace.get(currentModelIndex).getLocalRotation().x
			        									+ " " + modelsToPlace.get(currentModelIndex).getLocalRotation().y
			        									+ " " + modelsToPlace.get(currentModelIndex).getLocalRotation().z
			        									+ " " + modelsToPlace.get(currentModelIndex).getLocalRotation().w);
			        System.out.println();
			        
				//}
				go++;
			}
        };
        
        input.addAction(print, "print", false);
    }
	

}
