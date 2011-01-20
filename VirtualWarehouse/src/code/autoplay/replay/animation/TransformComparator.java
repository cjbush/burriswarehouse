package code.autoplay.replay.animation;

import java.util.Comparator;


/**
 * <b>File:</b> TransformComparator.java<br/>
 * <b>Date:</b> April, 2010<br/><br/>
 * Comparator for <code>TranformPriority</code> objects. Used in a PriorityQueue to enforce the
 * ordering of animation actions. (See <code>code.autoplay.animation.TranformPriority</code> for more
 * details.)
 * 
 * @author Jordan Hinshaw
 */
public class TransformComparator implements Comparator<TransformPriority> 
{
	public TransformComparator() {
		
	}

	@Override
	public int compare(TransformPriority o1, TransformPriority o2) 
	{
		int pr1 = o1.getPriority();
		int pr2 = o2.getPriority();
		
		if(pr1 < pr2)
		{
			return 1;
		}
		else if(pr1 > pr2)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}

}
