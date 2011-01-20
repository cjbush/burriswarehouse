package code.autoplay.replay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.Timer;

/**
 * <b>File:</b> ReplayController.java<br/>
 * <b>Date:</b> May, 2010<br/><br/>
 * <b>Description:</b> Controls and facilitates the replay of recorded data in the Warehouse Trainer game.
 * This class is responsible for ensuring that the necessary <code>Controllers</code> are added in the
 * appropriate places within the scene graph. This runs as a separate thread and ensures that the 
 * controller generation and information stays ahead of the actual game time.
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent<br/><br/>
 * 
 * <b>Edited by:</b> Jordan Hinshaw. November 2010.
 */
public class ReplayController extends Thread
{
	//Constants
	private static final long serialVersionUID = 7133350273292108521L;
	private static final double MAX_BOUNDARY = 25;
	private static final double NOTIFY_BOUNDARY = 15;
	private static final double MIN_BOUNDARY = 10;
	private static final double Q_BOUNDARY = 5;
	
	private Object lock = new Object();
	
	//Variables
	private VirtualWarehouse vw;
	private Reader read;
	private Node root;
	
	private double rTime = -1;	
	private double worldTime = 0;
	
	private boolean endRead = false;

	private Hashtable<TransformType, Integer> priorityTable;

	
	/**
	 * Constructor.
	 * 
	 * @param warehouse
	 * @param reader
	 * @param priorityTable
	 * @throws FileNotFoundException
	 */
	public ReplayController(VirtualWarehouse warehouse, Reader reader, 
			Hashtable<TransformType, Integer> priorityTable) throws FileNotFoundException
	{
		vw = warehouse;
		root = vw.getRootNode();
		this.priorityTable = priorityTable;
		
		//set the current Reader and starts the thread 
		read = reader;
		read.start();
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
				RecordEntry record = read.nextRecord();
				RecordType rType= record.getType();
				String name= record.getObjName();
				
				/*
				 * Set the first rTime. The offset is set to the value of
				 * the first record time so that no extra storage is needed to 
				 * store the initial start value of the record time.
				 */
				rTime = record.getTime();
				
				
				/*
				 * Used here instead of below as a temporary work-around.
				 */
				List<Spatial> ls = root.descendantMatches(name);
				int size = ls.size();

				if( size > 1 || size < 1) {
					throw new ObjectNameNotUniqueException(name + 
							". There are " + size + " objects with this name.");
				}
				
				Spatial spat = ls.get(0);
				UpdatableTransformer transform = new UpdatableTransformer(spat, (float) Q_BOUNDARY);
				spat.addController(transform);
				

				while(rType != RecordType.END_RECORD) {
					
					/*
					 * This call will find all objects in the scene graph with the
					 * same name as the one provided in the record, if there are
					 * any. If there is more than one such object an
					 * ObjectNameNotUniqueException is thrown.
					 */
/*					List<Spatial> ls = root.descendantMatches(name);
					int size = ls.size();

					if( size > 1 || size < 1) {
						throw new ObjectNameNotUniqueException(name + 
								". There are " + size + " objects with this name.");
					}*/

					//System.out.println("yyy");

					switch(rType) {
					case TRANSFORMATION:
						
						
						break;
					case IMU_TRANSFORM:
						Vector3f position = record.getTranslation();
						Quaternion rotation = record.getRotation();
						
						transform.addKeyframe( (float) rTime/*(rTime + offset)*/, position, rotation, 
								new Vector3f(1, 1, 1) );
						
						
						break;
						
					case PARENT_CHANGE:
						break;
					case CREATE_PRODUCT:
						break;
					case END_RECORD:
						endRead = true;
						transform.finishedUpdates();
						break;
					}
					
					
					/*
					 * This prevents the reading and processing of record data
					 * from getting too far behind or ahead of the game replay
					 */
					synchronized (lock) {
						double timeDiff = rTime - worldTime;
						
						System.out.println(timeDiff);
						
						if(timeDiff >= MAX_BOUNDARY) {
							System.out.println("wait");
							lock.notifyAll();
							lock.wait();
						}
						else if(timeDiff >= NOTIFY_BOUNDARY) {
							lock.notifyAll();
							System.out.println("notify");
						}
						
						//get next record entry and update
						record = read.nextRecord();
						rType = record.getType();
						rTime = record.getTime();
						name = record.getObjName();
					}
					
				} 
				
				endRead = true;
				//System.out.println("finished reading: " + rTime + "; at gTime: " + gTime);
				
			} 
			catch(Exception e) {
				/*
				 * The generic class Exception is caught because the main game thread depends
				 * on this one. If any exception is thrown and not caught, this thread would
				 * cease and the other game thread would freeze.  
				 */
				e.printStackTrace();
				endOfRecording();
			}

	}
	
	/**
	 * During a game replay, this method should be called during the update phase
	 * of every frame.
	 * 
	 * @param time - time elapsed since this method was last called.
	 */
	public void update(float time)
	{
		worldTime += time;
		
		try {
			
			synchronized (lock) {
				
				/*
				 * Calculates the difference between the record time and the 
				 * current game time. The larger the number, the farther 
				 * ahead the record processing is. 
				 * 
				 * NOTE: Except at the end of the replay, this number should
				 * never reach or drop below zero. The timeDiff would only
				 * reach or drop below zero if the game replay had caught up
				 * to or passed the process of the replay data. This, obviously,
				 * cannot occur.   
				 */
				double timeDiff = rTime - worldTime; 
				
				/*
				 * Ensures that record processing continues once the replay has
				 * caught up to the record processing. Under normal circumstances
				 * this should prevent the follow while loop from activating.
				 */
				if(timeDiff <= NOTIFY_BOUNDARY) {
					lock.notifyAll();
				}
				
				//System.out.println("rtime: " + rTime + "  worldTime: " + worldTime + "  timeDiff: " + timeDiff);
				
				/*
				 * If timeDiff gets too close to zero, the replay stops and waits
				 * until a notify is sent, indicating that the record processing has
				 * far enough exceeded the replay. The previous if-statement should,
				 * under normal circumstances prevent the loop conditions from being
				 * true, except perhaps at the very start of the game. However, since
				 * the replay CANNOT be allowed overtake the record processing, the
				 * possibility of this condition must be accounted for. 
				 */
				while(!endRead && timeDiff < MIN_BOUNDARY) {
					lock.notifyAll();
					lock.wait();
					
					//once the thread has been notified, timeDiff needs to
					//be updated to reflect the latest values.
					timeDiff = rTime - worldTime;
				}
				
				
				
				/*
				 * Once the end of the record data has been reached, the timeDiff
				 * will be allowed to approach zero. Once it has, the recording 
				 * will end.
				 */
				if(endRead && timeDiff <= 0) {
					endOfRecording();
				}
			}
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}
	
	
}
