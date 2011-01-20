package code.component;

import java.util.HashMap;

import code.app.VirtualWarehouse;

import com.jme.bounding.BoundingBox;
import com.jme.input.ChaseCamera;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class WarehouseChaseCam extends ChaseCamera {

	private VirtualWarehouse warehouseGame;
	private Camera camera;
	
	public WarehouseChaseCam(Camera cam, Spatial target, VirtualWarehouse vw) {
		super(cam, target);
		
		warehouseGame = vw;
		camera = cam;
		
		//set some properties for the chase camera
        HashMap<String, Object> chaserProps = new HashMap<String, Object>();
        chaserProps.put(ChaseCamera.PROP_ENABLESPRING, "true");
        chaserProps.put(ChaseCamera.PROP_DAMPINGK, "10.0");
        chaserProps.put(ChaseCamera.PROP_SPRINGK, "25.0");
        chaserProps.put(ChaseCamera.PROP_MAXDISTANCE, "120.0");
        //chaserProps.put(ChaseCamera.PROP_MAXDISTANCE, "5.0");
        chaserProps.put(ChaseCamera.PROP_MINDISTANCE, "0.0");
        chaserProps.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(3.0f, 0f, FastMath.DEG_TO_RAD * 30.000002f));
        chaserProps.put(ChaseCamera.PROP_STAYBEHINDTARGET, "true");
        chaserProps.put(ChaseCamera.PROP_TARGETOFFSET, new Vector3f(0f, ((BoundingBox) target.getWorldBound()).yExtent * 1.5f, 0f));
        //chaserProps.put(ChaseCamera.PROP_TARGETOFFSET, new Vector3f(0f, ((BoundingSphere) player.getWorldBound()).radius * 1.5f, 0f));
        chaserProps.put(ThirdPersonMouseLook.PROP_ENABLED, "true");
        chaserProps.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + FastMath.DEG_TO_RAD * 80);
        chaserProps.put(ThirdPersonMouseLook.PROP_MINASCENT, "" + FastMath.DEG_TO_RAD*0);
        chaserProps.put(ThirdPersonMouseLook.PROP_INVERTEDY, "false");
        chaserProps.put(ThirdPersonMouseLook.PROP_ROTATETARGET, "false");
        chaserProps.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "0");
        chaserProps.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "200");
        chaserProps.put(ThirdPersonMouseLook.PROP_MOUSEXMULT, "2.0");
        chaserProps.put(ThirdPersonMouseLook.PROP_MOUSEYMULT, "10.0");
        chaserProps.put(ThirdPersonMouseLook.PROP_MOUSEROLLMULT, "0.25");
        chaserProps.put(ThirdPersonMouseLook.PROP_LOCKASCENT, "false");
        
        //apply the properties
        updateProperties(chaserProps);

        setActionSpeed(1.0f);
	}
	
	public void update(float time) {
		super.update(time);
		
		//keep camera from going under the warehouse
		if(cam.getLocation().y < 0.5)
		{
			cam.getLocation().y = 0.5f;
			cam.update();
        }
	}

}
