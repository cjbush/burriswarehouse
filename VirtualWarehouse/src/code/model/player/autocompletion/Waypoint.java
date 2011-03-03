package code.model.player.autocompletion;

public class Waypoint {
	private float x;
	private float z;
	private int pickjob;
	private String description;
	private int section;
	private int sequence;
	private int action;
	
	public Waypoint(float x, float z, int pickjob, String description, int section, int sequence, int action){
		this.setX(x);
		this.setZ(z);
		this.setPickjob(pickjob);
		this.setDescription(description);
		this.setSection(section);
		this.setSequence(sequence);
		this.setAction(action);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getZ() {
		return z;
	}

	public void setPickjob(int pickjob) {
		this.pickjob = pickjob;
	}

	public int getPickjob() {
		return pickjob;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getSection() {
		return section;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getAction() {
		return action;
	}
}
