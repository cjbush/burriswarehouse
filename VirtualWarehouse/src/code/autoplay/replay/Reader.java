package code.autoplay.replay;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import code.autoplay.records.EndReplayEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;

/**
 * <b>File:</b> Reader.java<br/>
 * <b>Date:</b> May, 2010<br/><br/>
 * <b>Description:</b> Abstract class that provides the basics for a file reader that can be used
 * to gather and organize recorded information for a WarehouseTrainer autoplay. 
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent<br/><br/>
 * 
 * <b>Edited by:</b> Jordan Hinshaw. November 2010.
 */
public abstract class Reader extends Thread
{
	//Constants
	protected static final int DFLT_BUFFER_SIZE = 100;
	
	//Variables
	protected BlockingQueue<RecordEntry> buffer;
	protected boolean isEOF = false;

	/**
	 * Constructor.
	 * 
	 * @param filename
	 * @param bufferSize
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Reader(int bufferSize) throws IOException, InterruptedException
	{
		buffer = new ArrayBlockingQueue<RecordEntry>(bufferSize);
	}
	
	
	/**
	 * Simply calls the <code>BlockingQueue</code> method <code>take()</code>. This method,therefore,
	 * blocks when the queue is empty. 
	 * 
	 * @return the next RecordEntry. The last RecordEntry should be an EndReplayEntry.
	 * @throws InterruptedException
	 */
	public RecordEntry nextRecord() throws InterruptedException {
		return buffer.take();
	}
	
	
	/**
	 * Indicates whether the reader has reached the end of the file. 
	 * 
	 * @return true if the end of the file has been reached
	 */
	public boolean hasEnded() {
		return isEOF;
	}
	
	/**
	 * Reads the file and returns the next RecordEntry. Normally the last RecordEntry will be an EndReplayEntry with
	 * the time at which the game was ended. However, if the file is empty or the user tries to read beyond the end
	 * of the file, this method will return an EndReplayEntry with time = -1. <b>It is always safe to stop reading
	 * when <code>[returned RecordEntry].getType() == RecordType.END_RECORD</code></b>. Subclasses are responsible
	 * for ensuring this method behaves prescribed. 
	 * 
	 * @return the next RecordEntry
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	protected abstract RecordEntry readNext();
	
	protected void put(RecordEntry rec) {
		try {
			buffer.put(rec);
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public abstract void run();
	
}
