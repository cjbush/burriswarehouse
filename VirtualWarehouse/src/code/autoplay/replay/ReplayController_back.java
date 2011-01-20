package code.autoplay.replay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import code.app.VirtualWarehouse;
import code.autoplay.ObjectNameNotUniqueException;
import code.autoplay.records.*;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;
import code.autoplay.records.base.SynchronousRecordEntry;
import code.autoplay.replay.animation.TransformType;
import code.model.AnimatedModel;

import com.jme.animation.SpatialTransformer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.Timer;

/**
 * <b>File:</b> ReplayController.java<br/>
 * <b>Date:</b> May, 2010<br/><br/>
 * <b>Description:</b> Controls and facilitates the replay of recorded data in the Warehouse Trainer game.    
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent<br/><br/>
 * 
 * <b>Edited by:</b> Jordan Hinshaw. November 2010.
 */
public class ReplayController_back extends Thread
{
	private static final long serialVersionUID = 7133350273292108521L;
	private static final int MAX_READ = 1000;
	private static final long TIME_BOUNDARY = 10;
	
	private VirtualWarehouse vw;
	private Reader read;
	private float prevTime = -1;
	private float currTime = -1;
	private Node root;

	private Hashtable<TransformType, Integer> priorityTable;

	public ReplayController_back(VirtualWarehouse warehouse, Reader reader, 
			Hashtable<TransformType, Integer> priorityTable) throws FileNotFoundException
	{
		vw = warehouse;	
		//this.priorityTable = priorityTable;
		
		//set the current Reader and starts the thread 
		read = reader;
		read.start();
		
		//generateTransformer();
	}
	
	private Controller generateTransformer()
	{
		return null;
	}

	
	private void endOfRecording()
	{
		//TODO:
		System.out.println("end");
		System.exit(0);
	}
	
	
	private void modifyParent(ParentChangeEntry rec)
	{
		//TODO
		System.out.println("parentChange: " + rec.getParentName() + "->" 
				+  rec.getObjName() );
	}
	
	@Override
	public void run() {
		
		try {
			/*
			 * This allows the game time to catch up to the time stamped data
			 * already processed by this method. This is intended to prevent
			 * the replay control from reading a whole file's worth of data in
			 * at once, which, if a file were large enough could use up too
			 * much memory. 
			 */
			if(currTime >= prevTime - TIME_BOUNDARY) {
				
				RecordEntry record = read.nextRecord();
				RecordType rType = record.getType();
				String name = record.getObjName();
				
				int cntr = 0;
				
				/*
				 * Stops reading records when an EndReplayRecord has been found
				 * or when the max number of consecutive reads has been performed.
				 * If the max number of reads has been read and no end replay
				 * signal has been sent this loop will activate again when the
				 * above if-statement conditions are reached.
				 */
				while(rType != RecordType.END_RECORD && cntr < MAX_READ) {
					
					/*
					 * This call will find all objects in the scene graph with the
					 * same name as the one provided in the record, if there are
					 * any. If there is more than one such object an
					 * ObjectNameNotUniqueException is thrown.
					 */
					List<Spatial> ls = root.descendantMatches(name);
					int size = ls.size();
					
					if( size > 1 || size < 1) {
						throw new ObjectNameNotUniqueException(name);
					}
					
					
					switch(rType) {
						case TRANSFORMATION:
							
							break;
						case IMU_TRANSFORM:
							break;
						case PARENT_CHANGE:
							break;
						case CREATE_PRODUCT:
							break;
					}
					
					//update record entry and type
					record = read.nextRecord();
					rType = record.getType();
					
					//update counter
					cntr++;
				}
			}
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * During a game replay, this method should be called during the update phase
	 * of every frame.
	 * 
	 * @param time - measured by the JME Timer class
	 * @throws ObjectNameNotUniqueException
	 */
	public void update(float time) throws ObjectNameNotUniqueException
	{
		if(currTime < time) {
			//vw.
		}
		else {
			currTime = time;
		}
			
		/*try 
		{
			if(	time >= prevTime - TIME_BOUNDARY)
			{
				SpatialTransformer tempTransformer = null;
				RecordEntry rec = read.nextRecord();
				RecordType tempType = rec.getType();
				boolean objectSet = false;
				
				SynchronousRecordEntry synch;
				
				//while(tempType == RecordType.TRANSFORMATION)
				while(rec instanceof SynchronousRecordEntry)
				{
					synch = (SynchronousRecordEntry) rec;
					prevTime = synch.getTime();
					
					//debug statement
					//System.out.println("TRANSFORMATION " + prevTime);
					switch(synch.) {
					
					}
					List<Spatial> ls = root.descendantMatches(synch.getObjName());
					
					//if there ls.size() > 1 then there is more than one object with the
					//same name; such an event is an error b/c all names should be unique.
					if( ls.size() > 1 )
					{
						throw new ObjectNameNotUniqueException(synch.getObjName());
					}
					else if(!objectSet)
					{
						//if the name was unique and the object to be transformed has not yet been set
						Spatial spat = ls.remove(0);
						
						//if the model is an animated model, use the AnimatedModelTransformer instead of the
						//regular SpatialTransformer
						if(spat instanceof AnimatedModel)
						{
							tempTransformer = new AnimatedModelTransformer((AnimatedModel) spat, priorityTable);
						}
						else
						{
							tempTransformer = new SpatialTransformer(0);
							tempTransformer.setObject(spat, 0, -1);
						}
						
						objectSet = true;
					}
					
					//set the locatoin,rotation, and scale of the object for this time
					tempTransformer.setPosition(0, time, synch.getTranslation());
					tempTransformer.setRotation(0, time, synch.getRotation());
					tempTransformer.setScale(0, time, ((TransformEntry) synch).getScale());
					
					
					rec = read.nextRecord();
					tempType = rec.getType();
				}
				
				
				switch(tempType)
				{
					case END_RECORD:
						endOfRecording();
						break;
						
					case PARENT_CHANGE:
						modifyParent( (ParentChangeEntry) rec);
						prevTime = rec.getTime();
						break;
				}
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}*/
		
	}
	
	
	/*public static void main(String[] args)
	{
		ArrayBlockingQueue<RecordEntry> q = new ArrayBlockingQueue<RecordEntry>(100);
		
		try 
		{
			Reader read 
				= new Reader("src/data/placement/test.txt");
			
			read.start();
			
			//ReplayController replay = new ReplayController(null, q);
			
			while(true)
			{
				//replay.update(Timer.getTimer().getTime());
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
