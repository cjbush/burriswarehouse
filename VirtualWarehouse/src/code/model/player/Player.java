package code.model.player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import code.app.VirtualWarehouse;
import code.collisions.BoundingBox2D;
import code.component.Score;
import code.hud.DebugHUD;
import code.hud.PickErrorDisplay;
import code.model.AnimatedModel;
import code.model.action.pallet.StackedPallet;
import code.model.action.pick.Pick;
import code.model.action.product.Product;
import code.model.player.autocompletion.AutoCompletionHandler;
import code.model.vehicle.Vehicle;
import code.util.DatabaseHandler;
import code.util.WaypointCreator;
import code.world.DeliveryArea;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
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
import com.jmex.angelfont.BitmapFontLoader;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Lots of changes to the player for better control and of course a better model
 * 
 */
@SuppressWarnings("serial")
public class Player extends AnimatedModel {
	
	public static final Vector3f INITIAL_LOCATION = new Vector3f(10.0f, .1f, -5.0f); //initial location of the Player
	public static final Quaternion INITIAL_ROTATION = new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD*90f, new Vector3f(0f,0f,0f));
	
	//sets how close the player must be to the vehicle before they can use it
    public static float MAX_DISTANCE_ENTER_VEHICLE = 2;
    //how close the player must be to product to pick it up
	public static final float MAX_PRODUCT_PICKUP_DISTANCE = 0.5f;
	
	private VirtualWarehouse warehouseGame;
	private InputHandler input;
	public static AutoCompletionHandler ach;
	
	private Spatial collisionModel;
	
	private boolean inVehicle = false;
	private Vehicle vehicleBeingUsed = null;
	private boolean hasProduct = false;
	private code.model.action.pick.Pick currentProduct;
	private float productPlacementHeight;
	private boolean isGrabbing  = false;
	private boolean hasCorrectProduct = false;
	
	private Vector3f lastPosition = new Vector3f();
    private TriangleCollisionResults triCollision = new TriangleCollisionResults();
    private BoundingCollisionResults boundCollision= new BoundingCollisionResults();
    private Vector3f temp = new Vector3f();;
	private Vector3f delta = new Vector3f();
	
	public static final float noVehiclePaddingX = 0.03f;
	public static final float noVehiclePaddingZ = 0.03f;
	
	public static final float vehiclePaddingX = 0.14f;
	public static final float vehiclePaddingZ = 0.92f;
	
	private boolean detectCollisions = true;
	
	private BoundingBox2D playerBox;
	
	private WaypointCreator wp;
    
	public Player(VirtualWarehouse vw) 
	{
		super(Character.PLAYER_LOC + "Guy.md5mesh", "player", Character.STANDING_ANIM[Character.NAME_INDX], 
				Character.STANDING_ANIM[Character.FILE_INDX],	Controller.RT_WRAP, Vector3f.UNIT_X, Vector3f.UNIT_Z,
				DEFAULT_UP, null);
		
		warehouseGame = vw;
		
		collisionModel = this;
		//setupThirdPersonInputHandler();
		input = new PlayerHandler(this);
		ach = new AutoCompletionHandler(this, 1, 0, 2);
		
		getLocalTranslation().set(INITIAL_LOCATION.clone());
		lastPosition.set(INITIAL_LOCATION.clone());
		
		//set correct camera for rendering
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(warehouseGame.getThirdPersonCamera());
		
		addAnimations();
		
		playerBox = new BoundingBox2D();
		
		wp = new WaypointCreator(this);
	}
	
	public void setCollisionDetection(boolean b){ detectCollisions = b;}
	
	public void showArrow(String nextAisle, String nextSlot) {
		
		//DBInfoRetriever dbInfoRetriever = new DBInfoRetriever(
			//	"joseph.cedarville.edu", "talkman", "warehouse", "vwburr15");
		
		float rackLocationX = -1;
		float rackLocationZ = -1;
		try {
			String query = "select * from MODEL where id = (select id from RACK where binNumber1 = '"
					+ nextAisle
					+ nextSlot
					+ "' OR binNumber2 = '"
					+ nextAisle 
					+ nextSlot 
					+ "');";
			// do it.
			//System.out.println(query);
			// get the results
			ResultSet result = DatabaseHandler.execute(query);
			
			if (result.next()){
				// sweet! so theoretically, I should have the real x and real y of the place I want to go.
				// theoretically.
				rackLocationX = result.getFloat("translationX");
				rackLocationZ = result.getFloat("translationZ");
				//con.close(); //probably...
			}
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
		this.warehouseGame.getDebugHUD().setAutoMessage(ach.isActive());
		ach.update();

		//keep player at a constant height
		//setLocalTranslation(getLocalTranslation().x, 0.1f, getLocalTranslation().z);

		//check for collisions
        delta.set(collisionModel.getLocalTranslation().subtract(lastPosition));
		//checkForCollision();
        checkForCollision2D();
		
		if (inVehicle)
		{
			vehicleBeingUsed.checkForPalletPickup(false);
		}
		
		checkVehicleEnterExit(false);
		
		checkProductGetDrop(false);
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("insert_info", false)){
			this.insertInfo();
		}
		wp.update();
	}
	
	/**
	 * Checks if the player is trying to get in or out of a vehicle.
	 */
	public void insertInfo(){
		// inserts into the DPallet table so that 
		// the location happens based on the players location. yea...simply a helper for us.
 		float X = this.getLocalTranslation().getX();
		float Z = this.getLocalTranslation().getZ();
		try {
			String query = "insert into DPallet (X_Location, Z_Location, isRandom, isPickable) values ('" +X+ "'," + " '"+Z +"'," + " '1', '0');";
			DatabaseHandler.executeUpdate(query);			
			warehouseGame.getWarehouseWorld().makeThis(X,Z);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	public void checkVehicleEnterExit(boolean override) {
		
		if ((KeyBindingManager.getKeyBindingManager().isValidCommand("enter_exit_vehicle", false)||override) && !hasProduct)
    	{

    		if (!inVehicle)
    		{
    			Vehicle closest = (Vehicle) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getVehicles(), MAX_DISTANCE_ENTER_VEHICLE, this, override);
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
	public void checkProductGetDrop(boolean override) {

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("pickup_place_product", false) || override)
		{
			//somehow
			//setActiveAnimation(PICK_UP[NAME_INDX], Controller.RT_CLAMP, .25f);
			
			if (!hasProduct)
			{
				//pick up nearest product if player is close enough
				Product closest = (Product) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getProductList(), MAX_PRODUCT_PICKUP_DISTANCE, this, override);
				
				if (closest != null)
				{
					isGrabbing = true;
					
					attachProductToPlayer(closest);
				}
			}
			else
			{
				//set the product down if player is close enough to the pallet
				StackedPallet p = (StackedPallet) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getPalletsList(), MAX_PRODUCT_PICKUP_DISTANCE, currentProduct, override);
				if (p != null)
				{
					attachProductToPallet(p);
					isGrabbing = false;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Node getClosestWithinDistance(List list, float distance, Node object, boolean override) {
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
			
        	if (dist < MAX_PRODUCT_PICKUP_DISTANCE || override)
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
	
	private void attachProductToPlayer(Product closest) {		
		Pick smallBox = null;
		
		if (closest instanceof Product)
		{
			//make a smaller box that the player 'picked up' from the pile
			smallBox = closest.pickSmallProduct();
			warehouseGame.getWarehouseWorld().addToPickList(smallBox);
			smallBox.updateGeometricState(warehouseGame.getTimePerFrame(), true);
			
			if (warehouseGame.isUsingVocollect() && warehouseGame.getPickList().size() > 0)
			{
				//check if the player picked up the right box
				String binNumber = closest.getBinNumber();
				List<String> pickList = warehouseGame.getPickList();
				String pickNumber = pickList.get(0);
				System.out.println("picked from bin " + binNumber + " looking for " + pickNumber);
				//List<String> pickList = warehouseGame.getPickList(); 
				if (closest.getBinNumber().equals(pickList.get(0)))
				{
					System.out.println("correct pick");
					hasCorrectProduct = true;
				}
				else
				{
					System.out.println("wrong pick");
					warehouseGame.getScore().incrementBoxesPickedWrong();
					PickErrorDisplay e = new PickErrorDisplay(BitmapFontLoader.loadDefaultFont());
					warehouseGame.getHudNode().attachChild(e);
				}
			}
			
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
	
	private void attachProductToPallet(StackedPallet p) {
		
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
				ach.advance();
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
			warehouseGame.getWarehouseWorld().removeFromPickList(currentProduct);
			//p.lock();
			
			if (warehouseGame.isUsingVocollect())
			{
				hasCorrectProduct = false;
			}
		}
				
		currentProduct = null;
		hasProduct = false;
	}
    
    /**
     * 
     */
    private void checkForCollision2D(){
    	if(!detectCollisions) return;
    	BoundingBox2D[] boxes = warehouseGame.get2DCollidables();
    	this.updateBoundingBox();
    	for(BoundingBox2D b : boxes){    		
    		if(playerBox.isCollidingWith(b)){
    			
    			if(this.inVehicle()){
    				if(b.getID() == Vehicle.lastPalletPicked) return;
    				this.getVehicleBeingUsed().processCollisions();

	    			updateBoundingBox();
    			}
    			else{
    				float playerX = this.getLocalTranslation().getX();
    				float playerZ = this.getLocalTranslation().getZ();
	    			float diffRightX = Math.abs(playerBox.getRightX()-b.getRightX());
	    			float diffLeftX = Math.abs(playerBox.getLeftX()-b.getLeftX());
	    			float top = Math.abs(playerBox.getUpperZ()-b.getUpperZ());
	    			float bottom = Math.abs(playerBox.getLowerZ()-b.getLowerZ());
	    			
	    			if (diffRightX <= diffLeftX && diffRightX <= top && diffRightX<= bottom){
	    				this.setLocalTranslation(playerBox.getRightX()+diffRightX, .1f, playerZ);
	    			}
	    			
	    			else if (diffLeftX <= diffRightX && diffLeftX <= top && diffLeftX <= bottom){
	    				this.setLocalTranslation(playerBox.getLeftX()-diffLeftX, .1f, playerZ);
	    			}
	    			
	    			else if (top <= bottom && top <= diffRightX && top <= diffLeftX){
	    				this.setLocalTranslation(playerX, .1f, playerBox.getUpperZ()-top);
	    			}
	    			else if (bottom <= top && bottom <= diffRightX && bottom <= diffLeftX){
	    				this.setLocalTranslation(playerX, .1f, playerBox.getLowerZ()+bottom);
	    			}
	    			updateBoundingBox();
    			}
    			
    		}
    	}
    }
    
    private void updateBoundingBox(){
    	float playerX = this.getLocalTranslation().getX();
		float playerZ = this.getLocalTranslation().getZ();
	
		
		if(this.inVehicle && this.getVehicleBeingUsed()!= null){
			Vehicle v = this.getVehicleBeingUsed();
			playerX = v.getLocalTranslation().getX();
			playerZ = v.getLocalTranslation().getZ();
			
			// I need to use rotation to rotate our vehicle box around so that the box is actually
			// around the vehicle. Would be sweet if we had a "rotateBox" function that calculated
			// the exact bounds of the box, but that's now how this works...unfortunately...
			float rotationY = v.getWorldRotation().getY();
			
			if (rotationY < .355f && rotationY > -.355f){
				//reset the position stuff.
				// vehicle is facing positiveZ
				playerBox.setLeftX(playerX - vehiclePaddingX);
				playerBox.setRightX(playerX + vehiclePaddingX);
				playerBox.setLowerZ(playerZ + vehiclePaddingZ);
				playerBox.setUpperZ(playerZ);
			}
			else if (rotationY > .860f){
				// reset the position stuff.
				// vehicle is facing negativeZ
				playerBox.setLeftX(playerX - vehiclePaddingX);
				playerBox.setRightX(playerX + vehiclePaddingX);
				playerBox.setLowerZ(playerZ);
				playerBox.setUpperZ(playerZ - vehiclePaddingZ);
			}
			else if (rotationY < .860f && rotationY > .355f){
				//vehicle is facing positiveX
				playerBox.setLeftX(playerX);
				playerBox.setRightX(playerX + vehiclePaddingZ);
				playerBox.setLowerZ(playerZ + vehiclePaddingX);
				playerBox.setUpperZ(playerZ - vehiclePaddingX);
			}
			else if (rotationY < -.355f){
				// vehicle is facing negativeX
				playerBox.setLeftX(playerX - vehiclePaddingZ);
				playerBox.setRightX(playerX);
				playerBox.setLowerZ(playerZ + vehiclePaddingX);
				playerBox.setUpperZ(playerZ - vehiclePaddingX);
			}
			
			
		
			
			
			warehouseGame.getDebugHUD().setDebugMessage("leftX: "+playerBox.getLeftX()+" lowerZ: "+playerBox.getLowerZ()+" rightX: "+playerBox.getRightX()+" upperZ: "+playerBox.getUpperZ() /*+ "\n"+
					"rotZ: "+ rotationZ + "rotY: " + rotationY + "rotX: "+ rotationX + "rotW: " + rotationW*/);
			
		}
		else{
			
			
			playerBox.setLeftX(playerX-noVehiclePaddingX);
			playerBox.setRightX(playerX+noVehiclePaddingX);
			playerBox.setLowerZ(playerZ+noVehiclePaddingZ);
			playerBox.setUpperZ(playerZ-noVehiclePaddingZ);
			warehouseGame.getDebugHUD().setDebugMessage("leftX: "+playerBox.getLeftX()+" lowerZ: "+playerBox.getLowerZ()+" rightX: "+playerBox.getRightX()+" upperZ: "+playerBox.getUpperZ());
		}
		
    }

    /**
     * Helper function for checkForCollision()
     * @deprecated
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
		return inVehicle && this.getVehicleBeingUsed() != null;
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
	
	public AutoCompletionHandler getACH(){ return ach; }
	
	public DebugHUD getDebugHud(){ return warehouseGame.getDebugHUD(); }
}