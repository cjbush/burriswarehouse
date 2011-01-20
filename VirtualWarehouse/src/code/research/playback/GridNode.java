package code.research.playback;

import com.jme.bounding.BoundingVolume;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

@SuppressWarnings("serial")
public class GridNode extends Node{
	private boolean usable;
	private int Xg;
	private int Yg;
	private double Xr;
	private double Yr;
	private int wLength = 48;
	private int wHeight = 48;
	private boolean debug;
	//private static final Image image = new Image("");
	private Node object;
	
	public GridNode(boolean usable, int Xg, int Yg, float Xr, float Yr, boolean debug, Node root, Node model){
		this.usable = usable;
		this.Xg = Xg;
		this.Yg = Yg;
		this.Xr = Xr;
		this.Yr = Yr;
		this.debug = debug;
		if(debug && usable){
			object = model;
			//object = ModelLoader.loadModel("obj","data/models/miscObj/gridnode/gridnode.obj", "data/models/miscObj/gridnode/", null, true);
			object.setName("derp");
			object.setLocalScale(0.1f);
			object.setLocalTranslation(new Vector3f((float)Xr,.05f,(float)Yr));	
			object.setLocalRotation(new Quaternion(180,0,0,0));
			root.attachChild(object);
		}
	}
	
	public void setDebug(boolean debug, Node root){
		this.debug = debug;
		if(debug){
			root.attachChild(object);
		}
		else{
			root.detachChild(object);
		}
	}
	
	public void setUsable(boolean usable, Node root, Node model){
		this.usable = usable;
		if(usable && debug){
			object = model;
			object.setName("derp");
			object.setLocalScale(0.1f);
			object.setLocalTranslation(new Vector3f((float)Xr,.05f,(float)Yr));	
			object.setLocalRotation(new Quaternion(180,0,0,0));
			root.attachChild(object);
		}
		else{
			root.detachChild(object);
		}
	}

	@Override
	public void draw(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findCollisions(Spatial scene, CollisionResults results,
			int requiredOnBits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findPick(Ray toTest, PickResults results, int requiredOnBits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTriangleCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVertexCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasCollision(Spatial scene, boolean checkTriangles,
			int requiredOnBits) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setModelBound(BoundingVolume modelBound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateModelBound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateWorldBound() {
		// TODO Auto-generated method stub
		
	}
}
