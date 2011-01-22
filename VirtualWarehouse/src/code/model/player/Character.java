package code.model.player;

/**
 *
 */
public class Character 
{
	public static final String PLAYER_LOC = "data/models/animated/player/";
	
	//animation file locations
	public static final String[] STANDING_ANIM = {"stand", PLAYER_LOC + "Idle.md5anim"};
	public static final String[] IDLE_CARRY = {"idle_carry", PLAYER_LOC + "IdleCarry.md5anim"};
	
	public static final String[] WALK_ARMS_ANIM = {"walk_arms", PLAYER_LOC + "Walking.md5anim"};
	public static final String[] WALK_CARRY = {"walk_carry", PLAYER_LOC + "WalkingCarry.md5anim"};
	
	public static final String[] DRIVING_PALLET_ANIM = {"driving", PLAYER_LOC + "Driving.md5anim"};
	public static final String[] PICK_UP = {"pickup", PLAYER_LOC + "Pickup.md5anim"};
	
	//protected static final String[] GRAB_ANIM_FILE = {"grab", PLAYER_LOC + "grab.md5anim"};
	//protected static final String[] WALKING_GRAB = {"walking_grab", PLAYER_LOC + "walking_grab.md5anim"};
	//protected static final String[] WALKING_BACK_GRAB = {"walking_back_grab", PLAYER_LOC + "walking_back_grab.md5anim"};
	//protected static final String[] GRAB_PJ = {"grabPJ", PLAYER_LOC + "grab_pj.md5anim"};
	
	//index locations of the names and files in above constants
	public static final int NAME_INDX = 0;
	public static final int FILE_INDX = 1;
		
}