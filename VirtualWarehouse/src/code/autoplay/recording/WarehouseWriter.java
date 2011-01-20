package code.autoplay.recording;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import code.app.VirtualWarehouse;
import code.autoplay.ObjectNameNotUniqueException;
import code.autoplay.records.CreateProductEntry;
import code.autoplay.records.EndReplayEntry;
import code.autoplay.records.ParentChangeEntry;
import code.autoplay.records.RecordComparator;
import code.autoplay.records.TransformEntry;
import code.autoplay.records.base.AsynchronousRecordEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.SynchronousRecordEntry;
import code.model.player.Player;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.Timer;
import com.jme.util.stat.graph.GraphFactory;

/**
 * <b>File:</b> WarehouseWriter.java<br/>
 * <b>Date:</b> March, 2010<br/><br/>
 * <b>Description:</b> Defines an object that monitors and receives messages about the movements and
 * actions of the player in the game. These actions are then written to file as <code>RecordEntry</code>s
 * using the <code>Serializable</code> interface.     
 * 
 * @author Jordan Hinshaw, Aaron Ramsey, Matt Kent
 *
 */
public class WarehouseWriter 
{		
	//private BigInteger frame = BigInteger.ZERO;
	private Node movingNode;
	private Player player;
	private Node root;
		
	private float interval;
	private float prevTime = 0;
	private int buffSize;
	private boolean hasSynched = false;
	
	private ObjectOutputStream out;
	private PriorityQueue<RecordEntry> q;
	private Map<Class<?>, SynchronousRecordEntry> map;
	

	/**
	 * Constructor
	 * 
	 * @param warehouse
	 * @param timerResolution
	 * @param recordingResolution - in seconds
	 * @param buffSize
	 * @param filename
	 * @throws IOException
	 */
	public WarehouseWriter(VirtualWarehouse warehouse, float recordingResolution, int buffSize, 
			String filename) throws IOException
	{
		//vw = warehouse;
		
		root = warehouse.getRootNode();
		player = (Player) warehouse.getPlayerNode();
		movingNode = player;
		
		this.buffSize = buffSize;
		interval = recordingResolution;
		out = new ObjectOutputStream(new FileOutputStream(filename));
		q = new PriorityQueue<RecordEntry>(buffSize, new RecordComparator());
		map = new Hashtable<Class<?>, SynchronousRecordEntry>(5);
	}
	
	
	/**
	 * The method should be called every frame during a recording. It generates a TransformEntry every frame.
	 * 
	 * @param time -- the current time in the game
	 */
	public void update(float time)
	{
		Vector3f trans = movingNode.getLocalTranslation();
		Quaternion rot = movingNode.getLocalRotation();
		Vector3f scale = movingNode.getLocalScale();
		registerSynchronousRec(new TransformEntry(time, movingNode.getName(), trans, rot, scale));
		
		if(q.size() >= buffSize)
		{
			try 
			{
				write();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Determines if the SynchronousRecordEntry given should be written to file. It is only written to file if 
	 * a previously recorded .
	 * 
	 * @param rec - the SynchronousRecordEntry
	 */
	public void registerSynchronousRec(SynchronousRecordEntry rec)
	{
		float time = rec.getTime();
		Class<?> key = rec.getClass();
		SynchronousRecordEntry tempRec = map.get(key);
		
		if( tempRec == null ) 
		{
			map.put(key, rec);
			q.add(rec);
		}
		else if( !tempRec.equalsExceptTime(rec) && isPastTimeInterval(time) )
		{
			map.remove(key);
			map.put(key, rec);
			q.add(rec);
			
			prevTime = time;
		}
		
	}
	
	public void registerAsynchronousRec(AsynchronousRecordEntry rec)
	{
		switch (rec.getType()) 
		{
			case PARENT_CHANGE:
				ParentChangeEntry par = (ParentChangeEntry) rec;
				assessParentChange(par.getObjName(), par.getParentName());
				break;
			case CREATE_PRODUCT:
				CreateProductEntry create = (CreateProductEntry) rec;
				assessParentChange(create.getObjName(), create.getParentName());
				break;
		}
		q.add(rec);
	}
	
	protected boolean isPastTimeInterval(float time)
	{
		return (time > prevTime + interval);
	}
	
	protected void write() throws IOException
	{
		while(q.size() > 0)
		{
			out.writeObject(q.remove());
		}
	}
	
	protected void assessParentChange(String obj, String newParent) throws ObjectNameNotUniqueException
	{		
		Node objNode, parentNode;
		
		//determine whether or not the new parent is the root if not, find the descendant that matches
		if(root.matches(Node.class, newParent))
		{
			parentNode = root;
		}
		else
		{
			List<Node> parentLs = root.descendantMatches(Node.class, newParent);
			if(parentLs.size() > 1)
			{
				throw new ObjectNameNotUniqueException(newParent);
			}
			parentNode = parentLs.get(0);
		}
		
		//find the node that matches obj
		List<Node> objLs = root.descendantMatches(Node.class, obj);
		if(objLs.size() > 1)
		{
			throw new ObjectNameNotUniqueException(obj);
		}
		objNode = objLs.get(0);
		
		
		if(objNode.equals(player))
		{
			if(parentNode.equals(root))
			{
				movingNode = player;
			}
			else
			{
				movingNode = parentNode;
			}
		}

	}
	
	
	/**
	 * Performs writes any information not yet written to file. This should always be called when program
	 * exits normally.
	 * @throws IOException 
	 */
	public void close() throws IOException
	{
		write();
		out.writeObject(new EndReplayEntry(Timer.getTimer().getTime()));
		out.close();
	}
	
}
