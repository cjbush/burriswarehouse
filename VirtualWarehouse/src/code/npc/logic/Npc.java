package code.npc.logic;

import java.util.LinkedList;


public class Npc {
	private LinkedList<Coordinate> path;
	private int beginningX;
	private int beginningY;
	private int endingX;
	private int endingY;
	
	Npc(int beginningX, int beginningY, int endingX, int endingY){
		this.beginningX = beginningX;
		this.beginningY = beginningY;
		this.endingX = endingX;
		this.endingY = endingY;
		
		path = initPath(this.beginningX, this.beginningY, this.endingX, this.endingY);
	}
	
	public LinkedList<Coordinate> initPath(int begX, int begY, int endX, int endY){
		
		
		// do some path logic here.
		Coordinate newpath = new Coordinate(endY, endY);
		LinkedList<Coordinate> newlist = new LinkedList<Coordinate>();
		newlist.add(newpath);
		return newlist;
		//return path;
	}
	
	

}
