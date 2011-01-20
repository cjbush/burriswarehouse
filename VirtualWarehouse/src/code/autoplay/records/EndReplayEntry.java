package code.autoplay.records;

import code.autoplay.records.base.AsynchronousRecordEntry;
import code.autoplay.records.base.RecordType;

/**
 * Defines a RecordEntry the signals the end of the game replay/recording.
 * 
 * @author Jordan Hinshaw
 *
 */
public class EndReplayEntry extends AsynchronousRecordEntry 
{
	private static final long serialVersionUID = -1567030387073786204L;

	public EndReplayEntry(float aTime) 
	{
		super(aTime, RecordType.END_RECORD, null, null, null);
	}

}
