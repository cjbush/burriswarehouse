package code.world;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import code.app.VirtualWarehouse;
import code.model.action.pallet.StackedPallet;
import code.model.action.rack.Rack;
import code.util.DatabaseHandler;
import code.vocollect.DBInfoRetriever;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jmex.model.util.ModelLoader;

/**
 * When optimizing the game to load faster, we tried to use threads
 * This did not work, but still here just in case.
 * We went many other different routes (such as the .jme solution)
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * (Not used)
 * 
 */

public class RoomLoaderThread extends Thread {
	private int room;
	public static final String MODEL_DIR = "data/models/";
	
	public static final boolean loadRacks = WarehouseWorld.loadRacks;
	public static final boolean loadObjects = WarehouseWorld.loadObjects;
	public static final boolean fillRacks = WarehouseWorld.fillRacks;
	public static final boolean miscPallets = WarehouseWorld.miscPallets;
	
	private DBInfoRetriever vcdb;
	private ResultSet result;
	
	private RoomManager rooms;
	private VirtualWarehouse vw;
	private WarehouseWorld ww;
	
	public RoomLoaderThread(int room, RoomManager rooms, VirtualWarehouse vw, WarehouseWorld ww){
		this.vw = vw;
		this.ww = ww;
		this.room = room;
		this.rooms = rooms;
		vcdb = new DBInfoRetriever("joseph.cedarville.edu", "talkman", "warehouse", "vwburr15");
		//this.run();
	}
	
	@Override
	public void run() {
		double counter = System.currentTimeMillis();
		String query = "select * from MODEL where typeid!='warehouse'";
		Room r = rooms.getRoom(room);
		float x1 = r.getX1();
		float x2 = r.getX2();
		float z1 = r.getZ1();
		float z2 = r.getZ2();
		
		String name, typeid;
		String fileName, folderName, format;
		float translationX, translationY, translationZ;
		float scale;
		float rotationW, rotationX, rotationY, rotationZ;
		
		
		if(!loadRacks){
			query += " and typeid!='racks'";
		}
		if(!loadObjects){
			query += " and typeid!='object'";
		}
		
		query += " and translationX < "+x1+" and translationX > "+x2+" and translationZ > "+z1+" and translationZ < "+z2;
		
		query += ";";	
		
		Node roomNodes = rooms.makeRoomNodes();
		
		try {
			result = DatabaseHandler.executeQuery(query);
			Node object = null;
			Renderer render = vw.getDisplay().getRenderer();
			
			if(room == 0){
				Thread t = new Thread(new UpdateProgressBar(vw));
				t.start();
			}
			
			double totalRackTime = 0;
			
			while(result.next()){
				int id = result.getInt("id");
				name = result.getString("name");
				typeid = result.getString("typeid");
				fileName = result.getString("fileName");
				folderName = result.getString("folderName");
				format = result.getString("format");
				translationX = result.getFloat("translationX");
				translationY = result.getFloat("translationY");
				translationZ = result.getFloat("translationZ");
				scale = result.getFloat("scale");
				rotationW = result.getFloat("rotationW");
				rotationX = result.getFloat("rotationX");
				rotationY = result.getFloat("rotationY");
				rotationZ = result.getFloat("rotationZ");
					
				//object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName+"/", vw.getSharedNodeManager(), true, render, typeid);
				try {
					object = ModelLoader.loadModel(MODEL_DIR+folderName+fileName+"."+format);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (WarehouseWorld.useArrow && name.equals("arrow"))
				{
					vw.setArrowNode(object);
				}
				
				if(typeid.equals("rack"))
				{
					Rack rack = new Rack(object,name,ww,"random");

					ResultSet rack_result = DatabaseHandler.executeQuery("select * from RACK where id = "+id+";");
				    
				    String aisle = "";
			        String label = "";
			        String binNumber1 = null;
					String binNumber2 = null;
				   
					//creating the labels and such
				    if(rack_result.next())
				    {
				        aisle = rack_result.getString("rackaisle");
				        label = rack_result.getString("label");
				        binNumber1 = rack_result.getString("binNumber1");
				        binNumber2 = rack_result.getString("binNumber2"); 
					}
				    
				    if(binNumber1 != null && binNumber2 != null)
				    {
				    	String checkDigit = vcdb.getCheckDigit(binNumber1);
				    	rack.attachBinLabel(binNumber1, checkDigit, "LEFT");
				    	checkDigit = vcdb.getCheckDigit(binNumber2);
				    	rack.attachBinLabel(binNumber2, checkDigit, "RIGHT");
				    }
				    else if(binNumber1 != null)
				    {
				    	String checkDigit = vcdb.getCheckDigit(binNumber1);
				    	rack.attachBinLabel(binNumber1, checkDigit, "CENTER");
				    }	
				    
				    if (label != null && !label.equals("") && !label.equals("none"))
				    {
				    	rack.attachAisleLabel(aisle, label.toUpperCase());
				    }
				    //
				    
				    if (fillRacks)
				    {
					    //creating pallets and products on the racks, 
					    boolean withProduct = (vcdb.getIsPossiblePickJob(binNumber1) || vcdb.getIsPossiblePickJob(binNumber2));
					    String binName = binNumber1 != null ? binNumber1 : binNumber2 != null ? binNumber2 : "newRack"+id;
					    
					    double fillRacksStart = System.currentTimeMillis();
					    rack.createThePallets(binName,withProduct);
					    totalRackTime += System.currentTimeMillis() - fillRacksStart;
					    //System.out.println("It took "+((System.currentTimeMillis()-fillRacksStart)/1000) + " to fill the racks in room "+room);
				    }
				}
				
				object.setLocalScale(scale);
				object.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
				
				Quaternion q = new Quaternion();
				q.fromAngles((float)(rotationX*(Math.PI/180)),(float)(rotationY*(Math.PI/180)),(float)(rotationZ*(Math.PI/180)));
				object.setLocalRotation(q);
				
				//com.jme.util.geom.GeometryTool.minimizeVerts(object, 0);
				
				object.setName(name);
				
				((Node)roomNodes.getChild(r.getName())).attachChild(object);
				
				object.updateWorldBound();
				try{
					object.lock();
				}
				catch(NullPointerException e){
					
				}
			}
			
			
			if (miscPallets && room == 0)
			{
				result = DatabaseHandler.executeQuery("select * from DPallet;");
				
				int i = 0;
				
				while(result.next())
				{
					float tX = result.getFloat("X_Location");
					float tZ = result.getFloat("Z_Location");
					
					int h1 = (int)Math.round((double)FastMath.nextRandomFloat()*4)+1;
					int h2 = (int)Math.round((double)FastMath.nextRandomFloat()*3);
					
					StackedPallet SDP = new StackedPallet(result.getInt("id"), h1,ww,null,"Misc_Pallet"+i,false,h2,"random");
					SDP.setLocalTranslation(tX, 0f, tZ);
					
					//Room r = ww.getRoomManager().getRoom(tX, tZ);
					if (r != null)
					{
						((Node)roomNodes.getChild(r.getName())).attachChild(SDP);
					}
					
					i++;
				}
			}
			
			vw.getRootNode().attachChild(roomNodes);
			System.out.println("Total time to fill racks for room "+room+": "+totalRackTime/1000 + " seconds.");
			//if(room == 3) System.exit(1);			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}

class UpdateProgressBar implements Runnable{

	double counter = System.currentTimeMillis();
	double start = System.currentTimeMillis();
	
	Random generator = new Random(System.currentTimeMillis());
	
	VirtualWarehouse vw;
	
	public UpdateProgressBar(VirtualWarehouse vw){
		this.vw = vw;
	}
	
	@Override
	public void run() {	
		while(true){
			if((System.currentTimeMillis() - start)/1000 >= 65) break;
			vw.addToLoadingProgress(generator.nextInt(2)+1);		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
