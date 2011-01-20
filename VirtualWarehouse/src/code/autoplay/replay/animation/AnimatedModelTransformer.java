package code.autoplay.replay.animation;

import java.util.Hashtable;
import java.util.PriorityQueue;

import code.model.AnimatedModel;
import code.model.player.Player;

import com.jme.animation.SpatialTransformer;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * This class in incomplete. It is intended as a controller for models containing animations that must
 * be activated along with the movement of the model (see <code>code.model.AnimatedModel<code>). 
 * <i>Before continuing this class, check to make sure that no built-in JME controllers exist to
 * perform this functionality. The class</i> <code>ActionController</code> <i>might be a good place to
 * start.    
 * 
 * @author Jordan Hinshaw
 *
 */
public class AnimatedModelTransformer extends SpatialTransformer 
{
	private static final long serialVersionUID = -6412516114990791357L;	
	private static final int OBJ_INDX = 0;
	
	private boolean hasBeenUpdated = false;
	private Vector3f prevTrans = null;
	private Quaternion prevRot = null;
	
	private Vector3f forward = null;
	private Vector3f up = null;
	private Vector3f right = null;
	
	private Hashtable<TransformType, Integer> priorities;
	private PriorityQueue<TransformPriority> q;
	
	/**
	 * Constructor. <br/><br/>
	 * This constructor creates an <code>AnimatedModelTransformer</code> with the given model and the given priorities. 
	 *  
	 * @param model -- the <code>AnimatedModel</code> that should be controlled
	 * @param priorityTable -- a <code>Hashtable</code> that maps each <code>TransformType</code> to an integer
	 * value, indicating the priority of the action. Smaller numbers correspond to higher priorities.
	 */
	public AnimatedModelTransformer(AnimatedModel model, Hashtable<TransformType, Integer> priorityTable) 
	{
		super();
		//this.priorities = model.getPriorities();
		this.setObject(model, 0, -1);
		
		this.forward = model.getForwardVector();		
		this.up = model.getUpVector();
		this.right = model.getRightVector();
		
		this.q = new PriorityQueue<TransformPriority>(TransformType.values().length, new TransformComparator());
	}

	
	@Override
	public void update(float time) 
	{
		super.update(time);
		
		if(!hasBeenUpdated)
		{
			prevTrans = this.toChange[OBJ_INDX].getLocalTranslation();
			prevRot = this.toChange[OBJ_INDX].getLocalRotation();
			hasBeenUpdated = true;
		}
		else
		{
			//get cuurent tranlation and rotation
			Vector3f currTrans = this.toChange[OBJ_INDX].getLocalTranslation();
			Quaternion currRot = this.toChange[OBJ_INDX].getLocalRotation();
			
			//calculate the change in translation and rotation
			Vector3f movementDir = prevTrans.subtract(currTrans);
			Quaternion rotChange = prevRot.subtract(currRot);
			
			
			//determine if model was moving backwards or forwards
			float dirTest = movementDir.dot(forward);
			if(dirTest > 0)
			{
				addTranform(TransformType.FORWARD);
			}
			else if(dirTest < 0)
			{
				addTranform(TransformType.BACKWARD);
			}
			
			
			
			//determine if the model was moving left or right
			dirTest = movementDir.dot(right);
			if(dirTest > 0)
			{
				addTranform(TransformType.RIGHT);
			}
			else if(dirTest < 0)
			{
				addTranform(TransformType.LEFT);
			}
			
			
			//determine if the model was moving up or down
			dirTest = movementDir.dot(up);
			if(dirTest > 0)
			{
				addTranform(TransformType.UP);
			}
			else if(dirTest < 0)
			{
				addTranform(TransformType.DOWN);
			}
			
			
			//determine if rotated around X-axis
			if(rotChange.getX() < 0)
			{
				addTranform(TransformType.ROTX_NEG);
			}
			else if(rotChange.getX() > 0)
			{
				addTranform(TransformType.ROTX_POS);
			}
			
			//determine if rotated around Y-axis
			if(rotChange.getY() < 0)
			{
				addTranform(TransformType.ROTY_NEG);
			}
			else if(rotChange.getY() > 0)
			{
				addTranform(TransformType.ROTY_POS);
			}
			
			//determine if rotated around Z-axis
			if(rotChange.getZ() < 0)
			{
				addTranform(TransformType.ROTZ_NEG);
			}
			else if(rotChange.getZ() > 0)
			{
				addTranform(TransformType.ROTZ_POS);
			}
			
			
			
			//the highest transformation will be at the top of the queue. It it the animation for this 
			//transformation that will be activated.
			TransformPriority activeTransform = q.poll();
			
			if(activeTransform != null)
			{ 
				activateAnimation(activeTransform.getType());
			}
			
			
			//rotate axis according to rotation
			Quaternion yRot = new Quaternion();
			yRot.fromAngleAxis(rotChange.getY(), up);
			
			forward = yRot.mult(forward);
			right = yRot.mult(right);
			
			//clear queue
			q.clear();
		}
	}
	
	private void activateAnimation(TransformType type)
	{
		AnimatedModel model = (AnimatedModel) this.toChange[OBJ_INDX];
		switch(type)
		{
			case FORWARD:
				model.translateForwardAnim();
				break;
				
			case BACKWARD:
				model.translateBackwardAnim();
				break;
				
			case DOWN:
				model.translateDownAnim();
				break;
				
			case UP:
				model.translateUpAnim();
				break;
				
			case LEFT:
				model.translateLeftAnim();
				break;
				
			case RIGHT:
				model.translateRightAnim();
				break;
				
			case ROTX_NEG:
				model.rotateXNegAnim();
				break;
				
			case ROTX_POS:
				model.rotateXPosAnim();
				break;
				
			case ROTY_NEG:
				model.rotateYNegAnim();
				break;
				
			case ROTY_POS:
				model.rotateYPosAnim();
				break;
				
			case ROTZ_NEG:
				model.rotateZNegAnim();
				break;
				
			case ROTZ_POS:
				model.rotateZPosAnim();
				break;
				
			default:
				model.stationaryAnim();
				break;
		}
	}
	
	private void addTranform(TransformType type)
	{
		q.add( new TransformPriority(type, priorities.get(type)) );
	}
	
}
