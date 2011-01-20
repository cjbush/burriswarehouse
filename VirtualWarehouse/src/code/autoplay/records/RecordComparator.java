package code.autoplay.records;

import java.util.Comparator;

import code.autoplay.records.base.RecordEntry;

/**
 * This comparator is used by the PriorityQueue in <code>WarehouseWriter</code> to enforce a
 * chronological ordering of both synchronous and asynchronous record entries. 
 * 
 * @author Jordan Hinshaw
 *
 */
public class RecordComparator implements Comparator<RecordEntry>
{

	@Override
	public int compare(RecordEntry o1, RecordEntry o2) 
	{
		if(o1.getTime() == o2.getTime())
		{
			return 0;
		}
		else if(o1.getTime() < o2.getTime())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
