package code.model;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * No longer using this.  Still here just in case
 *
 */
public class TarpWall extends Node
{
	private static final long serialVersionUID = -9016674998705925387L;

	public TarpWall(String name, Vector3f corner1, Vector3f corner2)
	{
		Box wall = new Box(name, corner1, corner2);
		wall.setDefaultColor(new ColorRGBA(0f, .3f, .5f, 1f));
		
		wall.setModelBound(new BoundingBox());
        wall.updateModelBound();
		
		this.setName(name);
		this.attachChild(wall);
		
		this.setLightCombineMode(Spatial.LightCombineMode.Off);
		
		this.updateGeometricState(0, true);
		this.updateRenderState();
	}
	
	/*public TarpWall(String name)
	{
		loadModel();
		this.setName(name);
	}*/

	/*private void loadModel() 
	{
		Spatial wall1 = ModelLoader.loadOgreXMLModel(path)
	}*/
	
}
