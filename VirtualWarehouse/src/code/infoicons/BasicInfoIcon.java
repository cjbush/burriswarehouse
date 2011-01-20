package code.infoicons;

import java.util.logging.Logger;

import code.model.ModelLoader;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

/**
 * An icon that floats in space. Allows the user to go near it and hit a key to learn
 * more information about something.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class BasicInfoIcon extends Node {
	
	private static final Logger logger = Logger.getLogger(ModelLoader.class.getName());
	
	//hold the text information associated wtih the infoIcon
	private String text;
	
	private boolean active;
	
	private SpatialTransformer tempSt;
	
	public BasicInfoIcon(String s) {
		text = s;
	}
	
	public void makeActive() {
		logger.info(this.getName() + " active!");
		//pause the icon to show that it is active
		tempSt = (SpatialTransformer) this.getController(0);
		this.removeController(0);
	}
	
	public void makeUnActive() {
		logger.info(this.getName() + " unactive!");
		//make the icon move again to show that the player is
		//no longer close enough to activate it
		this.addController(tempSt);
		tempSt = null;
	}
	
	public String getText() {
		return text;
	}

}
