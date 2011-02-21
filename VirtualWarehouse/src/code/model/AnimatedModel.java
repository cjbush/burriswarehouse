package code.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import code.model.player.Player;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import code.MD5.MD5Animation;
import code.MD5.MD5Node;
import code.MD5.controller.MD5Controller;
import code.MD5.importer.MD5Importer;
import code.MD5.interfaces.IMD5Controller;

import code.model.player.RandomPerson;


/**
 * <b>File:</b> AnimatedModel.java<br/>
 * <b>Date:</b> April, 2010<br/><br/>
 * This class is designed to be a base class for animated models in the VirtualWarehouse game. It provides helper
 * functions for adding and activating animations to a model. <b>The class and all its subclasses will only work
 * correctly with md5 models. The use of any other model type will probably generate exceptions.</b> 
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent
 */
public abstract class AnimatedModel extends Node
{
	public static final Vector3f DEFAULT_UP = Vector3f.UNIT_Y;
	
	private static final long serialVersionUID = 3093771701396781177L;
	private static final String MODEL_TYPE = "md5";
	private static final float FADE_TIME = 1f;
	
	protected MD5Node modelNode = null;
	
	private Hashtable<String, String> animations;
	private IMD5Controller animController = null;
	private MD5Importer importer = null;
	private String activeAnimation = null;
	
	private Vector3f forwardVector;
	private Vector3f upVector;
	private Vector3f rightVector;
	
	private RandomPerson myPerson;
	
	/**
	 * Contructor. Generates a new animated model with the specified <b>md5</b> model and default animation.
	 * 
	 * @param filePath - path of the <b>md5</b> model to be loaded 
	 * @param folderPath - path of the folder in which the model is located
	 * @param defaultAnimName - a name for the default animation
	 * @param animFilePath - the file path for the default animation
	 * @param repeatType - as specified by the Controller static constants
	 */
	public AnimatedModel(String filePath, String modelName, String defaultAnimName, String animFilePath, 
			int repeatType, Vector3f forwardVector, Vector3f rightVector, Vector3f upVector,
			RandomPerson randomPerson)
	{
		myPerson = randomPerson;
		
		animations = new Hashtable<String, String>();
		importer = MD5Importer.getInstance();
		
		/**
		 * IMPORTANT: This box is added as the FIRST child of the player node to as a work-around
		 * for an md5 model importation problem. When the model was loaded the camera would not
		 * function properly. We solve this problem by adding an ordinary box as the first child of
		 * the node. Since the box is a child of the player node, the box move around in side the 
		 * animated md5 node.
		 */
		Box b = new Box("playerBox", new Vector3f(0f, .3f, 0f), .005f, .005f, .005f);
		b.setModelBound(new BoundingBox());
		attachChild(b);
		
		//loads md5 model
		modelNode = loadMd5Model(filePath, modelName, animFilePath, defaultAnimName, repeatType, myPerson);
		this.attachChild(modelNode);
		
		this.forwardVector = forwardVector;
		this.upVector = upVector;
		this.rightVector = rightVector;
		
		animController = getConroller();
		animations.put(defaultAnimName, animFilePath);
	}
	
	
	/**
	 * Constructor. Uses DEFAULT_UP for the up vector.
	 */
	public AnimatedModel(String filePath, String folderPath, String defaultAnimName, String animFilePath, 
			int repeatType, Vector3f forwardVector, Vector3f rightVector)
	{
		this(filePath, folderPath, defaultAnimName, animFilePath, repeatType, forwardVector, rightVector, DEFAULT_UP, null);
	}
	
	
	
	/**
	 * Adds an animation to this model. An animation is only needs to be loaded once. If the provided animation
	 * has already been loaded this method does nothing and returns true. If the animation provided is the first
	 * animation that is loaded it will automatically be set as the active animation.
	 * 
	 * @param animName
	 * @param animFilePath
	 * @return true if successful, false otherwise
	 */
	public boolean addAnimation(String animName, String animFilePath)
	{
		try 
		{
			if(!animations.containsKey(animName))
			{
				importer.loadAnim(AnimatedModel.class.getClassLoader().getResource(animFilePath), animName);
				MD5Animation anim = (MD5Animation) importer.getAnimation();
				animController.addAnimation(anim);
				
				//clears all information from the importer
				importer.cleanup();
				
				//adds animation to the table of animations for this model
				animations.put(animName, animName);
			}
			
			return true;
		} 
		catch (IOException e) 
		{
			return false;
		}
		
	}
	
	
	/**
	 * Activates the provided animation, if it exists
	 * 
	 * @param animName - name of the animation
	 * @param speed - the speed of the animation where 1f is the baseline speed
	 * @param repeatType - <code>Controller</code> repeat types; specifies how the animation is repeated or if it is
	 * repeated at all.
	 * @return true if successful, false otherwise
	 */
	public boolean setActiveAnimation(String animName, float speed, int repeatType, float fade)
	{
		if(!animations.containsKey(animName))
		{
			return false;
		}
		else if(!animName.equals(activeAnimation))
		{
			animController.fadeTo(animName, fade, true);
			animController.setRepeatType(repeatType);
			animController.setSpeed(speed);
			activeAnimation = animName;
		}
		
		return true;
	}
	
	public boolean setActiveAnimation(String animName, float speed, int repeatType)
	{
		return setActiveAnimation(animName, 1f, repeatType, FADE_TIME);
	}
	
	/**
	 * See <code>setActiveAnimation(String animName, float speed, int repeatType)</code>. Sets animations with the
	 * default speed.
	 * 
	 * @param animName - name of the animation
	 * @param repeatType - <code>Controller</code> repeat types; specifies how the animation is repeated or if it is
	 * repeated at all.
	 * @return true if successful, false otherwise
	 */
	public boolean setActiveAnimation(String animName, int repeatType)
	{
		return setActiveAnimation(animName, 1f, repeatType, FADE_TIME);
	}
	
	public boolean setActiveAnimation(String animName, int repeatType, float fade)
	{
		return setActiveAnimation(animName, 1f, repeatType, fade);
	}
	
	/**
	 * Creates an IMD5Controller for this model <i>and attaches it to the modelNode</i>
	 * 
	 * @return the new IMD5Controller
	 */
	private IMD5Controller getConroller()
	{
		IMD5Controller md5Cntrl = null;
		boolean hasMD5Control = false;
		
		//When model is loaded, a default animation is also loaded, so the model should already have at least
		//one IMD5Controller attached to it. If this is the case, the controller is found and will be returned.
		if(modelNode.getControllerCount() >= 1)
		{
			ArrayList<Controller> contLs = modelNode.getControllers();
			
			for(Controller cont : contLs)
			{
				if(cont instanceof IMD5Controller)
				{
					md5Cntrl = (IMD5Controller) cont;
					hasMD5Control = true;
					break;
				}
			}
			
		}
		
		//if there is no IMD5Controller attached to this model, create a new one and return it.
		if(!hasMD5Control)
		{
			md5Cntrl = new MD5Controller(modelNode);
			modelNode.addController(md5Cntrl);
		}
		
		return md5Cntrl;
	}

	/**
	 * Loads an md5 model.
	 * 
	 * @param meshPath - path of the .md5mesh file
	 * @param meshName - name of the model
	 * @param animPath - path of the .md5anim file
	 * @param animName - name of the animation
	 * @param repeatType - the Controller repeat type of the animation
	 * @return the MD5Node created by the loading process
	 */
	public MD5Node loadMd5Model(String meshPath, String meshName, String animPath, String animName, 
			int repeatType, RandomPerson randomPerson)
	{
		int indx = animPath.lastIndexOf("/");
		String folderPath = animPath.substring(0, indx);
		
		//override the texture key
		try 
		{
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE,
					new SimpleResourceLocator(Player.class.getClassLoader().getResource(folderPath)));
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		

		URL modelMesh = ModelLoader.class.getClassLoader().getResource(meshPath);
		URL modelAnim = ModelLoader.class.getClassLoader().getResource(animPath);

		MD5Node meshNode = null;
		
		try 
		{
			MD5Importer importer = MD5Importer.getInstance();
			
			if (randomPerson == null)
			{
				importer.load(modelMesh, meshName, modelAnim, animName, repeatType);
			}
			else
			{
				importer.load(modelMesh, meshName, modelAnim, animName, repeatType, randomPerson);
			}

			float scaleFloat = .17f;
			meshNode = (MD5Node) importer.getMD5Node();
			meshNode.rotateUpTo(new Vector3f(1f,0f,0f));
			meshNode.setLocalScale(new Vector3f(scaleFloat, scaleFloat, scaleFloat));
			meshNode.setLocalTranslation(new Vector3f(0f, .3f, 0f));

			importer.cleanup();
			
			//adds animation to the table of animations for this model
			animations.put(animName, animName);
			activeAnimation = animName;
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
			return null;
		}

		return meshNode;
	}	
	
	/**
	 * @return the initial direction that is "forward" for the model.
	 */
	public Vector3f getForwardVector() {
		return forwardVector;
	}

	/**
	 * @return the initial up vector for the model.
	 */
	public Vector3f getUpVector() {
		return upVector;
	}
	
	/**
	 * @return the vector pointing to the right of model.
	 */
	public Vector3f getRightVector() {
		return rightVector;
	}

	/**
	 * Activates the animation that should occur when the model moves forward
	 */
	public abstract void translateForwardAnim();
	
	/**
	 * Activates the animation that should occur when the model moves left
	 */
	public abstract void translateLeftAnim();
	
	/**
	 * Activates the animation that should occur when the model moves right
	 */
	public abstract void translateRightAnim();
	
	/**
	 * Activates the animation that should occur when the model moves backwards
	 */
	public abstract void translateBackwardAnim();
	
	/**
	 * Activates the animation that should occur when the model moves upward
	 */
	public abstract void translateUpAnim();
	
	/**
	 * Activates the animation that should occur when the model moves downward
	 */
	public abstract void translateDownAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated in a negative direction about the X-axis
	 */
	public abstract void rotateXNegAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated counter clockwise about the X-axis
	 */
	public abstract void rotateXPosAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated clockwise about the Y-axis
	 */
	public abstract void rotateYNegAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated counter clockwise about the Y-axis
	 */
	public abstract void rotateYPosAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated clockwise about the Z-axis
	 */
	public abstract void rotateZNegAnim();
	
	/**
	 * Activates the animation that should occur when the model is rotated counter clockwise about the Z-axis
	 */
	public abstract void rotateZPosAnim();
	
	/**
	 * Activates the animation that should occur when the model is not being affected by a transformation
	 */
	public abstract void stationaryAnim();
}
