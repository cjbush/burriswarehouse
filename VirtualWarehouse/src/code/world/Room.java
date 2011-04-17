package code.world;

import java.util.ArrayList;

/**
 * Stores information about a particular room in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * @See rooms.xml
 * Added productType to each room/area
 * Modified the visible rooms quite a bit
 * 
 */
public class Room {
	
	//store a rectangular area that defines the boundaries of the room
	private float x1;
	private float z1;
	private float x2;
	private float z2;
	
	//store some information about the room
	private String name;
	private String temperature;
	private String productType;
	
	//list of rooms visible from the current room - used for optimization
	private ArrayList<String> visibleRooms;
	
	public Room(String name, String temperature, float x1, float z1, float x2, float z2, String[] visibleRoomsArray, String productType) {
		this.name = name;
		this.temperature = temperature;
		this.productType = productType;
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2; 
		this.z2 = z2;
		
		this.visibleRooms = new ArrayList<String>();
		if (visibleRoomsArray != null)
		{
			for (int i=0; i<visibleRoomsArray.length; i++)
			{
				this.visibleRooms.add(visibleRoomsArray[i]);
			}
		}
		
	}

	public float getX1() {
		return x1;
	}

	public float getZ1() {
		return z1;
	}

	public float getX2() {
		return x2;
	}

	public float getZ2() {
		return z2;
	}

	public String getName() {
		return name;
	}

	public String getTemperature() {
		return temperature;
	}
	
	public ArrayList<String> getVisibleRoomsList() {
		return visibleRooms;
	}
	public String getProductType(){
		return productType;
	}

}
