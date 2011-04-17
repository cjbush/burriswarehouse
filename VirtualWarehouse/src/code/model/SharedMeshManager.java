package code.model;

import java.util.HashMap;
import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.RenderState;

/**
 * Stores nodes resulting from loading model data and their corresponding filenames.
 * Allows new nodes to be created with SharedMeshes, so that new models do not have to be
 * reloaded from the file for every instance of the model in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * More Optimization Fixes
 */
public class SharedMeshManager {

	 //store filenames and the corresponding model node 
	static HashMap<String, Node> cachedNodes;
	static HashMap<String, Quad> cachedQuads;
	static HashMap<String, TriMesh> cachedTriMeshes;
	
	public SharedMeshManager() {
		cachedNodes = new HashMap<String, Node>();
		cachedQuads = new HashMap<String, Quad>();
		cachedTriMeshes = new HashMap<String, TriMesh>();
	}
	
	/**
	 * Returns a SharedNode corresponding to the model with the given filename.
	 * Will return null if the model is not stored.
	 * @param model
	 * @return
	 */
	public static Node getNode(String model) {
		Node n = null;
		
		Node cached = cachedNodes.get(model);
		
		if (cached != null)
		{
			n = new Node();
			
			//rebuild the node with SharedMeshes
			for (int i=0; i<cached.getChildren().size(); i++)
			{
				n.attachChild( new SharedMesh("SharedMesh"+cached.getChild(i).getName(), (TriMesh) cached.getChild(i) ));
			}
			
			//set the same properties on the new node as the cached copy
			n.setName(cached.getName());
	        n.setCullHint(cached.getLocalCullHint());
	        n.setLightCombineMode(cached.getLocalLightCombineMode());
	        n.getLocalRotation().set(cached.getLocalRotation());
	        n.getLocalScale().set(cached.getLocalScale());
	        n.getLocalTranslation().set(cached.getLocalTranslation());
	        n.setRenderQueueMode(cached.getLocalRenderQueueMode());
	        n.setTextureCombineMode(cached.getLocalTextureCombineMode());
	        n.setZOrder(cached.getZOrder());
	        for (RenderState.StateType type : RenderState.StateType.values()) {
	            RenderState state = cached.getRenderState( type );
	            if (state != null) {
	                n.setRenderState(state );
	            }
	        }
		}
		
		return n;
	}
	
	/**
	 * Attempts to cache the model so that it can be created with SharedMeshes.
	 * The model will not be cached if it is not made of TriMeshes, as SharedMeshes
	 * only support TriMeshes (not QuadMeshes).
	 * @param model
	 * @param n
	 */
	public static void cacheNode(String model, Node n) {
		
		boolean canCache = true;
		
		//check if any of the children are not TriMeshes
		//and thus not supported by SharedMeshes
		List<Spatial> children = n.getChildren();
		for (int i=0; i<children.size(); i++)
		{
			if ( !(children.get(i) instanceof TriMesh) )
			{
				canCache = false;
			}
		}
		
		if (canCache)
		{
			cachedNodes.put(model, n);
		}
	}
	
	public static void cacheTriMesh(String model, TriMesh m){
		cachedTriMeshes.put(model, m);
	}
	
	public static TriMesh getTriMesh(String model){
		return cachedTriMeshes.get(model);
	}
	
	/**
	 * Stores a Quad in the cache.
	 * @param model
	 * @param q
	 */
	public static void cacheQuad(String model, Quad q) {
		cachedQuads.put(model, q);
	}
	
	/**
	 * Returns a SharedMesh version of a Quad from the cache if one is in the cache, 
	 * otherwise null is returned.
	 * @param model
	 * @return
	 */
	public static SharedMesh getQuad(String model) {
		Quad cached = cachedQuads.get(model);
		SharedMesh quad = null;
		if (cached != null)
		{
			quad = new SharedMesh(cached.getName(), cached);
			
			//set the same properties on the new quad as the cached copy
			quad.setName(cached.getName());
			quad.setCullHint(cached.getLocalCullHint());
			quad.setLightCombineMode(cached.getLocalLightCombineMode());
			quad.getLocalRotation().set(cached.getLocalRotation());
			quad.getLocalScale().set(cached.getLocalScale());
			quad.getLocalTranslation().set(cached.getLocalTranslation());
			quad.setRenderQueueMode(cached.getLocalRenderQueueMode());
			quad.setTextureCombineMode(cached.getLocalTextureCombineMode());
			quad.setZOrder(cached.getZOrder());
	        for (RenderState.StateType type : RenderState.StateType.values()) {
	            RenderState state = cached.getRenderState( type );
	            if (state != null) {
	            	quad.setRenderState(state );
	            }
	        }
		}
		
		return quad;
	}
	
}
