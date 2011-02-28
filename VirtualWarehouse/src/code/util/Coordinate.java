package code.util;

public class Coordinate {
	private float X;
	private float Z;
	private int next;
	
	public Coordinate(float x, float z){
		this.X = x;
		this.Z = z;		
	}
	
	public Coordinate(float x, float z, int next){
		this.X = x;
		this.Z = z;
		this.next = next;
	}
	
	public float getX(){
		return X;
	}
	public float getZ(){
		return Z;
	}
	public int getNext(){
		return next;
	}
}
