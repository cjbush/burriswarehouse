package code.gui;

import code.app.WarehouseTrainer;
import code.app.WarehouseTrainer.Mode;

import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.scene.TimedLifeController;

/**
 * Fades a MenuState out by decreasing the alpha value.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
class TransitionFadeOut extends TimedLifeController {
	private static final long serialVersionUID = 1L;

	private WarehouseTrainer app;
	
	/** Game state which is fading away */
	private MenuState fadingOutState;
	
	private GoToState nextState;
	private Mode mode;
	
	public enum GoToState {
		MAIN_MENU, IN_GAME, HIGH_SCORES;
	}

	/**
	 * Creates a controller to fade out the current state over the specified amount
	 * of time. The state must have a setAlpha() method to define what needs to be
	 * faded out. This can be used so that the current state is invisible before the
	 * next state is instantiated (just so that it looks better).
	 * 
	 * @param lifeInSeconds
	 * @param fadingOutState
	 * @param nextState
	 * @param mode
	 * @param app
	 */
	public TransitionFadeOut(float lifeInSeconds, MenuState fadingOutState, GoToState nextState, Mode mode, WarehouseTrainer app) {
		super(lifeInSeconds);
		this.fadingOutState = fadingOutState;
		this.app = app;
		this.nextState = nextState;
		this.mode = mode;
	}
	
	/**
	 * Constructor for going to another menu state. Creates a controller to fade out
	 * the current state over the specified amount
	 * of time. The state must have a setAlpha() method to define what needs to be
	 * faded out. This can be used so that the current state is ensured to be
	 * invisible before the next state is instantiated (looks better).
	 * 
	 * @param lifeInSeconds
	 * @param fadingOutState
	 * @param nextState
	 * @param app
	 */
	public TransitionFadeOut(float lifeInSeconds, MenuState fadingOutState, GoToState nextState, WarehouseTrainer app) {
		this(lifeInSeconds, fadingOutState, nextState, null, app);
	}

	public void updatePercentage(float percentComplete) {

		fadingOutState.setAlpha(1-percentComplete);

		if (percentComplete == 1.0f) {
			fadingOutState.setActive(false);
			GameStateManager.getInstance().detachChild(fadingOutState);
			if (nextState.equals(GoToState.IN_GAME)) {
				app.startInGameState(mode);
			}
			else if (nextState.equals(GoToState.HIGH_SCORES)) {
				app.startHighScoreState();
			}
			else if (nextState.equals(GoToState.MAIN_MENU)) {
				app.startMainMenuState();
			}
		}
		
	}
}
