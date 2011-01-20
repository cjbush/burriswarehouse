package code.autoplay.replay.animation;

/**
 * Enumerates a series of movement types. (See <code>code.autoplay.animation.TranformPriority</code> 
 * for more details.)
 * 
 * @author Jordan Hinshaw
 *
 */
public enum TransformType 
{
		/**
		 * Foward movement
		 */
		FORWARD,
		
		/**
		 * Backward movement 
		 */
		BACKWARD, 
		
		/**
		 * Movement left
		 */
		LEFT, 
		
		/**
		 * Movement right
		 */
		RIGHT, 
		
		/**
		 * Upward movement
		 */
		UP, 
		
		/**
		 * Downward movement
		 */
		DOWN, 
		
		/**
		 * Positive rotation around the X-axis
		 */
		ROTX_POS, 
		
		/**
		 * Positive rotation around the Y-axis
		 */
		ROTY_POS, 
		
		/**
		 * Positive rotation around the Z-axis
		 */
		ROTZ_POS,
		
		/**
		 * Negative rotation around the X-axis
		 */
		ROTX_NEG, 
		
		/**
		 * Negative rotation around the Y-axis
		 */
		ROTY_NEG, 
		
		/**
		 * Negative rotation around the Z-axis
		 */
		ROTZ_NEG;
}
