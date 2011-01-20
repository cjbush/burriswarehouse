package code.world;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;

/**
 * An area for the player to take the completed pick job to to complete the game.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class DeliveryArea extends Node {
	
	private static final long serialVersionUID = -158082304190640535L;
	
	public static float DISTANCE_ALLOWED = 1;
	
	private VirtualWarehouse vw;
	private Spatial objectToWatch;
	
	public DeliveryArea(float x, float z, VirtualWarehouse vw, Spatial objectToWatch) {
		this.vw = vw;
		this.objectToWatch = objectToWatch;
		
		Box object = new Box("deliveryArea", new Vector3f(0, 0, 0), .5f, .05f, .5f);
		object.clearRenderState(StateType.Texture);
		object.setDefaultColor(new ColorRGBA(ColorRGBA.darkGray));
		
		this.setModelBound(new BoundingBox());
		this.updateModelBound();
		this.attachChild(object);
		this.setLocalTranslation(x, .05f, z);
		addRotationController();
		
		vw.getRootNode().attachChild(this);
		
		this.updateGeometricState(0, true);
		this.updateRenderState();
	}
	
	/**
	 * Checks if the player is within the allowed distance to this object. The player
	 * wins when the pick jobs are complete and the pallet jack is taken to this area.
	 */
	public void checkForPlayer() {
		Vector3f objToWatchPosition = objectToWatch.getWorldTranslation();
		if (this.getWorldTranslation().distance(objToWatchPosition) < DISTANCE_ALLOWED)
		{
			//TODO: more checks such as making sure the player is on the pallet jack and that it is the
			//one with the boxes on it? 

			//player wins
			vw.showFinalScoreDisplay();
		}
	}
	
	private void addRotationController() {
		/*Make the icon rotate for visual effect*/
		
		//create a controller
		SpatialTransformer st = new SpatialTransformer(1);
		st.setObject(this, 0, -1);
		
		//Assign a rotation
		Quaternion x360 = new Quaternion();
		x360.fromAngleAxis(FastMath.DEG_TO_RAD * 360, new Vector3f(0, 1, 0));
		Quaternion x180 = new Quaternion();
		x180.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 1, 0));
		Quaternion x0 = new Quaternion();
		x0.fromAngleAxis(0, new Vector3f(0, 1, 0));

		st.setRotation(0, 0, x0);
		st.setRotation(0, 2.5f, x180);
		st.setRotation(0, 5, x360);
		st.setRepeatType(Controller.RT_WRAP);
		
		st.interpolateMissing();
		this.addController(st);
	}

}
