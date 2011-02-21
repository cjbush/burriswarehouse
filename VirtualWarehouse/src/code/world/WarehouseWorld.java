package code.world;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import code.app.VirtualWarehouse;
import code.collisions.BoundingBox2D;
import code.model.ModelLoader;
import code.model.TarpWall;
import code.model.action.pallet.Pallet;
import code.model.action.pallet.PalletStack;
import code.model.action.pallet.StackedDPallet;
import code.model.action.pick.Product;
import code.model.action.product.DProduct;
import code.model.action.rack.DRack;
import code.model.racklabels.BinNumberLabel;
import code.model.racklabels.CheckDigitLabel;
import code.model.racklabels.RackAisleLabel;
import code.util.DatabaseHandler;
import code.vocollect.DBInfoRetriever;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.util.CloneImportExport;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;

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
	public static final boolean fillRacks = true; //put pallets and product on racks //KEEP THIS FALSE, IDK the outcome yet, still working... - Dan
	public static final boolean miscPallets = true; //get misc pallets and put them in the warehouse
	public static final boolean iWantArrow = true;
	
	public static final boolean useLocalhost = false;
	
	private static final Logger logger = Logger.getLogger(WarehouseWorld.class.getName());
	
	//store a list of objects (tag names) that will be loaded by the game
	private ArrayList<String> tagNames = new ArrayList<String>();
	
	private ArrayList <BoundingBox2D>boundingBoxes;
	
	//parent node for the vehicles that the player can use
	private Node vehicles;
	
	private Node rooms;
	private Node pallets;
	private ArrayList<Pallet> palletsList = new ArrayList<Pallet>();
	private ArrayList<Product> productsList = new ArrayList<Product>();
	private Node palletRooms;
	private HashMap<String, Spatial> roomMap;
	private HashMap<String, Spatial> palletRoomMap;
	
	private ArrayList<DProduct> productList = new ArrayList<DProduct>();
	
	private HashMap<String, CloneImportExport> cache = new HashMap<String, CloneImportExport>();
	
	//a font to be used for labels
	private BitmapFont font;
	
	private enum LabelPosition {
		LEFT, RIGHT, CENTER
	}
	
	private RoomManager roomManager;
	
	private VirtualWarehouse warehouseGame;
	
	private DatabaseHandler db;
	
	public WarehouseWorld(VirtualWarehouse vw) {
		
		warehouseGame = vw;	
		
		roomManager = new RoomManager(warehouseGame);
		
		font = warehouseGame.getFont();
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}

		this.setName("warehouse node");
		
		//create a node for each room for organization
		rooms = roomManager.makeRoomNodes();
		
		//create a separate node for pallets
		pallets = new Node("pallets");
		palletRooms = roomManager.makeRoomNodes();
		pallets.attachChild(palletRooms);	
		
		//add the warehouse shell and place objects in the warehouse
		boundingBoxes = new ArrayList<BoundingBox2D>();
		buildWarehouse();
		
		
		//add tarp walls
		//addTarpWalls();
		
		if (loadVehicles)
		{
			vehicles = new VehicleManagerNode(this);
			this.attachChild(vehicles);
		}
		
		pallets.lock();
		//TODO: fix locking warnings?
		
		//use a hashmap to keep track of room nodes so that they can still be referenced
		//after disconnecting them from the scene graph
		roomMap = new HashMap<String, Spatial>();
		palletRoomMap = new HashMap<String, Spatial>();
		List<Spatial> roomsList = rooms.getChildren();
		for (int i=0; i<roomsList.size(); i++){
			roomMap.put(rooms.getChild(i).getName(), rooms.getChild(i));
			palletRoomMap.put(palletRooms.getChild(i).getName(), palletRooms.getChild(i));
		}
		
	}
	
	/**
	 * Detaches the room node so that the engine does not have to worry about it.
	 * Used for optimization.
	 * @param roomName Room to hide
	 */
	public void hideRoom(String roomName) {
		//cull everything in the room
		//rooms.getChild(roomName).setCullHint(CullHint.Always);
		rooms.detachChildNamed(roomName);
		//cull the pallets separately as they are stored in a pallets node
		//palletRooms.getChild(roomName).setCullHint(CullHint.Always);
		palletRooms.detachChildNamed(roomName);
	}
	
	/**
	 * Re-attaches the room node so that it will be shown in the game
	 * @param roomName Room to show
	 */
	public void showRoom(String roomName) {
		//cull everything in the room
		//rooms.getChild(roomName).setCullHint(CullHint.Dynamic);
		rooms.attachChild(roomMap.get(roomName));
		//cull the pallets separately as they are stored in a pallets node
		//palletRooms.getChild(roomName).setCullHint(CullHint.Dynamic);
		palletRooms.attachChild(palletRoomMap.get(roomName));
	}
	
	/**
	 * Attaches an aisle label (node with a quad with text on it to a rack) to a rack. 
	 * @param rack
	 * @param text
	 * @param position
	 */
	private void attachAisleLabel(Node rack, String text, LabelPosition position) {
		rack.updateGeometricState(0, true);
		
		RackAisleLabel t = new RackAisleLabel(text, font, warehouseGame.getSharedNodeManager());
		float xOffset = (((BoundingBox) rack.getWorldBound()).xExtent);
		
		// TODO:  End of aisle labels are not positioned in the center of double racks, but
		//        rather centered on the interior rack.  For accurate positioning, we need
		//        to know the rack type, single or double, when attaching the label
		Quaternion q = new Quaternion();
		if (position == LabelPosition.LEFT)
		{
			q.fromAngles(0,(float)(90*(Math.PI/180)),0);
			t.setLocalTranslation(xOffset+0.001f, 0.82f, 0);
		}
		else if (position == LabelPosition.RIGHT)
		{
			q.fromAngles(0,(float)(-90*(Math.PI/180)),0);
			t.setLocalTranslation(-xOffset-0.001f, 0.82f, 0);
		}
		t.setLocalRotation(q);
		
		rack.attachChild(t);
	}
	
	private void attachBinLabel(Node rack, String binNumber, String checkNumber, LabelPosition position) {
		rack.updateGeometricState(0, true);
		
		//Quaternion q180 = new Quaternion();
		//q180.fromAngles(0,(float)(180*(Math.PI/180)),0);
		
		BinNumberLabel binLabel = new BinNumberLabel(binNumber, font, warehouseGame.getSharedNodeManager());
		CheckDigitLabel checkDigitLabel = new CheckDigitLabel(checkNumber, font, warehouseGame.getSharedNodeManager());
		
		float zOffset = (((BoundingBox) rack.getWorldBound()).zExtent);
		float xOffset = (((BoundingBox) rack.getWorldBound()).xExtent)/2;
		if (position == LabelPosition.RIGHT)
		{
			binLabel.setLocalTranslation(xOffset, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(xOffset, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		else if (position == LabelPosition.LEFT)
		{
			binLabel.setLocalTranslation(-xOffset, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(-xOffset, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		else if (position == LabelPosition.CENTER)
		{
			binLabel.setLocalTranslation(0.0f, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(0.0f, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		
		//binLabel.setLocalRotation(q180);
		//checkDigitLabel.setLocalRotation(q180);
		
		rack.attachChild(binLabel);
		rack.attachChild(checkDigitLabel);
		
	}
	
	private void buildWarehouse() {
		
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
		Connection con;
		Statement stmt;
		Statement rack_stmt;
		ResultSet result;
		ResultSet rack_result;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, "warehouse", "vwburr15");
			//System.out.println("Connecting to "+url);
			stmt = con.createStatement();
			rack_stmt = con.createStatement();
			warehouseGame.addToLoadingProgress(5, "Model Database Connection Established");
			//set up the XML parser
			
			String query = "select * from MODEL where typeid='warehouse';";
			
			stmt.executeQuery(query);
			
			result = stmt.getResultSet();
			
			System.out.println(query);
			//System.out.println("Query returned
			
			//variables for holding data from XML file
			String name;
			String fileName, folderName, format;
			float translationX, translationY, translationZ;
			float scale;
			float rotationW, rotationX, rotationY, rotationZ;
			
			result.next();
			
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
			
			Node object;
			
			Renderer render = warehouseGame.getDisplay().getRenderer();
			
			if (loadWarehouseShell)
			{
				
				object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName + "/", null, false, warehouseGame.getDisplay().getRenderer(), "ignore");
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
			
			warehouseGame.addToLoadingProgress(5, "Building Loaded");
			
			
			if (loadWarehouseInsides)
			{
				int itemCounter = 0;
				warehouseGame.addToLoadingProgress(5, "Loading Warehouse Environment...");
					
				//Let's use are flags here - Dan
				if (loadRacks)
				{
					if (loadObjects)
					{
						stmt.executeQuery("select * from MODEL where typeid!='warehouse';");
					}
					else
					{
						stmt.executeQuery("select * from MODEL where typeid!='warehouse' and typeid!='object';");
					}
				}
				else
				{
					if (loadObjects)
					{
						stmt.executeQuery("select * from MODEL where typeid!='warehouse' and typeid!='rack';");
					}
					else
					{
						stmt.executeQuery("select * from MODEL where typeid!='warehouse' and typeid!='rack' and typeid!='object';");
					}
				}
				
				result = stmt.getResultSet();
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
					if (iWantArrow && name.equals("arrow")){
						String MODEL_DIR = "data/models/";
						object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName + "/", null, false, warehouseGame.getDisplay().getRenderer(), "ignore");
						if(object != null){
							object.setLocalScale(scale);
							object.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
							//object.setLocalRotation();
							object.setName(name);
							warehouseGame.setArrowNode(object);
						}
					}
					
					//MAIN OBJ loader
					object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName+"/", warehouseGame.getSharedNodeManager(), true, render, typeid);
					
					if(object != null)
					{
						String binNumber1 = null;
						String binNumber2 = null;
						
						//Filling Racks
						if (typeid.equals("rack") && fillRacks && name.equals("racksSingleRaised147"))
						{
							/*
							//special case for racksSingleRaised147 - don't have product, just empty pallets
							object.updateGeometricState(0, true);
							float xOffset = (((BoundingBox) object.getWorldBound()).xExtent)/2;
							float zOffset = (((BoundingBox) object.getWorldBound()).zExtent)/2;
							
							float heightOffset = 0.65f; //guesstimated space between each shelf - get better value
							for (int m=0; m<3; m++){
								int randomNumber = (int)(Math.random()*10);
								Node pallets = new PalletStack(this, randomNumber);
								object.attachChild(pallets);
								pallets.setLocalTranslation(-xOffset, heightOffset*m, 0);
								
								randomNumber = (int)(Math.random()*10);
								pallets = new PalletStack(this, randomNumber);
								object.attachChild(pallets);
								pallets.setLocalTranslation(xOffset, heightOffset*m, 0);
							}
							*/
						}
						else if(typeid.equals("rack"))
						{
							CullState cs = getVirtualWarehouse().getDisplay().getRenderer().createCullState();
							cs.setCullFace(CullState.Face.None);
					        object.setRenderState(cs);
					        
					        
					        rack_stmt.executeQuery("select * from RACK where id = "+id+";");
					        rack_result = rack_stmt.getResultSet();
					        if(rack_result.next())
					        {
						        String aisle = rack_result.getString("rackaisle");
						        String label = rack_result.getString("label");
						        binNumber1 = rack_result.getString("binNumber1");
						        binNumber2 = rack_result.getString("binNumber2");
						        
						        if(label.equalsIgnoreCase("left"))
						        {
						        	attachAisleLabel(object, aisle, LabelPosition.LEFT);
						        }
						        else if(label.equalsIgnoreCase("right"))
						        {
						        	attachAisleLabel(object, aisle, LabelPosition.RIGHT);
						        }
							}
					        
					        if(binNumber1 != null && binNumber2 != null)
					        {
					        	String checkDigit = dbInfoRetriever.getCheckDigit(binNumber1);
					        	attachBinLabel(object, binNumber1, checkDigit, LabelPosition.LEFT);
					        	checkDigit = dbInfoRetriever.getCheckDigit(binNumber2);
					        	attachBinLabel(object, binNumber2, checkDigit, LabelPosition.RIGHT);
					        }
					        else if(binNumber1 != null)
					        {
					        	String checkDigit = dbInfoRetriever.getCheckDigit(binNumber1);
					        	attachBinLabel(object, binNumber1, checkDigit, LabelPosition.CENTER);
					        }
						}
						
						object.setLocalScale(scale);
						object.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
						
						Quaternion q = new Quaternion();
						q.fromAngles((float)(rotationX*(Math.PI/180)),(float)(rotationY*(Math.PI/180)),(float)(rotationZ*(Math.PI/180)));
						object.setLocalRotation(q);
						
						Room r = roomManager.getRoom(translationX, translationZ);
						if (r != null)
						{
							//special case for pallets - need to be put into
							//the palletsRooms node
							if (typeid.equals("pallet"))
							{
								Node roomNode = (Node) palletRooms.getChild(r.getName());
								roomNode.attachChild(object);
								palletsList.add((Pallet) object);
							}
							else
							{
								Node roomNode = (Node) rooms.getChild(r.getName());
								roomNode.attachChild(object);
								object.setName(name);
							}
						}
						else
						{							
							object.setName(name);
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
						
						//special case for rack - put pallet/product on it
						if (typeid.equals("rack") && fillRacks && name.equals("racksSingle147"))
						{
							int height = (int)(Math.random()*10);
							object = new PalletStack(this, height);
						}
						else if (typeid.equals("rack") && fillRacks)
						{
							DRack rack = new DRack();
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
					
					warehouseGame.getRootNode().attachChild(object);
				}
				
				if (miscPallets)
				{
					stmt.executeQuery("select * from DPallet;");

					result = stmt.getResultSet();
					
					int i = 0;
					
					while(result.next())
					{
						float tX = result.getInt("X_Location");
						float tZ = result.getInt("Z_Location");
						
						int h1 = (int)Math.round((double)FastMath.nextRandomFloat()*4)+1;
						int h2 = (int)Math.round((double)FastMath.nextRandomFloat()*3)+1;
						
						StackedDPallet SDP = new StackedDPallet(h1,this,null,"Misc_Pallet"+i,true,h2);
						SDP.setLocalTranslation(tX, 0f, tZ);
						warehouseGame.getRootNode().attachChild(SDP);
						
						i++;
					}
				}
			}
			
		} catch (Exception e) {
				 e.printStackTrace();
		}
		
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
	
	public Node getPalletsNode() {
		return pallets;
	}
	
	public List<Pallet> getPalletsList() {
		return palletsList;
	}
	
	public List<Product> getProductsList() {
		return productsList;
	}
	
	public void addToProductsList(Product p) {
		productsList.add(p);
	}
	
	public void removeFromProductsList(Product p) {
		productsList.remove(p);
	}
	
	public RoomManager getRoomManager() {
		return roomManager;
	}
	
	///\/\/\/\/\/\/\\\
	
	public List<DProduct> getDProductList() {
		return productList;
	}
	
}