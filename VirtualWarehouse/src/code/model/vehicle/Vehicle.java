package code.model.vehicle;

import java.util.ArrayList;
import java.util.List;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.action.pallet.DPallet;
import code.model.player.Player;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;

/**
 * Vehicle will be a node that handles the movement of a vehicle in the game. It
 * has parameters that define its acceleration and speed as well as braking. The
 * turn speed defines what kind of handling it has, and the weight will define
 * things such as friction for drifting, how fast it falls etc.
 * 
 * @author Mark Powell - modified by VirtualWarehouse team
 * 
 */
public class Vehicle extends Node {

	public static final String PALLET_PATH = "data/models/animated/vehicles/autoPalletJack/";
	// sets how many pallets can be held on the vehicle
	public static final int MAX_PALLETS = 2;
	public static final float MAX_DISTANCE_PALLET_PICKUP = 2;
	public static final float PALLET_FROM_BODY_OFFSET = 0.26f;
	public static final float PLAYER_ATTACHED_Z_OFFSET = .74f;
	private Spatial model;
	private float weight;
	private float velocity;
	private float acceleration;
	private float braking;
	private float turnSpeed;

	private float lastX;
	private float lastZ;

	private float maxSpeed = 30;
	private float minSpeed = 10;

	private InputHandler input;
	// keep track of the player's input handler to give it back when
	// exiting the vehicle
	private InputHandler tempPlayerHandler;

	private Node jackRoot;
	private Node jackBody;
	private Node jackLeftFork;
	private Node jackRightFork;

	private Node playerOnOffPositionNode;
	private Node palletOnOffPositionNode;

	private boolean playerUsingVehicle;
	private ArrayList<DPallet> attachedPallets = new ArrayList<DPallet>();

	private VirtualWarehouse warehouseGame;
	private Spatial tempChaseTarget;
	private Spatial tempPlayerCollisionModel;
	private boolean checkPickUp;
	
	private Player player;

	// temporary vector for the rotation
	private static final Vector3f tempVa = new Vector3f();

	/**
	 * Creates a vehicle with default values.
	 */
	public Vehicle(VirtualWarehouse vw) {

		super("Vehicle");
		warehouseGame = vw;
		// Spatial vehicleModel = null;
		// vehicleModel = ModelLoader.loadModel("obj", PALLET_PATH +
		// "autoPalletJackv2_2.obj", PALLET_PATH);
		// setModel(vehicleModel);

		jackRoot = new Node("palletjackRoot");
		jackBody = ModelLoader.loadModel("obj", PALLET_PATH
				+ "autoPalletJack_body.obj", PALLET_PATH);
		jackLeftFork = ModelLoader.loadModel("obj", PALLET_PATH
				+ "autoPalletJack_leftfork.obj", PALLET_PATH);
		jackRightFork = ModelLoader.loadModel("obj", PALLET_PATH
				+ "autoPalletJack_rightfork.obj", PALLET_PATH);

		jackRoot.attachChild(jackBody);
		jackRoot.attachChild(jackLeftFork);
		jackRoot.attachChild(jackRightFork);

		jackBody.setName("jackBody");
		jackLeftFork.setName("jackLeftFork");
		jackRightFork.setName("jackRightFork");

		setModel(jackRoot);

		setAcceleration(8);
		setBraking(15);
		setTurnSpeed(1.5f);
		setWeight(25);
		setMaxSpeed(2);
		setMinSpeed(2);

		this.attachChild(jackRoot);
		this.updateGeometricState(0, true);
		setRenderQueueMode(Renderer.QUEUE_OPAQUE);

		updateGeometricState(0, true);

		// make a node at the position where the player should get on/off the
		// vehicle
		// can't just use the body Node since pallets are attached to it,
		// changing
		// its size
		playerOnOffPositionNode = new Node("OnOffPositionNode");
		this.attachChild(playerOnOffPositionNode);
		playerOnOffPositionNode.setLocalTranslation(0, 0, jackBody
				.getWorldTranslation().z
				+ jackBody.getWorldBound().getCenter().z);
	}

	/**
	 * Basic constructor takes the model that represents the graphical aspects
	 * of this Vehicle.
	 * 
	 * @param id
	 *            the id of the vehicle
	 * @param model
	 *            the model representing the graphical aspects.
	 */
	public Vehicle(String id, Spatial model) {
		super(id);
		setModel(model);
	}

	/**
	 * Constructor takes all performance attributes of the vehicle during
	 * creation.
	 * 
	 * @param id
	 *            the id of the vehicle
	 * @param model
	 *            the model representing the graphical apsects.
	 * @param maxSpeed
	 *            the maximum speed this vehicle can reach. (Unit/sec)
	 * @param minSpeed
	 *            the maximum speed this vehicle can reach while traveling in
	 *            reverse. (Unit/sec)
	 * @param weight
	 *            the weight of the vehicle, determines how quickly it slows
	 *            down when not accelerating
	 * @param acceleration
	 *            how fast this vehicle can reach max speed
	 * @param braking
	 *            how fast this vehicle can slow down and if held long enough
	 *            reverse
	 * @param turnSpeed
	 *            how quickly this vehicle can rotate.
	 */
	public Vehicle(String id, Spatial model, float maxSpeed, float minSpeed,
			float weight, float acceleration, float braking, float turnSpeed) {
		super(id);
		setModel(model);
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.weight = weight;
		this.acceleration = acceleration;
		this.braking = braking;
		this.turnSpeed = turnSpeed;
	}

	/*
	 * Checks for any pallets that the player is trying to pick up.
	 */
	public void checkForPalletPickup() {

		// this still may want to go inside the check for doing the MAX_PALLETS
		// thing...yea...probably...
		// so instead of doing this checking business, perhaps we should just
		// hit a key...something like
		// "p" for pickup or pallet....
		// i'd also have to check for the closest pallet to be the one I want to
		// pick up.

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"pickupPallet", true)) {

			checkPickUp = true;
		} else {
			checkPickUp = false;
		}

		if (checkPickUp) 
		{
			if (attachedPallets.size() < MAX_PALLETS) 
			{

				DPallet p = (DPallet) getClosestWithinDistance(warehouseGame.getWarehouseWorld().getPalletsList(),MAX_DISTANCE_PALLET_PICKUP, this);
				if (!attachedPallets.contains(p)) 
				{
					attachPalletToVehicle(p);
				} 
				else 
				{
					detachPalletFromVehicle(p);
				}
			}
		}
	}

	private void detachPalletFromVehicle(DPallet p) {

		p.unlock();
		p.removeFromParent();

		Vector3f translation = new Vector3f(this.getWorldTranslation().x, 0f,
				this.getWorldTranslation().z);
		Quaternion rotation = this.getLocalRotation().clone();

		p.setLocalTranslation(translation);
		p.getLocalRotation().set(rotation);

		// p.setCollisionModel(tempPlayerCollisionModel);
		warehouseGame.getRootNode().attachChild(p);
		attachedPallets.remove(p);
		
		p.setInUse(false);

	}

	private Node getClosestWithinDistance(List<DPallet> palletsList,
			float distance, Vehicle vehicle) {

		Node closest = null;
		float closestDist = distance;
		for (int i = 0; i < palletsList.size(); i++) {
			Node p = (Node) palletsList.get(i);
			float dist;

			dist = getWorldTranslation().distance(p.getWorldTranslation());
			if (dist < MAX_DISTANCE_PALLET_PICKUP) {
				if (closest != null) {
					if (dist < closestDist) {
						closest = p;
					}
				} else {
					closest = p;
				}
			}
		}
		return closest;
	}

	public void attachPlayerToVehicle(Player player) {

		this.unlock();

		this.player = player;
		input = new VehicleHandler(this, DisplaySystem.getDisplaySystem()
				.getDisplayRenderer(), player);
		// change the input handler to the vehicle input handler
		tempPlayerHandler = player.getInputHandler();
		player.setInputHandler(input);

		// attach the player node to the vehicle node
		Vector3f translation = new Vector3f(.12f, .12f,
				PLAYER_ATTACHED_Z_OFFSET);

		player.removeFromParent();
		player.setLocalTranslation(translation);
		player.setLocalRotation(new Quaternion().fromAngleAxis(0,
				Vector3f.UNIT_Y));

		this.attachChild(player);
		player.setPlayerInVehicle(true);
		playerUsingVehicle = true;

		// change the player's collision model to the vehicle
		tempPlayerCollisionModel = player.getCollisionModel();
		player.setCollisionModel(this);

		// change the camera target to the vehicle
		tempChaseTarget = warehouseGame.getChaseCam().getTarget();
		warehouseGame.getChaseCam().setTarget(this);
	}

	public void removePlayerFromVehicle(Player player) {

		// set the input handler back the one used before the player got in the
		// vehicle
		player.setInputHandler(tempPlayerHandler);
		player.setPlayerInVehicle(false);
		playerUsingVehicle = false;

		// remove the player from the vehicle and attach back to the root node
		player.removeFromParent();
		warehouseGame.getRootNode().attachChild(player);

		// position the player in the world
		Vector3f translation = new Vector3f(playerOnOffPositionNode
				.getWorldTranslation().x, Player.INITIAL_LOCATION.y,
				playerOnOffPositionNode.getWorldTranslation().z);
		Quaternion rotation = this.getLocalRotation().clone();

		player.setLocalTranslation(translation);
		player.getLocalRotation().set(rotation);

		player.setCollisionModel(tempPlayerCollisionModel);

		warehouseGame.getChaseCam().setTarget(tempChaseTarget);

		this.lock();

		player.stationaryAnim();
	}

	public void processCollisions() {

		if (velocity > 0) {
			velocity = -.2f;
		} else if (velocity < 0) {
			velocity = .2f;
		}

	}

	/**
	 * update applies the translation to the vehicle based on the time passed.
	 * called by the VehicleHandler class
	 * 
	 * @param time
	 *            the time between frames
	 */
	public void updateMovement(float time) {
		lastX = this.getLocalTranslation().x;
		lastZ = this.getLocalTranslation().z;

		this.localTranslation.addLocal(this.localRotation.getRotationColumn(2,
				tempVa).multLocal(velocity * time));
	}

	private void attachPalletToVehicle(DPallet pallet) {
		if (pallet != null) {
			pallet.unlock();
			pallet.removeFromParent();

			Vector3f translation;

			// set position based on how many pallets are on the jack
			if (attachedPallets.size() == 0) {
				translation = new Vector3f(0, 0, PALLET_FROM_BODY_OFFSET);
				pallet.setLocalTranslation(translation);
			} else {
				DPallet lastPallet = attachedPallets.get(attachedPallets.size() - 1);
				float offset = lastPallet.getLocalTranslation().getZ()
						+ (((BoundingBox) lastPallet.getWorldBound()).zExtent)
						+ (((BoundingBox) pallet.getWorldBound()).zExtent);

				translation = new Vector3f(0, 0, offset);
				pallet.setLocalTranslation(translation);
			}

			Quaternion q = new Quaternion();
			q.fromAngles(0, (float) (90 * (Math.PI / 180)), 0);
			pallet.getLocalRotation().set(q);

			jackBody.attachChild(pallet);
			attachedPallets.add(pallet);
			pallet.setInUse(true);
		}
	}

	/**
	 * update applies the translation to the vehicle based on the time passed.
	 * called by the VehicleHandler class
	 * 
	 * @param time
	 *            the time between frames
	 */

	/**
	 * Convience method that determines if the vehicle is moving or not. This is
	 * given if the velocity is approximately zero, taking float point rounding
	 * errors into account.
	 * 
	 * @return true if the vehicle is moving, false otherwise.
	 */
	public boolean vehicleIsMoving() {
		return velocity > FastMath.FLT_EPSILON
				|| velocity < -FastMath.FLT_EPSILON;
	}

	/**
	 * Returns a node at the location that the player should get on and off the
	 * vehicle
	 * 
	 * @return
	 */
	public Node getPlayerOnOffPositionNode() {
		return playerOnOffPositionNode;
	}

	/**
	 * set the weight of this vehicle
	 * 
	 * @param weight
	 *            the weight of this vehicle
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * retrieves the weight of this vehicle.
	 * 
	 * @return the weight of this vehicle.
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * retrieves the acceleration of this vehicle.
	 * 
	 * @return the acceleration of this vehicle.
	 */
	public float getAcceleration() {
		return acceleration;
	}

	/**
	 * set the acceleration rate of this vehicle
	 * 
	 * @param acceleration
	 *            the acceleration rate of this vehicle
	 */
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * retrieves the braking speed of this vehicle.
	 * 
	 * @return the braking speed of this vehicle.
	 */
	public float getBraking() {
		return braking;
	}

	/**
	 * set the braking speed of this vehicle
	 * 
	 * @param braking
	 *            the braking speed of this vehicle
	 */
	public void setBraking(float braking) {
		this.braking = braking;
	}

	/**
	 * retrieves the model Spatial of this vehicle.
	 * 
	 * @return the model Spatial of this vehicle.
	 */
	public Spatial getModel() {
		return model;
	}

	/**
	 * sets the model spatial of this vehicle. It first detaches any previously
	 * attached models.
	 * 
	 * @param model
	 *            the model to attach to this vehicle.
	 */
	public void setModel(Spatial model) {
		this.detachChild(this.model);
		this.model = model;
		this.attachChild(this.model);
	}

	/**
	 * retrieves the velocity of this vehicle.
	 * 
	 * @return the velocity of this vehicle.
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * set the velocity of this vehicle
	 * 
	 * @param velocity
	 *            the velocity of this vehicle
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * retrieves the turn speed of this vehicle.
	 * 
	 * @return the turn speed of this vehicle.
	 */
	public float getTurnSpeed() {
		return turnSpeed;
	}

	/**
	 * set the turn speed of this vehicle
	 * 
	 * @param turnSpeed
	 *            the turn speed of this vehicle
	 */
	public void setTurnSpeed(float turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	/**
	 * retrieves the maximum speed of this vehicle.
	 * 
	 * @return the maximum speed of this vehicle.
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * sets the maximum speed of this vehicle.
	 * 
	 * @param maxSpeed
	 *            the maximum speed of this vehicle.
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * retrieves the minimum speed of this vehicle.
	 * 
	 * @return the minimum speed of this vehicle.
	 */
	public float getMinSpeed() {
		return minSpeed;
	}

	/**
	 * sets the minimum speed of this vehicle.
	 * 
	 * @param minSpeed
	 *            the minimum speed of this vehicle.
	 */
	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}

	/**
	 * brake adjusts the velocity of the vehicle based on the braking speed. If
	 * the velocity reaches 0, braking will put the vehicle in reverse up to the
	 * minimum speed.
	 * 
	 * @param time
	 *            the time between frames.
	 */
	public void brake(float time) {
		velocity -= time * braking;
		if (velocity < -minSpeed) {
			velocity = -minSpeed;
		}
	}

	/**
	 * accelerate adjusts the velocity of the vehicle based on the acceleration.
	 * The velocity will continue to raise until maxSpeed is reached, at which
	 * point it will stop.
	 * 
	 * @param time
	 *            the time between frames.
	 */
	public void accelerate(float time) {
		velocity += time * acceleration;
		if (velocity > maxSpeed) {
			velocity = maxSpeed;
		}
	}

	/**
	 * drift calculates what happens when the vehicle is neither braking or
	 * accelerating. The vehicle will slow down based on its weight.
	 * 
	 * @param time
	 *            the time between frames.
	 */
	public void drift(float time) {
		if (velocity < -FastMath.FLT_EPSILON) {
			velocity += ((weight / 5) * time);
			// we are drifting to a stop, so we shouldn't go
			// above 0
			if (velocity > 0) {
				velocity = 0;
			}
		} else if (velocity > FastMath.FLT_EPSILON) {
			velocity -= ((weight / 5) * time);
			// we are drifting to a stop, so we shouldn't go
			// below 0
			if (velocity < 0) {
				velocity = 0;
			}
		}
	}

}