package code.world;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import code.app.VirtualWarehouse;
import code.collisions.BoundingBox2D;
import code.model.ModelLoader;
import code.model.TarpWall;
import code.model.action.pallet.StackedPallet;
import code.model.action.pick.Pick;
import code.model.action.product.Product;
import code.model.action.rack.Rack;
import code.util.DatabaseHandler;
import code.vocollect.DBInfoRetriever;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.CloneImportExport;

/**
 * This class provides a node containing the warehouse environment (the warehouse structure 
 * and the objects inside the warehouse).
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class WarehouseWorld extends Node {
	
	public static final String MODEL_DIR = "data/models/";

	//set which things are loaded into the game
	//these should probably all be set to true unless debugging
	public static final boolean loadWarehouseShell = true; //warehouse walls
	public static final boolean loadWarehouseInsides = true; //racks, pallets, etc...
	
	public static final boolean loadRacks = true; //racks
	public static final boolean loadVehicles = true; //palletjacks
	public static final boolean loadObjects = true; //all other objects
	public static final boolean fillRacks = true; //put pallets and product on racks
	public static final boolean miscPallets = true; //get misc pallets and put them in the warehouse
	public static final boolean useArrow = true;
	
	public static final boolean useLocalhost = false;
	
	private static final Logger logger = Logger.getLogger(WarehouseWorld.class.getName());
	
	private ArrayList <BoundingBox2D>boundingBoxes;
	
	//parent node for the vehicles that the player can use
	private Node vehicles;
	
	private RoomManager roomManager;
	
	private Node rooms;
	private HashMap<String, Spatial> roomMap;
	
	
	private ArrayList<StackedPallet> palletsList = new ArrayList<StackedPallet>();
	private ArrayList<Product> productList = new ArrayList<Product>();
	private ArrayList<Pick> pickList = new ArrayList<Pick>();
	
	private HashMap<String, CloneImportExport> cache = new HashMap<String, CloneImportExport>();
	
	private VirtualWarehouse warehouseGame;
	
	private int III = 0;
	
	public WarehouseWorld(VirtualWarehouse vw) {
		
		warehouseGame = vw;	
		
		roomManager = new RoomManager(warehouseGame);

		this.setName("warehouse node");
		
		//create a node for each room for organization
		rooms = roomManager.makeRoomNodes();
		
		//add the warehouse shell and place objects in the warehouse
		boundingBoxes = new ArrayList<BoundingBox2D>();
		buildWarehouse();		
		
		//add tarp walls
		addTarpWalls();
		
		if (loadVehicles)
		{
			vehicles = new VehicleManagerNode(this);
			warehouseGame.getRootNode().attachChild(vehicles);
		}
		
		//use a hashmap to keep track of room nodes so that they can still be referenced
		//after disconnecting them from the scene graph
		roomMap = new HashMap<String, Spatial>();
		List<Spatial> roomsList = rooms.getChildren();
		for (int i=0; i<roomsList.size(); i++){
			roomMap.put(rooms.getChild(i).getName(), rooms.getChild(i));
		}
		
	}
	
	/**
	 * Detaches the room node so that the engine does not have to worry about it.
	 * Used for optimization.
	 * @param roomName Room to hide
	 */
	public void hideRoom(String roomName)
	{
		rooms.detachChildNamed(roomName);
	}
	
	/**
	 * Re-attaches the room node so that it will be shown in the game
	 * @param roomName Room to show
	 */
	public void showRoom(String roomName) 
	{
		rooms.attachChild(roomMap.get(roomName));
	}
	
	public void prefetchModels(){
		System.out.println("Prefecting models...");
		warehouseGame.addToLoadingProgress(5, "Prefetching models...");
		ResultSet result = null;
		try {
			result = DatabaseHandler.executeQuery("SELECT * FROM MODEL;");
		
			Renderer render = warehouseGame.getDisplay().getRenderer();
			
			while(result.next()){
				int id = result.getInt("id");
				String name = result.getString("name");
				String typeid = result.getString("typeid");
				String fileName = result.getString("fileName");
				String folderName = result.getString("folderName");
				String format = result.getString("format");
				ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName+"/", true, render, typeid);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	private void buildWarehouse() {	
		double start = System.currentTimeMillis();
		//prefetchModels();
		warehouseGame.addToLoadingProgress(5, "Establishing Talkman Database Connection...");
		
		//DBInfoRetriever dbInfoRetriever = new DBInfoRetriever("joseph.cedarville.edu","talkman","warehouse","vwburr15");
		
		warehouseGame.addToLoadingProgress(5, "Loading Warehouse Model...");
		
		warehouseGame.addToLoadingProgress(5, "Establishing Model Database Connection...");
		
		String url;
		DBInfoRetriever dbInfoRetriever;
		if(!useLocalhost){
			url = "jdbc:mysql://joseph.cedarville.edu:3306/vwburr";
			dbInfoRetriever = new DBInfoRetriever("joseph.cedarville.edu", "talkman", "warehouse", "vwburr15");
		}
		else{
			url = "jdbc:mysql://localhost:3306/vwburr";
			dbInfoRetriever = new DBInfoRetriever("localhost","talkman","warehouse","vwburr15");
		}
		ResultSet result;
		try {
			//System.out.println("Connecting to "+url);
			warehouseGame.addToLoadingProgress(5, "Loading Warehouse Building");
			
			String query = "select * from MODEL where typeid='warehouse';";
			
			result = DatabaseHandler.executeQuery(query);
			
			//variables for holding data from XML file
			String name;
			String fileName, folderName, format;
			float translationX, translationY, translationZ;
			float scale;
			float rotationW, rotationX, rotationY, rotationZ;

			Node object;
			
			Renderer render = warehouseGame.getDisplay().getRenderer();
			
			if (loadWarehouseShell)
			{
				double shellStart = System.currentTimeMillis();
				while(result.next())
				{
					name = result.getString("name");
					
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
					
					object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName + "/", false, render, name.equals("warehouseCeiling") ? "object" : "ignore");
					if(object != null){
						object.setLocalScale(scale);
						object.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
						object.setLocalRotation(new Quaternion(rotationX, rotationY, rotationZ, rotationW));
						object.setName(name);
						
						object.updateWorldBound();
						object.lock();
					}
					else{
						logger.info("Could not load warehouse shell database from "+MODEL_DIR + folderName + fileName);
					}
					
					warehouseGame.getRootNode().attachChild(object);
				}
				System.out.println("The warehouse model loaded in "+(System.currentTimeMillis() - shellStart)/1000 + " seconds.");
				
			}
			
			warehouseGame.addToLoadingProgress(5, "Loading Warehouse Objects");
			
			/*RoomLoaderThread t;
			t = new RoomLoaderThread(0, roomManager, warehouseGame, this);
			//t.start();
			t.run();
			
			for(int i=1; i<roomManager.getNumRooms(); i++){
				Thread thread = new RoomLoaderThread(i, roomManager, warehouseGame, this);
				thread.start();
				//thread.run();
			}*/
			
			//System.exit(1);
			
			if (loadWarehouseInsides)
			{
				int itemCounter = 0;
				warehouseGame.addToLoadingProgress(5, "Loading Warehouse Environment...");
				
				query = "select * from MODEL where typeid!='warehouse'";
				
				if(!loadRacks){
					query += " and typeid!='rack'";
				}
				if(!loadObjects){
					query += " and typeid!='object'";
				}
				
				query += ";";
				
				result = DatabaseHandler.execute(query);
				
				while(result.next())
				{
					int id = result.getInt("id");
					name = result.getString("name");
					String typeid = result.getString("typeid");					
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
					
					object = null;
					
					//MAIN OBJ loader
					object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName+"/", true, render, typeid);
					Room r = roomManager.getRoom(translationX, translationZ);
					if(object != null)
					{
						//Arrow case
						if (useArrow && name.equals("arrow"))
						{
							warehouseGame.setArrowNode(object);
						}
						
						//rack stuff, in one spot, easier to read
						if(typeid.equals("rack"))
						{
							Rack rack = new Rack(object,name+"_"+id,this,r.getProductType());
							
							ResultSet rack_result = DatabaseHandler.execute("select * from RACK where id = "+id+";");
						    
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
						    	String checkDigit = dbInfoRetriever.getCheckDigit(binNumber1);
						    	rack.attachBinLabel(binNumber1, checkDigit, "LEFT");
						    	checkDigit = dbInfoRetriever.getCheckDigit(binNumber2);
						    	rack.attachBinLabel(binNumber2, checkDigit, "RIGHT");
						    }
						    else if(binNumber1 != null)
						    {
						    	String checkDigit = dbInfoRetriever.getCheckDigit(binNumber1);
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
							    boolean withProduct = (dbInfoRetriever.getIsPossiblePickJob(binNumber1) || dbInfoRetriever.getIsPossiblePickJob(binNumber2));
							    String binName = binNumber1 != null ? binNumber1 : binNumber2 != null ? binNumber2 : "newRack"+id;
							    
							    rack.createThePallets(binName,withProduct);
						    }
						}
						
						object.setLocalScale(scale);
						object.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
						
						Quaternion q = new Quaternion();
						q.fromAngles((float)(rotationX*(Math.PI/180)),(float)(rotationY*(Math.PI/180)),(float)(rotationZ*(Math.PI/180)));
						object.setLocalRotation(q);
						
						object.setName(name);
						
						
						if (r != null)
						{
							((Node)rooms.getChild(r.getName())).attachChild(object);
						}
						else
						{							
							logger.info("no room defined for object " + name);
						}
						
						//optimize
			        	object.updateWorldBound();
						object.lock();
			        	
			        	//ADD CACHE HERE
			        	if(!cache.containsKey(name)){
							cache.put(name, new CloneImportExport());
							cache.get(name).saveClone(object);
			        	}
					}
					else
					{
						logger.info("model " + name + " could not be loaded at " + MODEL_DIR + folderName + fileName);
					}
					
					//increment the loading screen occasionally to show progress
					//(just for effect)
					if (itemCounter == 18)
					{
						warehouseGame.addToLoadingProgress(1);
						itemCounter = 0;
					}
					else
					{
						itemCounter++;
					}
				}

				if (miscPallets)
				{
					result = DatabaseHandler.execute("select * from DPallet;");
					
					int i = 0;
					
					while(result.next())
					{
						float tX = result.getFloat("X_Location");
						float tZ = result.getFloat("Z_Location");
						
						boolean isPickable = result.getBoolean("isPickable");
						
						int h1 = isPickable ? 1 : (int)Math.round((double)FastMath.nextRandomFloat()*4)+1;
						int h2 = isPickable ? 1 : (int)Math.round((double)FastMath.nextRandomFloat()*3);
						
						StackedPallet SDP = new StackedPallet(result.getInt("id"), h1,this,null,"Misc_Pallet"+i,isPickable,h2,null);
						SDP.setLocalTranslation(tX, 0f, tZ);
						
						Room r = roomManager.getRoom(tX, tZ);
						if (r != null)
						{
							((Node)rooms.getChild(r.getName())).attachChild(SDP);
						}
						else
						{							
							logger.info("no room defined for the particular pallet");
						}
						
						i++;
					}
				}
				
				warehouseGame.getRootNode().attachChild(rooms);
			}
			
			System.out.println("It took "+((System.currentTimeMillis()-start)/1000)+" seconds to load the warehouse.");
			ModelLoader.disableCacheRebuild();
			
		} catch (Exception e) {
				 e.printStackTrace();
		}
		
	}
	
	public void makeThis(float x, float z)
	{
		int h1 = (int)Math.round((double)FastMath.nextRandomFloat()*4)+1;
		int h2 = (int)Math.round((double)FastMath.nextRandomFloat()*3)+1;
		
		StackedPallet SDP = new StackedPallet(-1, h1,this,null,"New_Misc_Pallet"+III,true,h2,null);
		SDP.setLocalTranslation(x, 0f, z);
		warehouseGame.getRootNode().attachChild(SDP);
		
		III += 1;
	}
	
	private void addTarpWalls() 
	{
		//Walls separating room 2 from surroundings
		TarpWall tw = new TarpWall("tarpWall1", new Vector3f(5.513f, 0f, -27.3f), new Vector3f(5.523f, 5f, -26.24f)); 
		
		tw = new TarpWall("tarpWall2", new Vector3f(5.513f, 1.8f, -26.24f), new Vector3f(5.523f, 5f, -25.4f)); 
		
		tw = new TarpWall("tarpWall3", new Vector3f(5.5135f, 0f, -25.4f), new Vector3f(5.5235f, 5f, -15.3f)); 
		
		tw = new TarpWall("tarpWall4", new Vector3f(0.06f, 0f, -15.31f), new Vector3f(1.973f, 5f, -15.3f));
		
		tw = new TarpWall("tarpWall5", new Vector3f(1.973f, 5f, -15.3f), new Vector3f(3.173f, 1.8f, -15.31f));
		
		tw = new TarpWall("tarpWall6", new Vector3f(3.173f, 5f, -15.3f), new Vector3f(5.513f, 0f, -15.31f));
		
		
		//walls separating room 1 and 3
		tw = new TarpWall("tarpWall7", new Vector3f(5.523f, 0f, -17.039f), new Vector3f(5.738f, 5f, -17.05f));
		
		tw = new TarpWall("tarpWall8", new Vector3f(6.7f, 0f, -17.039f), new Vector3f(9.2f, 5f, -17.05f));
		
		tw = new TarpWall("tarpWall9", new Vector3f(10f, 0f, -17.039f), new Vector3f(11.74f, 5f, -17.05f));
	}
	
	public VirtualWarehouse getVirtualWarehouse() {
		return warehouseGame;
	}
	
	public Node getRooms() {
		return rooms;
	}
	
	public List<Spatial> getVehicles() {
		return vehicles.getChildren();
	}
	
	public List<StackedPallet> getPalletsList() {
		return palletsList;
	}
	
	public List<Pick> getPickList() {
		return pickList;
	}
	
	public List<Product> getProductList() {
		return productList;
	}
	
	public void addToPickList(Pick p) {
		pickList.add(p);
	}
	
	public void removeFromPickList(Pick p) {
		pickList.remove(p);
	}
	
	public RoomManager getRoomManager() {
		return roomManager;
	}
	
	
	
}