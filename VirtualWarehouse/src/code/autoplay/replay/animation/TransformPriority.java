package code.autoplay.replay.animation;


/**
 * <b>File:</b> TransformPriority.java<br/>
 * <b>Date:</b> April, 2010<br/><br/>
 * Wrapper class designed to hold a priority number and <code>TRANSFORM_TYPE</code>.<br/><br/>
 * 
 * The idea behind this whole structure, which is used in the <code>AnimatedModelTransformer</code>, 
 * is that situation could arise during the replay a recorded game in which multiple movements
 * could occur in the same time. For example, a rotation and a translation could occur at close to
 * same time. Since the direction in which something moves depends on directions it is facing (ie. 
 * its rotation) the intent of this structure was to de-conflict the these multiple coincident 
 * changes by allowing the user to define a priority order between different movement types.
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent
 *
 */
public class TransformPriority 
{
	private TransformType type;
	private int priority;
	
	/**
	 * Contructor
	 * 
	 * @param type
	 * @param priority
	 */
	public TransformPriority(TransformType type, int priority)
	{
		this.type = type;
		this.priority = priority;
	}

	/**
	 * @return the type
	 */
	public TransformType getType() {
		return type;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	
	
}
