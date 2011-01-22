package code.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import code.component.Score;
import code.component.WarehouseChaseCam;
import code.gui.LoadingWindow;
import code.hud.AutoplayHUD;
import code.hud.DebugHUD;
import code.hud.InformationBar;
import code.hud.MessageBox;
import code.hud.MinimapHUD;
import code.hud.PauseDisplay;
import code.hud.ScoreDisplay;
import code.hud.ScoringTimer;
import code.infoicons.InfoIconManager;
import code.model.ModelLoader;
import code.model.SharedMeshManager;
import code.model.player.Character;
import code.model.player.Player;
import code.model.player.RandomPerson;
import code.npc.logic.Npc;
import code.research.playback.Grid;
import code.sound.SoundPlayer;
import code.vocollect.VocollectHandler;
import code.world.DeliveryArea;
import code.world.WarehouseWorld;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.bounding.BoundingBox;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.game.state.GameState;
import com.jmex.model.collada.ColladaImporter;

import java.io.FileInputStream;

/**
 * The in-game state of the Virtual Warehouse application.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class VirtualWarehouse extends GameState {

	private static final Logger logger = Logger.getLogger(VirtualWarehouse.class.getName());
	

	
	private boolean infoIconsEnabled = true;
	
	private boolean DEBUG_MODE = false;
	
	private LoadingWindow loadingScreen;
	private boolean showLoadingScreen = true;
	
	private DisplaySystem display;
	
	//HUD node for displaying debug information
	private DebugHUD debugHUD;
	
	//HUD for Minimap
	private MinimapHUD minimapHUD;
	
	//HUD for message box
	private MessageBox messageBox;
	
	//HUD for information bar
	private InformationBar infoBar;
	
	//display for when the game is paused
	private PauseDisplay pauseDisplay;
	
	//a screen for showing the final score
	private ScoreDisplay finalScoreDisplay;
	
	//the root node of the scene graph
	private Node rootNode;
	
	//the root node for the HUD
	private Node hudNode;
	private Node arrow;
	
	//node for the WarehouseWorld
	private WarehouseWorld world;
	
	//node for the InfoIcons
	private InfoIconManager infoIconManagerNode;
	
	//the player node
	private Player playerNode;
	//auto characters
	private Npc []characters;
	
	//keep track of scoring items
	private Score score;
	
	//a font to be used for HUD items and labels
	private BitmapFont font;
	
	//moved to WarehouseTrainer
    //private int width, height, depth, freq;
    //private boolean fullscreen;
    
    // Our camera objects for viewing the scene
    // tpCam:  the third person camera which is attached to the chaseCam and follows the
    //         player
    private Camera tpCam;
    private CameraNode camNode;
    private ChaseCamera chaseCam;
    
    //the timer
    protected Timer timer;
	
    private ScoringTimer scoringTimer;
    
    //specifies if a game has been successfully completed
    private boolean gameComplete;
    
    protected BasicPassManager pManager;
    protected RenderPass rootPass;
    protected RenderPass hudPass;
    
    //use sharedNodes for optimizing loading of models
    private SharedMeshManager sharedMeshManager;
    
    /**
     * Optimizes the displaying of rooms to increase fps.
     * When false, all rooms of the warehouse are connected to the scene graph.
     * When true, only the room occupied by the player and nearby rooms specified in
     * the rooms XML file are connected.
     */
    private boolean optimizeVisibleRooms = true;
    
    private boolean paused = false;
    
    //the sound player
    SoundPlayer sounds = new SoundPlayer();
    
    //the Vocollect Handler
    VocollectHandler vh;
    private boolean useVocollect = false;
    private DeliveryArea deliveryArea;
    
    private Grid grid;

	private boolean showArrow;
    private static final boolean showGrid = false;
    
    // The game starts here by instantiating the game class (i.e. TermProject)
    // Since TermProject is a JME game class the following methods will be called:
    //   ONCE:
    //      initSystem:  To initialize the graphics environment (e.g., the cameras)
    //      initGame:    To initialize the game content, including the GUIs
    //   EACH FRAME:
    //      update:      To update the game content
    //      render:      To draw the game content
    public static void main(String[] args) {
    	//Start the WarehouseTrainer
    	WarehouseTrainer app = new WarehouseTrainer();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();

	}
    
    public VirtualWarehouse(boolean enableTrainingMode) {
    	display = DisplaySystem.getDisplaySystem();
    	
    	if (showLoadingScreen)
    	{
    		loadingScreen = new LoadingWindow(display.getWidth()-10, display.getHeight()-50);
    	}
    	
    	this.useVocollect = enableTrainingMode;
    }

    public DisplaySystem getDisplay() {
    	return display;
    }

    public Node getRootNode() {
		return rootNode;
	}
	
	public Node getHudNode() {
		return hudNode;
	}
	
	public Node getPlayerNode() {
		return playerNode;
	}
	
	public float getTimePerFrame() {
		return timer.getTimePerFrame();
	}
	
	public ScoringTimer getScoringTimer() {
		return scoringTimer;
	}
	
	public SharedMeshManager getSharedNodeManager() {
		return sharedMeshManager;
	}
	
	public void setSharedMeshManager(SharedMeshManager smm) {
		sharedMeshManager = smm;
	}
	
	public Camera getThirdPersonCamera() {
		return tpCam;
	}
	
	public ChaseCamera getChaseCam() {
		return chaseCam;
	}
	
	public void setChaseCam(ChaseCamera c) {
		chaseCam = c;
	}
	
	public WarehouseWorld getWarehouseWorld() {
		return world;
	}
	
	public Node getCollidables() {
		return world.getCollidables();
	}
	
	public Score getScore() {
		return score;
	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	public boolean isGameComplete() {
		return gameComplete;
	}
	
	public List<String> getPickList() {
		return VocollectHandler.getPickList();
	}
	
	public void setDeliveryArea(DeliveryArea d) {
		deliveryArea = d;
	}
	
	public boolean isUsingVocollect() {
		return useVocollect;
	}
	
	public Node getArrowNode(){
		return arrow;
	}
	
	public void setArrowNode(Node a){
		arrow = a;
	}
	
	public void update(float interpolation) {
		
		//timer info comes from WarehouseTrainer
		//update the time to get the framerate
	    //timer.update();
		//interpolation = timer.getTimePerFrame();
		
		if (DEBUG_MODE)
		{
			debugHUD.setFPS(timer.getFrameRate());
			Vector3f loc = getPlayerNode().getLocalTranslation();
			debugHUD.setPlayerLocation(loc.x, loc.y, loc.z);
		}
        
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

        //moved to cleanup()
//			//if escape was pressed, we exit
//			if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
//				sounds.cleanup();
//				//finished = true;
//				
//				if(isRecording)
//				{
//					recorder.close();
//				}
//			}
		
		
		//if r was pressed, reset position
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("position_reset", false)) {
		   
			playerNode.setLocalScale(1);
			playerNode.setLocalTranslation(Player.INITIAL_LOCATION.clone());
			playerNode.setLocalRotation(Player.INITIAL_ROTATION.clone());
			
		}
		
		//pause
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("pause", false)) {
		   paused = !paused;
		   
		   //show pause menu
		   if (paused) {
			   hudNode.attachChild(pauseDisplay);
			   KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
			   KeyBindingManager.getKeyBindingManager().set("pause", KeyInput.KEY_SPACE);
		   }
		   else {
			   hudNode.detachChild(pauseDisplay);
			   KeyBindingManager.getKeyBindingManager().set("pause", KeyInput.KEY_ESCAPE);
		   }
		   
		   hudNode.updateGeometricState(0, true);
		}
		
		if (!paused)
		{
			//update the keyboard/mouse input (move the player around)
			//and check for collisions
			if (playerNode != null)
			{
				playerNode.update(interpolation);
			}
	        
	        // update the chase camera mode and its attached camera
	        chaseCam.update(interpolation);
	        tpCam.update();
			
			if (infoIconsEnabled)
			{
				infoIconManagerNode.updateInfoIcons();
			}
			
			if (minimapHUD != null)
			{
				minimapHUD.update();
			}
			
			if (world.getRoomManager() != null)
			{
				world.getRoomManager().findCurrentRoom(); //needs to be called before the infobar is updated
				if (optimizeVisibleRooms)
				{
					world.getRoomManager().optimizeAttachedRooms();
				}
			}
			
			if (scoringTimer != null)
			{
				scoringTimer.update(interpolation);
			}
			
			if (infoBar != null)
			{
				infoBar.updateText();
			}
			
			if (deliveryArea != null)
			{
				deliveryArea.checkForPlayer();
			}

			pManager.updatePasses(interpolation);
							
			rootNode.updateGeometricState(interpolation, true);
			hudNode.updateGeometricState(interpolation, true);		
		}

		if(sounds != null)
		{
			//update sounds
			sounds.updateStuff();
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("askForMap", false)){
			if (showArrow){
				showArrow= false;
			}
			else {
				showArrow = true;
			}
		}
		if (showArrow){
			String nextAisle = vh.getAisle();
			String nextSlot = vh.getSlot();
			playerNode.showArrow(nextAisle, nextSlot);
		}
		else {
			Node arrow = this.getArrowNode();
			//arrow.setLocalScale(0f);
		}

	}
 
	public void render(float interpolation) {
		
		//Clear the screen
		display.getRenderer().clearBuffers();

		//Draw the screen
		//display.getRenderer().draw(rootNode);
		
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();
		pManager.renderPasses(display.getRenderer());
		
		if (minimapHUD != null)
		{
			minimapHUD.render();
		}

		//show bounding boxes
		//Debugger.drawBounds( rootNode, display.getRenderer(), true );
		//Debugger.drawNormals(rootNode, display.getRenderer());
	}

	/**
	 * Since this is a GameState, initSystem() and initGame() are not called automatically.
	 * These should probably be called from this class' constructor.
	 */
	protected void initSystem() {

		MouseInput.get().setCursorVisible(false);
		
		initCameras();
		
        // Get a high resolution timer for FPS updates.
        timer = Timer.getTimer();

		// setup some key controls
        
		KeyBindingManager.getKeyBindingManager().set("pause", KeyInput.KEY_ESCAPE);
		
		KeyBindingManager.getKeyBindingManager().set("resume", KeyInput.KEY_SPACE);
        
        KeyBindingManager.getKeyBindingManager().set("position_reset", KeyInput.KEY_R);
        
	}
	
	private void initCameras()
	{
		addToLoadingProgress(5, "Creating Cameras...");
		
		tpCam = display.getRenderer().createCamera(display.getWidth(), display.getHeight());
		
        // set the background to blue
        display.getRenderer().setBackgroundColor(ColorRGBA.lightGray.clone());

        // initialize the cameras using the correct aspect ratio
        //tpCam.setFrustum(1f, 1000f, -width/1000f, width/1000f, height/1000f, -height/1000f);
        //fpCam.setFrustumPerspective(45.0f, (float)display.getWidth() / (float)display.getHeight(), 1.0f, 1000.0f);
        tpCam.setFrustumPerspective(45.0f, (float)display.getWidth() / (float)display.getHeight(), 1.0f, 1000.0f);
        
        // Signal that we've changed our cameras' frustums.
        tpCam.update();
	}

	/**
	 * Since this is a GameState, initSystem() and initGame() are not called automatically.
	 * These should probably be called from this class' constructor.
	 */
	protected void initGame() {		
		
		display.setTitle("Warehouse Trainer");
		
		pManager = new BasicPassManager();
		
		//create the root node of the scene graph 
		rootNode = new Node("scene graph root node");
		
		//create a root node for HUD objects 
		hudNode = new Node("HUD root node");
		
		addToLoadingProgress(5, "Building HUD...");
		
		font = BitmapFontLoader.loadDefaultFont();
		
		//create the HUDs
		minimapHUD = new MinimapHUD(this);
		debugHUD = new DebugHUD();
		messageBox = new MessageBox(font);
		infoBar = new InformationBar(this, font);
		pauseDisplay = new PauseDisplay(font);
		
		// Add rootNode to the pass manager to be rendered
        rootPass = new RenderPass();
        rootPass.add(rootNode);
        pManager.add(rootPass);
        
        //Add the HUD stuff to the pass manager to be rendered
        //Note: minimap is currently handled separately
        hudPass = new RenderPass();
        hudPass.add(hudNode);
        
        hudNode.attachChild(messageBox);
        hudNode.attachChild(infoBar);
        
        if (useVocollect)
        {
        	//Start the VocollectHandler thread
        	vh = new VocollectHandler(messageBox);
        	vh.start();
        	messageBox.setText("Vocollect System", "Waiting for Vocollect connection...");
        }
        else
        {
        	messageBox.setText("Welcome", "Feel free to explore the warehouse.");
        }
        
        
        
        if (DEBUG_MODE)
        {
        	hudNode.attachChild(debugHUD);
        }
        
        pManager.add(hudPass);
		
		//Create a ZBuffer to display pixels closest to the camera above farther ones
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);
		
        //Time for a little optimization. We don't need to render back face triangles, so lets
        //not. This will give us a performance boost for very little effort.
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        rootNode.setRenderState(cs);
        
        //create sharedNode manager to cache models and improve performance
        sharedMeshManager = new SharedMeshManager();
        
        //set up sounds
        sounds.initGameStuff();
        
        scoringTimer = new ScoringTimer();
        score = new Score(scoringTimer);
        infoBar.setScoringTimer(scoringTimer);
        
        //start the game
        makeScene();
		paused = false;

		if (showLoadingScreen)
		{
			loadingScreen.dispose();
		}
		
		//grid = new Grid(rootNode, this, showGrid);
		
		// gotta init the characters...make them. this also
		// creates the path they should take.
		characters = new Npc[5];
		for (int i = 0; i < 5; i++){
			characters[i] = new Npc(FastMath.nextRandomInt(5,15), FastMath.nextRandomInt(0,-10), 4, 4, Character.PLAYER_LOC + "Guy.md5mesh", "Character"+i, Character.STANDING_ANIM[Character.NAME_INDX], 
			Character.STANDING_ANIM[Character.FILE_INDX],	Controller.RT_WRAP, Vector3f.UNIT_X, Vector3f.UNIT_Z,
			code.model.AnimatedModel.DEFAULT_UP, new RandomPerson());
			
			rootNode.attachChild(characters[i]);
		}
	}
 
	protected void reinit() {
	}
 
	public void cleanup() {
		
		sounds.cleanup();
		
		if (useVocollect)
		{
			VocollectHandler.close();
		}
	}
	
	private void buildPlayer() {

		addToLoadingProgress(5, "Creating Player...");
		
		playerNode = new Player(this);
		rootNode.attachChild(playerNode);
		
		playerNode.attachChild(camNode);
		float yOffset = (((BoundingBox) playerNode.getWorldBound()).yExtent)*3;
	    playerNode.updateGeometricState(0, true);

	    //attach a third person camera to the player
	    chaseCam = new WarehouseChaseCam(tpCam, playerNode, this);
	}
	
	private void buildEnvironment() {

		//build the warehouse and attach to scene graph
		world = new WarehouseWorld(this);
		rootNode.attachChild(world);
		
		if (infoIconsEnabled)
		{
			//build and place the info icons in the warehouse
			infoIconManagerNode = new InfoIconManager(this);
			rootNode.attachChild(infoIconManagerNode);
		}
		
		System.out.println("ENVIRONMENT");
		System.out.println("ENVIRONMENT");
		System.out.println("ENVIRONMENT");
		System.out.println("ENVIRONMENT");
		System.out.println("ENVIRONMENT");
		System.out.println("               ");
		System.out.println("            ");
		System.out.println("               ");
		System.out.println("            ");
		System.out.println("               ");
		System.out.println("            ");
	}
	
    private void buildLighting() {
    	//TODO: proper lighting
        /** Set up a basic, default light. */
        //DirectionalLight light = new DirectionalLight();
        //light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        //light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, .5f));
        //light.setDirection(new Vector3f(0,-1,0));
        //light.setShadowCaster(true);
        //light.setEnabled(true);

        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.3f, 0.3f, 0.3f, 1.0f ) );
        light.setLocation( new Vector3f( 100.0f, 100.0f, 100.0f ) );
        light.setEnabled( true );
        
        PointLight light2 = new PointLight();
        light2.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light2.setAmbient( new ColorRGBA( 0.3f, 0.3f, 0.3f, 1.0f ) );
        light2.setLocation( new Vector3f( -100.0f, 100.0f, -100.0f ) );
        light2.setEnabled( true );
        
          /** Attach the light to a lightState and the lightState to rootNode. */
        LightState lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.setGlobalAmbient(new ColorRGBA(.2f, .2f, .2f, 1f));
        lightState.attach(light);
        lightState.attach(light2);
        rootNode.setRenderState(lightState);
        
//        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
//        ms.setAmbient(new ColorRGBA(1, 1, 1, 1));
//        ms.setDiffuse(new ColorRGBA(1, 1, 1, 1));
//        ms.setSpecular(new ColorRGBA(1, 1, 1, 1));
//        ms.setMaterialFace(MaterialFace.FrontAndBack);
//        ms.setShininess(60);
//        ms.setEnabled(true);
//        rootNode.setRenderState(ms);

    	
//		/** Set up a basic, default light. */
//		DirectionalLight light = new DirectionalLight();
//		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
//		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//
//        //Have light face down and the right/front corner
//		light.setDirection(new Vector3f(1, -1, 1));
//		light.setEnabled(true);
//
//		/** Add a second light for more brightness */
//		DirectionalLight light2 = new DirectionalLight();
//		light2.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
//		light2.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//
//		//Have light face down and the left/back corner
//		light.setDirection(new Vector3f(-1, -1, -1));
//		light2.setEnabled(true);
//
//		/** Attach the light to a lightState and the lightState to rootNode. */
//		LightState lightState = display.getRenderer().createLightState();
//		lightState.setGlobalAmbient(new ColorRGBA(1f, 1f, 1f, 0f));
//		lightState.setEnabled(true);
//		lightState.attach(light);
//		lightState.attach(light2);
//		rootNode.setRenderState(lightState);
    	
    }
	
    private void makeScene() {
    	
    	//Add player
        buildPlayer();
        
        //Add lights
        buildLighting();
        //Add warehouse and objects to the world
        buildEnvironment();
        
        rootNode.updateGeometricState(0.0f, true);
        rootNode.updateRenderState();
        rootNode.updateWorldBound();
    }
    
    /**
     * Ends the game. Displays the final score before allowing the player to exit.
     */
    public void showFinalScoreDisplay() {
    	finalScoreDisplay = new ScoreDisplay(font, this);
    	hudNode.attachChild(finalScoreDisplay);
    	paused = true;
    	gameComplete = true;
    }
    
    /**
     * During system and game initialization, when the loading window is
     * displayed, the progress bar can be updated to show the user some
     * idea of when the game will be ready to be played.
     * 
     * @param valueToAdd a percentage value to add to the existing
     * percentage indicated by the progress bar on the loading window
     */
    public void addToLoadingProgress(int valueToAdd) {
    	if (loadingScreen != null) {
    		loadingScreen.addProgress(valueToAdd);
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * During system and game initialization, when the loading window is
     * displayed, the progress bar can be updated to show the user some
     * idea of when the game will be ready to be played.
     * 
     * @param valueToAdd a percentage value to add to the existing
     * percentage indicated by the progress bar on the loading window
     * @param newMessage a descriptive string telling the user what is
     * currently happening to get the game ready (what is being initialized
     * now)
     */
    public void addToLoadingProgress(int valueToAdd, String newMessage) {
    	if (loadingScreen != null) {
    		loadingScreen.addProgress(valueToAdd, newMessage);
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
}
