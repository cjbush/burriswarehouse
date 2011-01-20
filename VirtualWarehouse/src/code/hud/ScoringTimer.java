package code.hud;

/**
 * Keeps track of the number of seconds that the game has been running.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class ScoringTimer {

	private float timeInSeconds;
	
	/**
	 * A timer that counts up in seconds.
	 */
	public ScoringTimer() {
		timeInSeconds = 0;
	}
	
	/**
	 * Updates the timer based on the time per frame
	 * @param tpf
	 */
	public void update(float tpf) {
		timeInSeconds += tpf;
		//currently a second corresponds to a second in real time regardless of fps; should it
		//slow down or speed up when the frame rate goes up or down?
	}
	
	public String getFormattedTime() {
		String time;
		int seconds = getSeconds();
		int minutes = getMinutes();
		
		if (getSeconds() < 10)
		{
			time = minutes + ":" + "0" + seconds;
		}
		else
		{
			time = minutes + ":" + seconds;
		}
		
		return time;
	}
	
	/**
	 * Returns the total number of seconds passed
	 * @return
	 */
	public float getTimeInSeconds() {
		return timeInSeconds;
	}
	
	/**
	 * Returns the number of seconds passed as a value from 0-59 (not the total
	 * number of seconds passed) - should probably be used with getMinutes() to get
	 * the total time passed
	 * @return
	 */
	public int getSeconds() {
		return (int) (timeInSeconds % 60);
	}
	
	/**
	 * Returns the number of minutes passed - should probably be used with getSeconds()
	 * to get the total time passed
	 * @return
	 */
	public int getMinutes() {
        return (int)(timeInSeconds / 60);
    }
	
}
