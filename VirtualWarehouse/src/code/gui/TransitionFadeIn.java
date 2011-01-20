package code.gui;

import code.app.WarehouseTrainer;
import code.app.WarehouseTrainer.Mode;

import com.jmex.game.state.CameraGameState;
import com.jmex.scene.TimedLifeController;

/**
 * Fades a MenuState in by incrementing alpha values. 
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
class TransitionFadeIn extends TimedLifeController {
	private static final long serialVersionUID = 1L;

	private WarehouseTrainer app;
	
	/** Game state which is fading in */
	private MenuState fadingInState;

	/**
	 * Creates a controller to fade in the current state over the specified amount
	 * of time. The state must have a setAlpha() method to define what needs to be
	 * faded out. This can be used so that the current state is transitioned to
	 * rather than just instantly appearing.
	 * 
	 * @param lifeInSeconds
	 * @param fadingInState
	 * @param app
	 */
	public TransitionFadeIn(float lifeInSeconds, MenuState fadingInState, WarehouseTrainer app) {
		super(lifeInSeconds);
		this.fadingInState = fadingInState;
		this.app = app;
	}
	
	public void updatePercentage(float percentComplete) {

		fadingInState.setAlpha(percentComplete);
		
	}
}
