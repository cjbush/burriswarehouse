package code.model.player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import code.app.VirtualWarehouse;
import code.component.Score;
import code.hud.PickErrorDisplay;
import code.model.AnimatedModel;
import code.model.pallet.Pallet;
import code.model.product.LargeProductBox;
import code.model.product.Product;
import code.model.vehicle.Vehicle;
import code.research.playback.Grid;
import code.vocollect.DBInfoRetriever;
import code.world.DeliveryArea;

import code.model.player.Character;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionData;
import com.jme.intersection.TriangleCollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
@SuppressWarnings("serial")
public class Player extends AnimatedModel {
	
	public static final Vector3f INITIAL_LOCATION = new Vector3f(10.0f, .1f, -5.0f);
	public static final Quaternion INITIAL_ROTATION = new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD*90f, new Vector3f(0f,0f,0f));
	
	//sets how close the player must be to the vehicle before they can use it
    public static final float MAX_DISTANCE_ENTER_VEHICLE = 2;
    //how close the player must be to product to pick it up
	public static final float MAX_PRODUCT_PICKUP_DISTANCE = 0.5f;
	
	private VirtualWarehouse warehouseGame;
	private InputHandler input;
	
	private Spatial collisionModel;
	
	private boolean inVehicle = false;
	private Vehicle vehicleBeingUsed = null;
	private boolean hasProduct = false;
	private Product currentProduct;
	private float productPlacementHeight;
	private boolean isGrabbing  = false;
	private boolean hasCorrectProduct = false;
	
	private Vector3f lastPosition = new Vector3f();
    private TriangleCollisionResults triCollision = new TriangleCollisionResults();
    private BoundingCollisionResults boundCollision= new BoundingCollisionResults();
    private Vector3f temp = new Vector3f();;
	private Vector3f delta = new Vector3f();
    
	public Player(VirtualWarehouse vw) 
	{
		super(Character.PLAYER_LOC + "Guy.md5mesh", "player", Character.STANDING_ANIM[Character.NAME_INDX], 
				Character.STANDING_ANIM[Character.FILE_INDX],	Controller.RT_WRAP, Vector3f.UNIT_X, Vector3f.UNIT_Z,
				DEFAULT_UP, new RandomPerson());
		
		warehouseGame = vw;
		this.setName("Player");
		
		collisionModel = this;
		//loadModel();
		setupThirdPersonInputHandler();
		
		getLocalTranslation().set(INITIAL_LOCATION.clone());
		lastPosition.set(INITIAL_LOCATION.clone());
		
		updateGeometricState(0, true);
		
		//set correct camera for rendering
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(warehouseGame.getThirdPersonCamera());
		
		addAnimations();
	}
	
	public void showArrow(String nextAisle, String nextSlot) {
		
		//DBInfoRetriever dbInfoRetriever = new DBInfoRetriever(
			//	"joseph.cedarville.edu", "talkman", "warehouse", "vwburr15");
		
		float rackLocationX = -1;
		float rackLocationZ = -1;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//String url = "jdbc:mysql://joseph.cedarville.edu:3306/vwburr";
			String url = "jdbc:mysql://localhost:3306/vwburr";
			Connection con = DriverManager.getConnection(url, "warehouse", "vwburr15");
			Statement stmt = con.createStatement();
			String query = "select * from MODEL where id = (select id from RACK where binNumber1 = '"
					+ nextAisle
					+ nextSlot
					+ "' OR binNumber2 = '"
					+ nextAisle 
					+ nextSlot 
					+ "');";
			// do it.
			//System.out.println(query);
			stmt.executeQuery(query);
			// get the results
			ResultSet result = stmt.getResultSet();
			
			if (result.next()){
				// sweet! so theoretically, I should have the real x and real y of the place I want to go.
				// theoretically.
				rackLocationX = result.getFloat("translationX");
				rackLocationZ = result.getFloat("translationZ");
				//con.close(); //probably...
			}
			con.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		Player playerNode = this;
		// if these things are still -1, that means that the database stuff isn't
		// hooked up...therefore, I don't want to show the arrow pointing to -1, -1. that's just 
		// silly.
		//if (rackLocationX != -1 && rackLocationZ != -1){
			if (!playerNode.inVehicle()){
				Vector3f translation = playerNode.getLocalTranslation();
				float playerX = translation.x;
				float playerZ = translation.z;
				
				//System.out.println("The place I want to be is: " + rackLocationX + " " + rackLocationZ);
				//System.out.println("The place I am is: " + playerX +" " + playerZ);
				
				// UNDER CONSTRUCTION!!!
				float direction = (FastMath.atan2(playerZ-rackLocationZ, rackLocationX-playerX));
				//System.out.println(direction);
				Node arrow = warehouseGame.getArrowNode();
				arrow.setLocalTranslation(translation.x, translation.y, translation.z);
				arrow.setLocalRotation(new Quaternion().fromAngleAxis(direction, Vector3f.UNIT_Y));
				arrow.setLocalScale(.2f);			
			}
			else if (playerNode.inVehicle()){
				Vector3f translation = playerNode.getVehicleBeingUsed().getLocalTranslation();
				float playerX = translation.x;
				float playerZ = translation.z;
				
				//System.out.println("The place I want to be is: " + rackLocationX + " " + rackLocationZ);
				//System.out.println("The place I am is: " + playerX +" " + playerZ);
				
				// UNDER CONSTRUCTION!!!
				float direction = (FastMath.atan2(playerZ-rackLocationZ, rackLocationX-playerX));
				//System.out.println(direction);
				Node arrow = warehouseGame.getArrowNode();
				arrow.setLocalTranslation(translation.x, translation.y, translation.z);
				arrow.setLocalRotation(new Quaternion().fromAngleAxis(direction, Vector3f.UNIT_Y));
				arrow.setLocalScale(.2f);
			}
		//}	
	}
	
	public void update(float interpolation) {
		
		//keep track of distance traveled for scoring
		Score score = warehouseGame.getScore();
		if (score != null)
		{
			score.addDistance(collisionModel.getWorldTranslation().distance(lastPosition));
		}
		
		//save the current position
		lastPosition.set(collisionModel.getLocalTranslation().clone());
		
		//move the player
		input.update(interpolation);

		//keep player at a constant height
		//setLocalTranslation(getLocalTranslation().x, 0.1f, getLocalTranslation().z);

		//check for collisions
        delta.set(collisionModel.getLocalTranslation().subtract(lastPosition));
		checkForCollision();
		
		//first try at collision detection
		//check for collisions between player and walls/solid objects
//		if (playerNode.hasCollision(world.getCollidables(), false))
//		{
//			logger.info("collision!!!");
//			playerNode.getLocalTranslation().set(thirdLastPosition);
//			playerNode.getLocalRotation().set(lastRotation);
//			playerNode.updateGeometricState(0, true);
//			// if (playerNode.hasCollision(collidables, false)) {
//			//     logger.info("another collision!");
//			// }
//			
//		}
//		playerNode.updateGeometricState(0, true);
//		//keep track of the last few positions for collision detection
//		lastRotation = playerNode.getLocalRotation().clone();
//		thirdLastPosition.set(new Vector3f(secondLastPosition.getX(), secondLastPosition.getY(), secondLastPosition.getZ()));
//		secondLastPosition.set(new Vector3f(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ()));
//		lastPosition.set(playerNode.getLocalTranslation());
		
		if (inVehicle)
		{
			vehicleBeingUsed.checkForPalletPickup();
		}
		
		checkVehicleEnterExit(false);
		
		checkProductGetDrop();
	}
	
	/**
	 * Checks if the player is trying to get in or out of a vehicle.
	 */
	public void checkVehicleEnterExit(boolean controllerOverride) {
		
		if ((KeyBindingManager.getKeyBindingManager().isValidCommand("enter_exit_vehicle", false)||controllerOverride) && !hasProduct)
    	{

    		if (!inVehicle)
    		{
    			Vehicle closest = (Vehicle) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getVehicles(), MAX_DISTANCE_ENTER_VEHICLE, this);
    			if (closest != null)
    			{
    				this.stationaryAnim();
	    			closest.attachPlayerToVehicle(this);
	    			vehicleBeingUsed = closest;
	    			grabPalletJack();
	    			isGrabbing = true;
	    			inVehicle = true;
	    			setActiveAnimation(Character.DRIVING_PALLET_ANIM[Character.NAME_INDX], Controller.RT_WRAP, .25f);
    			}
    		}
    		else
    		{
    			//get out of vehicle
    			vehicleBeingUsed.removePlayerFromVehicle(this);
    			vehicleBeingUsed = null;
    			isGrabbing = false;
    			inVehicle = false;
    		}
    	}
	}
	
	/**
	 * Checks if the player is attempting to pick up or set down product.
	 */
	private void checkProductGetDrop() {

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("pickup_place_product", false))
		{
			//somehow
			//setActiveAnimation(PICK_UP[NAME_INDX], Controller.RT_CLAMP, .25f);
			
			if (!hasProduct)
			{
				//pick up nearest product if player is close enough
				Product closest = (Product) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getProductsList(), MAX_PRODUCT_PICKUP_DISTANCE, this);
				
				
				if (closest != null)
				{
					isGrabbing = true;
					
					attachProductToPlayer(closest);
				}
			}
			else
			{
				//set the product down if player is close enough to the pallet
				Pallet p = (Pallet) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getPalletsList(), MAX_PRODUCT_PICKUP_DISTANCE, currentProduct);
				if (p != null)
				{
					attachProductToPallet(p);
					isGrabbing = false;
				}
			}
		}
	}
	
	/*private void loadModel() {
		Spatial playerModel = null;
		
		*//**
		 * IMPORTANT: This box is added as the FIRST child of the player node to as a work-around
		 * for an md5 model importation problem. When the model was loaded the camera would not
		 * function properly. We solve this problem by adding an ordinary box as the first child of
		 * the node. Since the box is a child of the player node, the box move around in side the 
		 * animated md5 node.
		 *//*
		Box b = new Box("playerBox", new Vector3f(0f, 0f, 0f), .005f, .005f, .005f);
		b.setModelBound(new BoundingBox());
		attachChild(b);
		
		playerModel = ModelLoader.loadModel("md5", PLAYER_LOC + "person.md5mesh", PLAYER_LOC);
		//playerModel = ModelLoader.loadModel("obj", PLAYER_LOC + "Dareus4.obj", PLAYER_LOC);
		//playerModel = ModelLoader.loadModel("md5", PLAYER_LOC + "marine.md5mesh", PLAYER_LOC);
		attachChild(playerModel);
		collisionModel = this;
	}*/
	
	@SuppressWarnings("unchecked")
	private Node getClosestWithinDistance(List list, float distance, Node object) {
		Node closest = null;
		float closestDist = distance;
		for (int i=0; i<list.size(); i++)
		{
			Node p = (Node) list.get(i);
			float dist;
			
			if (p instanceof Vehicle)
			{
				//special case for vehicles - check distance to the body, since that
				//is where the player will get on
				Vehicle v = (Vehicle) p;
				Vector3f position = v.getPlayerOnOffPositionNode().getWorldTranslation();
				dist = object.getWorldTranslation().distance(position);
			}
			else
			{
				//for everything else just get the distance from the object's origin
				dist = object.getWorldTranslation().distance(p.getWorldTranslation());
			}
			
        	if (dist < MAX_PRODUCT_PICKUP_DISTANCE)
        	{
				if (closest != null)
        		{
        			if (dist < closestDist)
        			{
        				closest = p;
        			} 
        		}
        		else
        		{
        			closest = p;
        		}
        	}
		}
		return closest;
	}
	
	private void attachProductToPlayer(Product product) {
		Pallet palletParent = (Pallet) product.getParent().getParent();
		
		//palletParent.unlock();
		
		//product.removeFromParent();
		//palletParent.setModelBound(new BoundingBox());
		//palletParent.updateModelBound();
		//palletParent.updateGeometricState(0.0f, true);
		//if (!palletParent.isInUse())
		//{
		//	palletParent.lock();
		//}
		
		Product smallBox = null;
		
		if (product instanceof LargeProductBox)
		{
			//make a smaller box that the player 'picked up' from the pile
			LargeProductBox largeBox = (LargeProductBox) product;
			smallBox = largeBox.pickSmallProduct();
			warehouseGame.getWarehouseWorld().addToProductsList(smallBox);
			smallBox.updateGeometricState(warehouseGame.getTimePerFrame(), true);
			
			if (warehouseGame.isUsingVocollect() && warehouseGame.getPickList().size() > 0)
			{
				//check if the player picked up the right box
				String binNumber = largeBox.getBinNumber();
				List<String> pickList = warehouseGame.getPickList();
				String pickNumber = pickList.get(0);
				System.out.println("picked from bin " + binNumber + " looking for " + pickNumber);
				//List<String> pickList = warehouseGame.getPickList(); 
				if (largeBox.getBinNumber().equals(pickList.get(0)))
				{
					System.out.println("correct pick");
					hasCorrectProduct = true;
				}
				else
				{
					System.out.println("wrong pick");
					warehouseGame.getScore().incrementBoxesPickedWrong();
					PickErrorDisplay e = new PickErrorDisplay(warehouseGame.getFont());
					warehouseGame.getHudNode().attachChild(e);
				}
			}
			
		}
		else
		{
			//attach the small box to the player
			//product is instance of SmallProductBox
			product.removeFromParent();
			smallBox = product;
			
			palletParent.setModelBound(new BoundingBox());
			palletParent.updateModelBound();
			palletParent.updateGeometricState(0.0f, true);
			
		}
		
		float yOffset = ((BoundingBox) this.getWorldBound()).yExtent;
		float zOffset = (((BoundingBox) this.getWorldBound()).zExtent)+
						(((BoundingBox) smallBox.getWorldBound()).zExtent)/8;
		smallBox.setLocalTranslation(-zOffset, yOffset - 0.09f, 0f);
		smallBox.getLocalRotation().loadIdentity();
    	this.attachChild(smallBox);
    	currentProduct = smallBox;
    	hasProduct = true;
    	
	}
	
	private void attachProductToPallet(Pallet p) {
		
		p.unlock();
		currentProduct.removeFromParent();
		
		if (p.isInUse())
		{
			//attach it to the pallet (which is on the pallet jack)
			
			//TODO: this assumes that the pallet is 3 boxes wide and 3 long
			//place product on the correct place on the pallet
			int boxesOnPallet;
			if (p.getProductNode().getChildren() != null)
			{
				boxesOnPallet = p.getProductNode().getChildren().size();
				if (boxesOnPallet % 9 == 0)
				{
					productPlacementHeight = (((BoundingBox) p.getWorldBound()).yExtent)*2;
				}
			}
			else
			{
				boxesOnPallet = 0;
				productPlacementHeight = (((BoundingBox) p.getWorldBound()).yExtent)*2;
			}
			
			float xOffset = 0;
			float yOffset = productPlacementHeight;
			float zOffset = 0;
			
			if (boxesOnPallet % 9 == 0)
			{
				xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = -(((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 1)
			{
				xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				//zOffset = -(((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 2)
			{
				xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = (((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 3)
			{
				//xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = (((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 4)
			{
				//xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				//zOffset = (((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 5)
			{
				//xOffset = (((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = -(((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 6)
			{
				xOffset = -(((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = (((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 7)
			{
				xOffset = -(((BoundingBox) p.getWorldBound()).xExtent)/2;
				//zOffset = (((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			else if (boxesOnPallet % 9 == 8)
			{
				xOffset = -(((BoundingBox) p.getWorldBound()).xExtent)/2;
				zOffset = -(((BoundingBox) p.getWorldBound()).zExtent)/2;
			}
			
			if (boxesOnPallet % 9 == 0)
			{
				yOffset = (((BoundingBox) p.getWorldBound()).yExtent)*2;
			}
			else
			{
				//Product lastProduct = (Product) p.getProductNode().getChildren().get(boxesOnPallet-1);
				//yOffset = lastProduct.getLocalTranslation().y;
			}
			
			currentProduct.setLocalTranslation(new Vector3f(xOffset, yOffset, zOffset));
			currentProduct.getLocalRotation().loadIdentity();
			
			p.getProductNode().attachChild(currentProduct);
			p.updateGeometricState(Timer.getTimer().getTimePerFrame(), true);
			
			if (warehouseGame.isUsingVocollect() && hasCorrectProduct)
			{
				List<String> pickList = warehouseGame.getPickList(); 
				warehouseGame.getScore().incrementBoxesPickedCorrect();
				pickList.remove(0);
				if (pickList.isEmpty())
				{
					System.out.println("picking complete");
					//make a goal area for the player to go to
					DeliveryArea d = new DeliveryArea(10.5f, -2, warehouseGame, this);
					warehouseGame.setDeliveryArea(d);
				}
				hasCorrectProduct = false;
			}
		}
		else
		{
			//make the product 'disappear' into the large box
			//(the player put it back)
			warehouseGame.getWarehouseWorld().removeFromProductsList(currentProduct);
			//p.lock();
			
			if (warehouseGame.isUsingVocollect())
			{
				hasCorrectProduct = false;
			}
		}
				
		currentProduct = null;
		hasProduct = false;
	}
	
	private void setupThirdPersonInputHandler() {
		
		//setup some properties for the input handler
	    //HashMap<String, Object> tpHandlerProps = new HashMap<String, Object>();
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_ROTATEONLY, "true");
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_DOGRADUAL, "true");
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_TURNSPEED, "3.1415");
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_LOCKBACKWARDS, "false");
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_STRAFETARGETALIGN, "true");
	    //tpHandlerProps.put(ThirdPersonHandler.PROP_CAMERAALIGNEDMOVE, "true");
		
		//input = new ThirdPersonHandler(this, warehouseGame.getThirdPersonCamera(), tpHandlerProps);
	    //input.setActionSpeed(2f);
	    input = new PlayerHandler(this);
	}
	
	/**
	 * Attempt at smooth collision detection (wall sliding) - code found on
	 * http://www.jmonkeyengine.com/forum/index.php?topic=10909.0
	 */
    private void checkForCollision() {

		// check for bounding collision
		if(collisionModel.hasCollision(warehouseGame.getCollidables(), false)) {
			
			//System.out.println("collision");
			
			if (inVehicle && vehicleBeingUsed != null)
			{
				vehicleBeingUsed.processCollisions();
			}
			else
			{
				triCollision.clear();
				collisionModel.calculateCollisions(warehouseGame.getCollidables(), triCollision);
	
				// check for actual triangle collision
				if(triCollision.getNumber() > 0) {
	
					temp.zero();
	
					for(int index = 0; index < triCollision.getNumber(); index++)
						temp.addLocal(getCollisionNormal(triCollision.getCollisionData(index)));
	
					temp.normalizeLocal();
					temp.multLocal(delta.length());
					temp.addLocal(delta);
	
					temp.setY(0); //should never go up or down
					collisionModel.setLocalTranslation(lastPosition.add(temp));
				}
			}
			
		}
	}

    /**
     * Helper function for checkForCollision()
     */
	private Vector3f getCollisionNormal(CollisionData data) {

		Vector3f normal = new Vector3f();

		if(data.getTargetTris().size() > 0) {

			for(int colIndex = 0; colIndex < triCollision.getNumber(); colIndex++) {

				CollisionData triData = triCollision.getCollisionData(colIndex);
				TriMesh mesh = (TriMesh) triData.getTargetMesh();
				Triangle[] triangles = mesh.getMeshAsTriangles(null);
				ArrayList<Integer> triIndex = triData.getTargetTris();

				for(Integer i : triIndex) {

					Triangle t = triangles[i];
					t.calculateNormal();
					normal.addLocal(triData.getTargetMesh().getLocalRotation().mult(t.getNormal()));
				}
			}
		}
		
		return normal.normalizeLocal();
	}
	
	public boolean inVehicle() {
		return inVehicle;
	}
	
	public void setPlayerInVehicle(boolean in) {
		inVehicle = in;
	}
	
	public void setVehicleBeingUsed(Vehicle v) {
		vehicleBeingUsed = v;
	}
	public Vehicle getVehicleBeingUsed (){
		return vehicleBeingUsed;
	}
	
	public void setInputHandler(InputHandler handler) {
		input = handler;
	}
	
	public InputHandler getInputHandler() {
		return input;
	}
	
	public Spatial getCollisionModel() {
		return collisionModel;
	}
	
	public void setCollisionModel(Spatial model) {
		collisionModel = model;
	}
	
	public Vector3f getLastPosition() {
		return lastPosition;
	}
	
	public void setLastPosition(Vector3f pos) {
		lastPosition = pos;
	}
	
	/**
	 * Adds the animations, using the <code>addAnimation</code> method in <code>AnimatedModel</code>
	 */
	private void addAnimations()
	{
		this.addAnimation(Character.WALK_ARMS_ANIM[Character.NAME_INDX], Character.WALK_ARMS_ANIM[Character.FILE_INDX]);
		
		this.addAnimation(Character.IDLE_CARRY[Character.NAME_INDX], Character.IDLE_CARRY[Character.FILE_INDX]);
		
		this.addAnimation(Character.WALK_CARRY[Character.NAME_INDX], Character.WALK_CARRY[Character.FILE_INDX]);
		
		this.addAnimation(Character.DRIVING_PALLET_ANIM[Character.NAME_INDX],Character.DRIVING_PALLET_ANIM[Character.FILE_INDX]);
		
		this.addAnimation(Character.PICK_UP[Character.NAME_INDX],Character.PICK_UP[Character.FILE_INDX]);
		
		//this.addAnimation(GRAB_ANIM_FILE[NAME_INDX], GRAB_ANIM_FILE[FILE_INDX]);
		//this.addAnimation(WALKING_GRAB[NAME_INDX], WALKING_GRAB[FILE_INDX]);
		//this.addAnimation(WALKING_BACK_GRAB[NAME_INDX], WALKING_BACK_GRAB[FILE_INDX]);
		//this.addAnimation(GRAB_PJ[NAME_INDX], GRAB_PJ[FILE_INDX]);
	}
	
	public void grabPalletJack()
	{
		//setActiveAnimation(GRAB_PJ[NAME_INDX], Controller.RT_CLAMP);
	}

	@Override
	public void rotateXNegAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateXPosAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateYNegAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateYPosAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateZNegAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateZPosAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stationaryAnim() 
	{
		if(!isGrabbing)
		{
			setActiveAnimation(Character.STANDING_ANIM[Character.NAME_INDX], Controller.RT_WRAP, .25f);
		}
		else
		{
			setActiveAnimation(Character.IDLE_CARRY[Character.NAME_INDX], Controller.RT_WRAP, .25f);
		}
		
	}

	@Override
	//Stub
	public void translateBackwardAnim(){}

	/**
	 * Stub. Does nothing
	 */
	public void translateDownAnim() {}

	@Override
	public void translateForwardAnim() 
	{
		if(!isGrabbing)
		{
			boolean result = setActiveAnimation(Character.WALK_ARMS_ANIM[Character.NAME_INDX], Controller.RT_WRAP, .25f);
		}
		else
		{
			setActiveAnimation(Character.WALK_CARRY[Character.NAME_INDX], Controller.RT_WRAP, .3f);
		}
	}

	@Override
	public void translateLeftAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translateRightAnim() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translateUpAnim() {
		// TODO Auto-generated method stub
		
	} 
	
}