package code.model.player;

/**
 * Some constants that a player or npc players reference
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */
public class Character 
{
	public static final String PLAYER_LOC = "data/models/animated/player/"; //the path of the player
	
	//animation file locations
	public static final String[] STANDING_ANIM = {"stand", PLAYER_LOC + "Idle.md5anim"}; //the idle animation
	public static final String[] IDLE_CARRY = {"idle_carry", PLAYER_LOC + "IdleCarry.md5anim"}; //the idle carrying animation
	
	public static final String[] WALK_ARMS_ANIM = {"walk_arms", PLAYER_LOC + "Walking.md5anim"}; //the walking animation
	public static final String[] WALK_CARRY = {"walk_carry", PLAYER_LOC + "WalkingCarry.md5anim"}; //the walking carrying animation
	
	public static final String[] DRIVING_PALLET_ANIM = {"driving", PLAYER_LOC + "Driving.md5anim"}; //the driving animation
	public static final String[] PICK_UP = {"pickup", PLAYER_LOC + "Pickup.md5anim"}; //the picking up animation
	
	//index locations of the names and files in above constants
	public static final int NAME_INDX = 0;
	public static final int FILE_INDX = 1;
		
}