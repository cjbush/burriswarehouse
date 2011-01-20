package code.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

import code.app.VirtualWarehouse;

/**
 * Manages the data that defines the rooms in the warehouse and loads it from an XML file.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class RoomManager {
	
	public static final String ROOMS_DATA_FILE = "src/data/placement/rooms.xml";

	//a list of all the rooms in the warehouse
	private ArrayList<Room> roomsList;
	
	private VirtualWarehouse warehouseGame;
	private Vector3f playerLocation;
	private float playerX;
	private float playerZ;
	private Room currentRoom;
	private Room previousRoom;
	
	/**
	 * Loads in room data from a data file, and creates a list of rooms from
	 * the data.
	 * @param vw
	 */
	public RoomManager(VirtualWarehouse vw) {
		
		warehouseGame = vw;
		roomsList = new ArrayList<Room>();
		
		//read room data from an XML file
		try {
			
			//set up the XML parser
			File file = new File(ROOMS_DATA_FILE);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			doc.getDocumentElement().normalize();
			
			//set up some variables for the XML parser 
			NodeList nodeList;
			Element element;
			NodeList nl;
			
			//variables for holding data from XML file
			String name;
			String temperature;
			float x1, z1, x2, z2;
			String visibleRooms;
						
			//read in XML data for the rest of the models and place them in the warehouse
			NodeList objectList = doc.getElementsByTagName("room");
			for (int i = 0; i < objectList.getLength(); i++)
			{

				org.w3c.dom.Node objectNode = objectList.item(i);
			
				if (objectNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
				{
					Element objectElement = (Element) objectNode;
						
					nodeList = objectElement.getElementsByTagName("name");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					name = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
						
					nodeList = objectElement.getElementsByTagName("temperature");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					temperature = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("x1");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					x1 = Float.parseFloat(((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
						
					nodeList = objectElement.getElementsByTagName("z1");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					z1 = Float.parseFloat(((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("x2");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					x2 = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("z2");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					z2 = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("visibleRooms");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					String[] roomsArray = null;
					if (((org.w3c.dom.Node) nl.item(0)) != null)
					{
						visibleRooms = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
						roomsArray = visibleRooms.split(",");
					}
	
					//create a room object with the specified data
					Room room = new Room(name, temperature, x1, z1, x2, z2, roomsArray);
					roomsList.add(room);
				}
			}
		}
		
		catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * Returns the currentRoom that the player is in. Should be called after calling
	 * findCurrentRoom() to make sure that it returns updated information.
	 * @return
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	/**
	 * Updates the currentRoom variable containing the room that the player is in. 
	 */
	public void findCurrentRoom() {
		previousRoom = currentRoom;
		
		//find the location of the player
		playerLocation = warehouseGame.getPlayerNode().getWorldTranslation();
		playerX = playerLocation.x;
		playerZ = playerLocation.z;
		
		currentRoom = getRoom(playerX, playerZ);
	}
	
	/**
	 * Returns the room that an object at point x, z would be in.
	 */
	public Room getRoom(float x, float z) {
		
		Room room = null;
		for (int i=0; i<roomsList.size(); i++)
		{
			//check if the point is within the boundaries of the room 
			Room r = roomsList.get(i);
			if (x <= r.getX1() && x >= r.getX2() && z >= r.getZ1() && z <= r.getZ2())
			{
				room = r;
			}
		}
		
		return room;
	}
	
	/**
	 * Hides rooms that are not visible from the current room.
	 * This is an optimization to help increase fps. 
	 */
	public void optimizeAttachedRooms() {
		if (currentRoom != previousRoom)
		{
			for (int i=0; i<roomsList.size(); i++)
			{
				if (currentRoom != null)
				{
					Room r = roomsList.get(i);
					if (!currentRoom.getName().equals(r.getName()) && !currentRoom.getVisibleRoomsList().contains(r.getName()))
					{
						warehouseGame.getWarehouseWorld().hideRoom(r.getName());
					}
					else
					{
						warehouseGame.getWarehouseWorld().showRoom(r.getName());
					}
				}
			}
		}
	}
	
	/**
	 * Returns a node containing a node for each room in the warehouse. 
	 */
	public Node makeRoomNodes() {
		Node rooms = new Node("rooms");
		
		for (int i=0; i<roomsList.size(); i++)
		{
			Room r = roomsList.get(i);
			Node roomNode = new Node(r.getName());
			rooms.attachChild(roomNode);
		}
		
		return rooms;
	}

}
