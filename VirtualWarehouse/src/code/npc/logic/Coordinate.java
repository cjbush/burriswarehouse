package code.npc.logic;

/**
 * Contains a coordinate for the NPC to walk to
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Coordinate {
	private float X;
	private float Z;
	
	public Coordinate(float x, float z){
		this.X = x;
		this.Z = z;		
	}
	
	public float getX(){
		return X;
	}
	public float getZ(){
		return Z;
	}
}
