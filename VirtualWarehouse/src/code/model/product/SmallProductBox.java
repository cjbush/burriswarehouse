package code.model.product;

import code.model.ModelLoader;
import code.model.SharedMeshManager;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The smaller, individual boxes that can be taken from the large boxes.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class SmallProductBox extends Product {

	private static final String PROD_LOC = "data/models/boxes/small_generic/";
	private static final String PROD_FILE = "smallbox.obj";
	
	private static final Vector3f INITIAL_LOC = new Vector3f(0f,.07f,0f);
	
	/**
	 * This ensures that all small product boxes are given a unique name
	 */
	private static int count = 1;
	
	public SmallProductBox(SharedMeshManager smm, String name) {
		this.setName(name + count);
		loadModel(smm);
		count++;
	}
	
	private void loadModel(SharedMeshManager smm) {
		Spatial productModel = null;
		productModel = ModelLoader.loadModel("obj", PROD_LOC + PROD_FILE,
				PROD_LOC, smm, true);
		this.setLocalTranslation(INITIAL_LOC);
		attachChild(productModel);
	}
	
}
