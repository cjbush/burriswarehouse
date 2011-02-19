package code.npc.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

import code.model.AnimatedModel;
import code.model.player.Character;
import code.model.player.RandomPerson;
import code.util.Coordinate;

public class Npc extends AnimatedModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2393676632933569642L;

	private ArrayList<Coordinate> path;
	private int counter = 0;
	private boolean walking = false;

	// this beginning and ending coordinate values are in grid-space; NOT real
	// space. All values will be
	// ints, and not floats, at least for assignment, because for the
	// translation, we're just going
	// go get the grid node at begX, begY, and translate it on the fly.
	private float beginningX;
	private float beginningZ;

	// private float endingX;
	// private float endingY;

	public Npc(float x, float z, String filePath, String folderPath,
			String defaultAnimName, String animFilePath, int rtWrap,
			Vector3f forwardVector, Vector3f rightVector, Vector3f defaultUp,
			RandomPerson randomPerson, ArrayList<Coordinate> ll) {
		// TODO Auto-generated constructor stub
		super(filePath, folderPath, defaultAnimName, animFilePath, rtWrap,
				forwardVector, rightVector, DEFAULT_UP, randomPerson);
		this.beginningX = x;
		this.beginningZ = z;
		this.path = ll;
		getLocalTranslation().set(
				new Vector3f(this.beginningX, .1f, this.beginningZ).clone());
		addAnimations();
		
		
		setLocalScale(new Vector3f(randomPerson.weight,randomPerson.weight,randomPerson.height));
	}

	public void move() {

		Coordinate cd = path.get(this.counter);

		// so this is the place I want to go. Therefore,
		// if I'm at the place I want to go, then I can go onto the next
		// counter.
		float x = cd.getX();
		float z = cd.getZ();
		float myX = this.getLocalTranslation().getX();
		float myZ = this.getLocalTranslation().getZ();

		float direction = -(FastMath.atan2(myZ - z, myX - x));
		setLocalRotation(new Quaternion().fromAngleAxis(direction,
				Vector3f.UNIT_Y));
		
		// if I'm in the place I want to go, then increment and go to the next place...
		if ((Math.abs(myX - x) < .1f) && (Math.abs(myZ - z) < .1f)) {
			this.counter++;
		} else {

			if ((myX < x) && (Math.abs(myX - x) > .07f)) {
				walking = true;
				this.setLocalTranslation(myX + .035f, .1f, myZ);

			} else if ((myX > x) && (Math.abs(myX - x) > .07f)) {
				walking = true;
				this.setLocalTranslation(myX - .035f, .1f, myZ);
			}

			if ((myZ < z) && Math.abs(myZ - z) > .07f) {
				walking = true;
				this.setLocalTranslation(myX, .1f, myZ + .035f);
			} else if ((myZ > z) && Math.abs(myZ - z) > .07f) {
				walking = true;
				this.setLocalTranslation(myX, .1f, myZ - .035f);
			}
		}

		// need to reset the counter to make the path look cyclical.
		if (counter == path.size()) {
			counter = 0;
		}
		if (!walking) {
			// System.out.println("Stationary.");
			stationaryAnim();
		} else {
			translateForwardAnim();
		}
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
		setActiveAnimation(Character.STANDING_ANIM[Character.NAME_INDX],
				Controller.RT_WRAP, .25f);

	}

	@Override
	public void translateBackwardAnim() {

	}

	@Override
	public void translateDownAnim() {

	}

	@Override
	public void translateForwardAnim() {

		setActiveAnimation(Character.WALK_ARMS_ANIM[Character.NAME_INDX],
				Controller.RT_WRAP, .25f);
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

	private void addAnimations() {
		this.addAnimation(Character.WALK_ARMS_ANIM[Character.NAME_INDX],
				Character.WALK_ARMS_ANIM[Character.FILE_INDX]);

		this.addAnimation(Character.IDLE_CARRY[Character.NAME_INDX],
				Character.IDLE_CARRY[Character.FILE_INDX]);

		this.addAnimation(Character.WALK_CARRY[Character.NAME_INDX],
				Character.WALK_CARRY[Character.FILE_INDX]);

		this.addAnimation(Character.DRIVING_PALLET_ANIM[Character.NAME_INDX],
				Character.DRIVING_PALLET_ANIM[Character.FILE_INDX]);

		this.addAnimation(Character.PICK_UP[Character.NAME_INDX],
				Character.PICK_UP[Character.FILE_INDX]);

		// this.addAnimation(GRAB_ANIM_FILE[NAME_INDX],
		// GRAB_ANIM_FILE[FILE_INDX]);
		// this.addAnimation(WALKING_GRAB[NAME_INDX], WALKING_GRAB[FILE_INDX]);
		// this.addAnimation(WALKING_BACK_GRAB[NAME_INDX],
		// WALKING_BACK_GRAB[FILE_INDX]);
		// this.addAnimation(GRAB_PJ[NAME_INDX], GRAB_PJ[FILE_INDX]);
	}
}
