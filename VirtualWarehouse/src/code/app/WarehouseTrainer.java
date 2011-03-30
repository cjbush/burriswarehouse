package code.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Callable;

import code.component.Score;
import code.gui.*;

import com.jme.app.BaseGame;
import com.jme.app.FixedFramerateGame;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;
import com.jmex.game.state.load.TransitionGameState;

/**
 * The main launching point for the Virtual Warehouse game. It initializes the system,
 * and maintains a GameStateManager to allow for menu states or other states to be used  
 * in addition to the main game state. Extends FixedFramerateGame to ensure that 
 * the framerate does not go too high.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 */

public class WarehouseTrainer extends FixedFramerateGame {

	private static final Logger logger = Logger.getLogger(WarehouseTrainer.class.getName());
	
	private static final boolean SHOW_MAIN_MENU = true;
	
	private static int width;

	private static int height;

	private int depth, freq;
    private boolean fullscreen;
    private Timer timer;    
    private Camera cam;
    
    private VirtualWarehouse warehouseGame;
    
    private boolean checkForHighScore = false;
    
    public enum Mode {
    	FREE_EXPLORATION, TRAINING, RECORD, REPLAY;
    }
    
	public static void main(String[] args) throws Exception {
		WarehouseTrainer app = new WarehouseTrainer();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		//app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}

	protected void update(float interpolation) {
		
		//update the time to get the framerate
	    timer.update();
		interpolation = timer.getTimePerFrame();
		
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();
		
		GameStateManager.getInstance().update(interpolation);
		
		//if escape was pressed, close the game
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
			
			if (warehouseGame != null && warehouseGame.isActive()) {
				boolean gameComplete = warehouseGame.isGameComplete();
				Score score = warehouseGame.getScore();
				warehouseGame.setActive(false);
				GameStateManager.getInstance().cleanup();
				warehouseGame = null;
				
				if (SHOW_MAIN_MENU) {
			        display.getRenderer().setBackgroundColor(new ColorRGBA(.2f,.2f,.2f,1f));
					startMainMenuState();
					if (gameComplete && score.checkForHighScore() == true) {
						startEnterNameState(score);
					}
				}
				else {
					finished = true;
				}
			}
			
		}
		
	}

	protected void render(float interpolation) {
		display.getRenderer().clearBuffers();
		
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();
		
		GameStateManager.getInstance().render(timer.getTimePerFrame());
	}
	
	protected void initGame() {
		display.setTitle("Warehouse Trainer");
		
		// Get a high resolution timer for FPS updates.
        timer = Timer.getTimer();
		
		GameStateManager.create();
		
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		
		if (SHOW_MAIN_MENU)
		{
			startMainMenuState();
		}
		else
		{
			startInGameState();
		}
	}

	protected void initSystem() {
		// store the settings information
        width = settings.getWidth();
        height = settings.getHeight();
        depth = settings.getDepth();
        freq = settings.getFrequency();
        fullscreen = settings.isFullscreen();
        
        try {
            display = DisplaySystem.getDisplaySystem(settings.getRenderer());
            System.out.println("Using adapter: "+display.getAdapter());
            System.out.println("Using renderer: "+display.getDisplayRenderer());
            System.out.println("Using vendor: "+display.getDisplayVendor());
            
            // I AM A WIZARD
//            display.setMinStencilBits(8);
//            display.setMinDepthBits(16);
//            display.setMinAlphaBits(8);
//            display.setMinSamples(8);
            display.createWindow(width, height, depth, freq, fullscreen);
            
            cam = display.getRenderer().createCamera(width, height);
        } catch (JmeException e) {
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
            System.exit(1);
        }

        //Note, this is not the same camera as in teh game.  This is just for start up (loading screen).
        cam.setFrustumPerspective(45.0f, (float)display.getWidth() / (float)display.getHeight(), .1f, 1000f);
        cam.update();
        display.getRenderer().setCamera(cam);
        
        // set the background
        display.getRenderer().setBackgroundColor(new ColorRGBA(.2f,.2f,.2f,1f));
	}

	protected void cleanup() {
		logger.info("Cleaning up resources.");
		
		GameStateManager.getInstance().cleanup();
	}
	
	protected void reinit() {
		
	}

	protected GameSettings getNewSettings() {
		return new PropertiesGameSettings("properties.cfg");
	}
	
	public void startMainMenuState() {
		GameState mainMenu = new MainMenu(this);
		mainMenu.setActive(true);
		GameStateManager.getInstance().attachChild(mainMenu);
	}
	
	public void startInGameState(Mode mode) {
		//set additional options for the various modes
		boolean trainingMode = false;
		if (mode != null)
		{
			if (mode.equals(Mode.TRAINING))
			{
				trainingMode = true;
			}
			else if (mode.equals(Mode.RECORD)) 
			{
				
			}
			else if (mode.equals(Mode.REPLAY)) 
			{
				
			}
		}
		
		warehouseGame = new VirtualWarehouse(trainingMode);
		warehouseGame.initSystem();
		warehouseGame.initGame();
		warehouseGame.setActive(true);
		GameStateManager.getInstance().attachChild(warehouseGame);
		
	}
	
	public void startInGameState() {
		startInGameState(null);
	}
	public void startOptions(){
		GameState optionsMenu = new OptionMenu(this);
		optionsMenu.setActive(true);
		GameStateManager.getInstance().attachChild(optionsMenu);

	}
	public void startHighScoreState() {
		GameState highScoreMenu = new HighScoreMenu(this);
		highScoreMenu.setActive(true);
		GameStateManager.getInstance().attachChild(highScoreMenu);
	}
	
	public void startEnterNameState(Score score) {
		GameState enterNameState = new EnterNameState(this, score);
		enterNameState.setActive(true);
		GameStateManager.getInstance().attachChild(enterNameState);
	}
	
	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}

	public void setFinished() {
		finished = true;
	}
}
