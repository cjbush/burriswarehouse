package code.npc.logic;

import java.util.LinkedList;


public class Npc {
	private LinkedList<Coordinate> path;
	private int beginningX;
	private int beginningY;
	private int endingX;
	private int endingY;
	
	public Npc(int begX, int begY, int endX, int endY){
		this.beginningX = begX;
		this.beginningY = begY;
		this.endingX = endX;
		this.endingY = endY;
		
		path = initPath(this.beginningX, this.beginningY, this.endingX, this.endingY);
	}
	
	public LinkedList<Coordinate> initPath(int begX, int begY, int endX, int endY){
		
		
		// do some path logic here. probably a triple loop. yup.
		Coordinate newCoord = new Coordinate(endY, endY);
		LinkedList<Coordinate> newlist = new LinkedList<Coordinate>();
		newlist.add(newCoord);
		return newlist;
		//return path;
	}
	
	

}
