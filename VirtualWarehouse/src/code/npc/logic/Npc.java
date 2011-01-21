package code.npc.logic;

import java.util.LinkedList;

import com.jme.math.Vector3f;

import code.model.AnimatedModel;
import code.model.player.RandomPerson;


public class Npc extends AnimatedModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2393676632933569642L;

	private LinkedList<Coordinate> path;
	
	// this beginning and ending coordinate values are in grid-space; NOT real space. All values will be
	// ints, and not floats, at least for assignment, because for the translation, we're just going
	// go get the grid node at begX, begY, and translate it on the fly.
	private int beginningX;
	private int beginningY;
	private int endingX;
	private int endingY;
	
	public Npc(int begX, int begY, int endX, int endY, String filePath, String folderPath, String defaultAnimName,
			String animFilePath, int repeatType, Vector3f forwardVector, Vector3f rightVector, Vector3f upVector,
			RandomPerson randomPerson){
		super(filePath, folderPath, defaultAnimName, animFilePath, repeatType, forwardVector, rightVector, DEFAULT_UP, randomPerson);
		this.beginningX = begX;
		this.beginningY = begY;
		this.endingX = endX;
		this.endingY = endY;
		
		getLocalTranslation().set(new Vector3f(begX, .1f, begY).clone());
		
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
	
	public void move(){
		// something like this. then get the location, and move the thing.
		// if the path is out of moves to do, then we're going to have to create another new
		// beginning and ending X,Y to be able to make another path to go.
		//path.remove();
	}

	@Override
	public void rotateXNegAnim() {
		
		
	}

	@Override
	public void rotateXPosAnim() {
		
		
	}

	@Override
	public void rotateYNegAnim() {
		
		
	}

	@Override
	public void rotateYPosAnim() {
		
		
	}

	@Override
	public void rotateZNegAnim() {
		
		
	}

	@Override
	public void rotateZPosAnim() {
		
		
	}

	@Override
	public void stationaryAnim() {
		
		
	}

	@Override
	public void translateBackwardAnim() {
		
		
	}

	@Override
	public void translateDownAnim() {
		
		
	}

	@Override
	public void translateForwardAnim() {
		
		
	}

	@Override
	public void translateLeftAnim() {
		
		
	}

	@Override
	public void translateRightAnim() {
		
		
	}

	@Override
	public void translateUpAnim() {
		
		
	}
}
