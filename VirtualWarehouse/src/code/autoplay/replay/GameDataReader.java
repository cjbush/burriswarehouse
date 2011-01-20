package code.autoplay.replay;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import code.autoplay.records.EndReplayEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;

/**
 * <b>File:</b> Reader.java<br/>
 * <b>Date:</b> May, 2010<br/><br/>
 * <b>Description:</b> Reads the given file of recorded data for the Warehouse Trainer game.
 * This class is used specifically to read information from a recorded game. 
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent<br/><br/>
 * 
 * <b>Edited by:</b> Jordan Hinshaw. November 2010.
 */
public class GameDataReader extends Reader
{
	//Variables
	private ObjectInputStream in;
	
	/**
	 * Constructor.
	 * 
	 * @param filename
	 * @param bufferSize
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public GameDataReader(String filename, int bufferSize) throws IOException, InterruptedException
	{
		super(bufferSize);
		
		try 
		{
			in = new ObjectInputStream(new FileInputStream(filename));
		}
		catch (EOFException e) 
		{
			isEOF = true;
		}

	}
	
	
	/**
	 * Constructor
	 * 
	 * @param filename
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public GameDataReader(String filename) throws IOException, InterruptedException {
		this(filename, DFLT_BUFFER_SIZE);
	}
	
	
	@Override
	protected RecordEntry readNext()
	{
		try
		{
			if(isEOF)
			{
				return generateEnd(); 
			}
			else
			{
				return (RecordEntry) in.readObject();
			}
		}
		catch(EOFException e) {
			return generateEnd();
		}
		catch(IOException e) {
			return generateEnd();
		}
		catch(ClassNotFoundException e) {
			return generateEnd();
		}
	}
	
	
	private EndReplayEntry generateEnd() {
		isEOF = true;
		return new EndReplayEntry(-1);
	}
	
	
	@Override
	public void run()
	{	
		RecordEntry rec; 

		while(!isEOF) {
			rec = readNext();
			this.put(rec);
		}

	}
	
	public static void main(String[] args)
	{
		try {
			//BlockingQueue<RecordEntry> q = new ArrayBlockingQueue<RecordEntry>(10);
			Reader reader = new GameDataReader("src/data/placement/test.txt");
			reader.start();
			
			RecordEntry temp = reader.nextRecord();
			while(temp != null && temp.getType() != RecordType.END_RECORD)
			{
				System.out.println(temp.getTime() + "  " + temp.getType().name());
				temp = reader.nextRecord();
			}
			
			System.out.println(temp.getTime() + "  " + temp.getType().name());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
