package code.app;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import code.collisions.BoundingBox2D;
import code.component.Score;
import code.component.WarehouseChaseCam;
import code.gui.LoadingWindow;
import code.hud.AutoCompletionHUD;
import code.hud.DebugHUD;
import code.hud.InformationBar;
import code.hud.MessageBox;
import code.hud.MinimapHUD;
import code.hud.PauseDisplay;
import code.hud.ScoreDisplay;
import code.hud.ScoringTimer;
import code.infoicons.InfoIconManager;
import code.model.SharedMeshManager;
import code.model.player.Character;
import code.model.player.Player;
import code.model.player.RandomPerson;
import code.npc.logic.Npc;
import code.research.recording.Recording;
import code.sound.SoundPlayer;
import code.util.Coordinate;
import code.util.DatabaseHandler;
import code.vocollect.VocollectHandler;
import code.world.DeliveryArea;
import code.world.WarehouseWorld;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.renderer.pass.ShadowedRenderPass;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.angelfont.BitmapFont;
import com.jmex.game.state.GameState;

/**
 * The in-game state of the Virtual Warehouse application.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 */
public class VirtualWarehouse extends GameState {

	private static final Logger logger = Logger
			.getLogger(VirtualWarehouse.class.getName());

	private boolean infoIconsEnabled = true;

	public static boolean DEBUG_MODE = false;

	private LoadingWindow loadingScreen;
	private boolean showLoadingScreen = true;

	private DisplaySystem display;

	// HUD node for displaying debug information
	private DebugHUD debugHUD;
	
	private static AutoCompletionHUD autoHUD;

	// HUD for Minimap
	private MinimapHUD minimapHUD;

	// HUD for message box
	private MessageBox messageBox;

	// HUD for information bar
	private InformationBar infoBar;

	// display for when the game is paused
	private PauseDisplay pauseDisplay;

	// a screen for showing the final score
	private ScoreDisplay finalScoreDisplay;

	// the root node of the scene graph
	private Node rootNode;

	// the root node for the HUD
	private Node hudNode;
	private Node arrow;

	// node for the WarehouseWorld
	private WarehouseWorld world;

	// node for the InfoIcons
	private InfoIconManager infoIconManagerNode;

	// the player node
	private Player playerNode;
	// auto characters
	private Npc[] characters;

	// keep track of scoring items
	private Score score;

	// a font to be used for HUD items and labels
	private BitmapFont font;
	
	private Recording recorder;

	// moved to WarehouseTrainer
	// private int width, height, depth, freq;
	// private boolean fullscreen;

	// Our camera objects for viewing the scene
	// tpCam: the third person camera which is attached to the chaseCam and
	// follows the
	// player
	private Camera tpCam;
	private ChaseCamera chaseCam;
	
	private float nearPlane = .01f;
	private float farPlane = 15f;
	private float angleOfView = 54.3f;
	
	private ColorRGBA mainLight = new ColorRGBA(.954f,.980f,.865f,1f);

	// the timer
	protected Timer timer;

	private ScoringTimer scoringTimer;

	// specifies if a game has been successfully completed
	private boolean gameComplete;

	protected BasicPassManager pManager;
	protected RenderPass rootPass;
	protected RenderPass hudPass;
	
	protected ShadowedRenderPass srp = new ShadowedRenderPass();

	// use sharedNodes for optimizing loading of models
	private SharedMeshManager sharedMeshManager;

	/**
	 * Optimizes the displaying of rooms to increase fps. When false, all rooms
	 * of the warehouse are connected to the scene graph. When true, only the
	 * room occupied by the player and nearby rooms specified in the rooms XML
	 * file are connected.
	 */
	private boolean optimizeVisibleRooms = true;

	private boolean paused = false;

	// the sound player
	SoundPlayer sounds = new SoundPlayer();

	// the Vocollect Handler
	VocollectHandler vh;
	private boolean useVocollect = false;
	private DeliveryArea deliveryArea;

	private static BoundingBox2D [] boundingBoxes;

	private boolean showArrow;
	private static final boolean showGrid = false;
	private boolean recording = false;

	// The game starts here by instantiating the game class (i.e. TermProject)
	// Since TermProject is a JME game class the following methods will be
	// called:
	// ONCE:
	// initSystem: To initialize the graphics environment (e.g., the cameras)
	// initGame: To initialize the game content, including the GUIs
	// EACH FRAME:
	// update: To update the game content
	// render: To draw the game content
	public static void main(String[] args) {
		// Start the WarehouseTrainer
		WarehouseTrainer app = new WarehouseTrainer();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		//app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();

	}

	public VirtualWarehouse(boolean enableTrainingMode) {
		display = DisplaySystem.getDisplaySystem();

		if (showLoadingScreen) {
			loadingScreen = new LoadingWindow(display.getWidth() - 10, display
					.getHeight() - 50);
		}

		this.useVocollect = false;
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

	public Score getScore() {
		return score;
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

	public Node getArrowNode() {
		return arrow;
	}

	public void setArrowNode(Node a) {
		arrow = a;
	}

	public void update(float interpolation) {

		// timer info comes from WarehouseTrainer
		// update the time to get the framerate
		// timer.update();
		// interpolation = timer.getTimePerFrame();


		if (DEBUG_MODE) {
			debugHUD.setFPS(timer.getFrameRate());
			Vector3f loc = getPlayerNode().getLocalTranslation();
			if(((Player)getPlayerNode()).inVehicle()){
				loc = ((Player)getPlayerNode()).getVehicleBeingUsed().getLocalTranslation();
			}
			debugHUD.setPlayerLocation(loc.x, loc.y, loc.z);
		}

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
				.execute();

		// moved to cleanup()
		// //if escape was pressed, we exit
		// if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
		// false)) {
		// sounds.cleanup();
		// //finished = true;
		//				
		// if r was pressed, reset position
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"position_reset", false)) {

			if (!playerNode.inVehicle()){
				playerNode.setLocalScale(1);
				playerNode.setLocalTranslation(Player.INITIAL_LOCATION.clone());
				playerNode.setLocalRotation(Player.INITIAL_ROTATION.clone());
			}
			else{
				playerNode.getVehicleBeingUsed().setLocalScale(1);
				playerNode.getVehicleBeingUsed().setLocalTranslation(Player.INITIAL_LOCATION.clone());
				playerNode.getVehicleBeingUsed().setLocalRotation(Player.INITIAL_ROTATION.clone());
			}

		}
		
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("record",false)){
			if(recording){
				recorder.cleanup();
				recording = false;
			}
			else{
				recorder = new Recording();
				recording = true;
			}
		}

		// pause
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("pause",
				false)) {
			paused = !paused;

			// show pause menu
			if (paused) {
				hudNode.attachChild(pauseDisplay);
				KeyBindingManager.getKeyBindingManager().set("exit",
						KeyInput.KEY_ESCAPE);
				KeyBindingManager.getKeyBindingManager().set("pause",
						KeyInput.KEY_SPACE);
			} else {
				hudNode.detachChild(pauseDisplay);
				KeyBindingManager.getKeyBindingManager().set("pause",
						KeyInput.KEY_ESCAPE);
			}

			hudNode.updateGeometricState(0, true);
		}

		if (!paused) {
			// update the keyboard/mouse input (move the player around)
			// and check for collisions
			if (playerNode != null) {
				playerNode.update(interpolation);
			}

			// update the chase camera node and its attached camera
			chaseCam.update(interpolation);
			tpCam.update();

			if (infoIconsEnabled) {
				infoIconManagerNode.updateInfoIcons();
			}

			if (minimapHUD != null) {
				minimapHUD.update();
			}

			//Fix because they are dumb
			if (world.getRoomManager() != null) {
				world.getRoomManager().findCurrentRoom(); // needs to be called
				// before the
				// infobar is
				// updated
				if (optimizeVisibleRooms) {
					world.getRoomManager().optimizeAttachedRooms();
				}
			}

			if (scoringTimer != null) {
				scoringTimer.update(interpolation);
			}

			if (infoBar != null) {
				infoBar.updateText();
			}

			if (deliveryArea != null) {
				deliveryArea.checkForPlayer();
			}

			pManager.updatePasses(interpolation);

			rootNode.updateGeometricState(interpolation, true);
			hudNode.updateGeometricState(interpolation, true);
			

			for (int i = 0; i < characters.length; i++)
			{
				characters[i].move();
			}
		}

		if (sounds != null) {
			// update sounds
			sounds.updateStuff();
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"askForMap", false)) {
			if (showArrow) {
				showArrow = false;
			} else {
				showArrow = true;
			}
		}
		
		if (showArrow) 
		{
			String nextAisle = vh.getAisle();
			String nextSlot = vh.getSlot();
			playerNode.showArrow(nextAisle, nextSlot);
		} 
		else 
		{
			//Node arrow = this.getArrowNode();
			//arrow.setLocalScale(0f);
		}
		

	

	}

	public void render(float interpolation) {

		// Clear the screen
		display.getRenderer().clearBuffers();

		// Draw the screen
		// display.getRenderer().draw(rootNode);

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
				.execute();
		pManager.renderPasses(display.getRenderer());

		if (minimapHUD != null) {
			minimapHUD.render();
		}

		// show bounding boxes
		// Debugger.drawBounds( rootNode, display.getRenderer(), true );
		// Debugger.drawNormals(rootNode, display.getRenderer());
	}

	/**
	 * Since this is a GameState, initSystem() and initGame() are not called
	 * automatically. These should probably be called from this class'
	 * constructor.
	 */
	protected void initSystem() {

		MouseInput.get().setCursorVisible(false);

		initCameras();

		// Get a high resolution timer for FPS updates.
		timer = Timer.getTimer();

		// setup some key controls

		KeyBindingManager.getKeyBindingManager().set("pause",
				KeyInput.KEY_ESCAPE);

		KeyBindingManager.getKeyBindingManager().set("resume",
				KeyInput.KEY_SPACE);

		KeyBindingManager.getKeyBindingManager().set("position_reset",
				KeyInput.KEY_R);
		
		KeyBindingManager.getKeyBindingManager().set("autocomplete", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("record",KeyInput.KEY_X);

	}

	private void initCameras() {
		addToLoadingProgress(5, "Creating Cameras...");
		tpCam = display.getRenderer().createCamera(display.getWidth(),display.getHeight());
		
		// initialize the cameras using the correct aspect ratio
		tpCam.setFrustumPerspective(45.0f, (float) display.getWidth() / (float) display.getHeight(), nearPlane, farPlane);

		display.getRenderer().setBackgroundColor(ColorRGBA.lightGray);
		
		// Signal that we've changed our cameras' frustums.
		tpCam.update();
	}

	/**
	 * Since this is a GameState, initSystem() and initGame() are not called
	 * automatically. These should probably be called from this class'
	 * constructor.
	 */
	protected void initGame() {
		
		double totalStart = System.currentTimeMillis();
		display.setTitle("Warehouse Trainer");

		pManager = new BasicPassManager();

		// create the root node of the scene graph
		rootNode = new Node("scene graph root node");

		// create a root node for HUD objects
		hudNode = new Node("HUD root node");

		addToLoadingProgress(5, "Building HUD...");

		// create the HUDs
		// minimapHUD = new MinimapHUD(this);
		
		double start = System.currentTimeMillis();
		debugHUD = new DebugHUD();
		autoHUD = new AutoCompletionHUD();
		messageBox = new MessageBox(font);
		infoBar = new InformationBar(this, font);
		pauseDisplay = new PauseDisplay(font);

		// Add rootNode to the pass manager to be rendered
		rootPass = new RenderPass();
		rootPass.add(rootNode);
		pManager.add(rootPass);

		// Add the HUD stuff to the pass manager to be rendered
		// Note: minimap is currently handled separately
		hudPass = new RenderPass();
		hudPass.add(hudNode);

		hudNode.attachChild(messageBox);
		hudNode.attachChild(infoBar);
		if (useVocollect) {
			// Start the VocollectHandler thread
			vh = new VocollectHandler(messageBox);
			vh.start();
			messageBox.setText("Vocollect System",
					"Waiting for Vocollect connection...");
		} else {
			messageBox
					.setText("Welcome", "Feel free to explore the warehouse.");
		}

		if (DEBUG_MODE) {
			hudNode.attachChild(debugHUD);
		}
		
		hudNode.attachChild(autoHUD);

		pManager.add(hudPass);
		double total = System.currentTimeMillis() - start;
		System.out.println("It took "+total/1000+" seconds to build the HUD.");

		// Create a ZBuffer to display pixels closest to the camera above
		// farther ones
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		rootNode.setRenderState(buf);

		// create sharedNode manager to cache models and improve performance
		sharedMeshManager = new SharedMeshManager();

		start = System.currentTimeMillis();
		// set up sounds
		sounds.initGameStuff();
		System.out.println("It took "+(System.currentTimeMillis()-start)/1000+" seconds to build sounds.");

		scoringTimer = new ScoringTimer();
		score = new Score(scoringTimer);
		infoBar.setScoringTimer(scoringTimer);

		// build the autonomous characters.
		start = System.currentTimeMillis();
		buildBoundingBoxes();
		buildAutoCharacters();
		System.out.println("It took "+(System.currentTimeMillis()-start)/1000+" seconds to build autonomous characters.");
		
		// start the game
		start = System.currentTimeMillis();
		makeScene();
		System.out.println("It took "+(System.currentTimeMillis()-start)/1000+" seconds to make the scene.");
		paused = false;

		if (showLoadingScreen) {
			loadingScreen.dispose();
		}
		
		System.out.println("It took "+(System.currentTimeMillis()-totalStart)/1000+" seconds to load the game.");
		//System.exit(1);

		// grid = new Grid(rootNode, this, showGrid);
	}

	private void buildBoundingBoxes() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://joseph.cedarville.edu:3306/vwburr";
			// String url = "jdbc:mysql://localhost:3306/vwburr";
			String query = "select * from BOUNDINGBOXES ORDER BY ID desc;";
			String query2 = "select count(id) from DPallet";
			ResultSet result = DatabaseHandler.execute(query);
			ResultSet rs2 = DatabaseHandler.execute(query2);
			rs2.next();
			int boxCount = rs2.getInt("count(id)");
			
			
			// I simply want to know how many characters I need to make.
			result.first();
			int numBoxes = result.getInt("ID");
			boundingBoxes = new BoundingBox2D[numBoxes + boxCount];
			
			
			
			// this particular loop just wants to get all of the very static things (the rows of racks, mostly)
			// these things shouldn't move, so that's why we put a box around the rows, instead of having to get
			// where the thing
			result.beforeFirst();
			for (int i = 0; result.next(); i++){
				float leftX = result.getFloat("leftX");
				float rightX = result.getFloat("rightX");
				float lowerZ = result.getFloat("lowerZ");
				float upperZ = result.getFloat("upperZ");
				
				boundingBoxes[i] = new BoundingBox2D(leftX, rightX, lowerZ, upperZ);
				/*boundingBoxes[i].setLeftX(leftX);
				boundingBoxes[i].setRightX(rightX);
				boundingBoxes[i].setLowerZ(lowerZ);
				boundingBoxes[i].setUpperZ(upperZ);*/
			}
			
			// now I want to get all of the pallets from the DPallet table on the db because I need to have a bounding box around those
			// as well...woot.
			
			// so....this portion of this code specifically wants to get all of the boxes/pallets that are laying around and 
			// get those into the collision list.
			String query3 = "select * from DPallet";
			ResultSet rs3 = DatabaseHandler.execute(query3);
			
			for (int i = numBoxes; i < boxCount+numBoxes; i++){
				rs3.next();
				float x = rs3.getFloat("X_Location");
				float z = rs3.getFloat("Z_Location");
				boundingBoxes[i] = new BoundingBox2D(rs3.getInt("id"), x, z);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	protected void reinit() {
	}

	public void cleanup() {
		DatabaseHandler.close();
		sounds.cleanup();
		if(recording)recorder.cleanup();
		if (useVocollect) {
			VocollectHandler.close();
		}
	}

	private void buildAutoCharacters() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://joseph.cedarville.edu:3306/vwburr";
			// String url = "jdbc:mysql://localhost:3306/vwburr";
			
			//Statement stmt = con.createStatement();

			//String query = "select count(id) from AutoPickJobs";
			//stmt.execute(query);
			//ResultSet result = stmt.getResultSet();
			
			// I simply want to know how many characters I need to make.
			//result.next();
			//int numPossibleCharacters = result.getInt("count(id)");
			int characterLimit = 7;
			Scanner br = new Scanner(new File("numcharacters.cfg"));
			if (br != null){
				characterLimit = br.nextInt();
				// check to make sure negative numbers are no good...
				if (characterLimit < 0){
					characterLimit = 0;
				}
			}
			
			characters = new Npc[characterLimit];
			
			
			// do it.

			/*
			 * so. here's the deal. This simply looks at the characters.length,
			 * which is how many characters I want to make (it also happens to
			 * be how many paths I want to create. Then, we look to see how many
			 * items are in each path (query2). From there, we simply look
			 * through that list and get the X and Z(Y) locations that we need.
			 * We put those into an array list, and then get them into the
			 * people's information through the constructor.
			 */
			for (int i = 0; i < characters.length; i++) {
				ArrayList<Coordinate> ll = new ArrayList<Coordinate>();
				String query2 = "select * from AutoPickCoords where AutoPickJobID = "
						+ "ANY(select ID from AutoPickJobs where AutoPickJobID = "
						+ (i + 1) + ");";
				ResultSet result2 = DatabaseHandler.execute(query2);

				float x, z;
				while (result2.next()) {
					x = result2.getFloat("XCoord");
					z = result2.getFloat("YCoord");
					Coordinate cd = new Coordinate(x, z);
					ll.add(cd);
				}
				characters[i] = new Npc(ll.get(0).getX(), ll.get(0).getZ(),
						Character.PLAYER_LOC + "Guy.md5mesh", "Character" + i,
						Character.STANDING_ANIM[Character.NAME_INDX],
						Character.STANDING_ANIM[Character.FILE_INDX],
						Controller.RT_WRAP, Vector3f.UNIT_X, Vector3f.UNIT_Z,
						code.model.AnimatedModel.DEFAULT_UP,
						new RandomPerson(), ll);
				rootNode.attachChild(characters[i]);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void buildPlayer() {

		addToLoadingProgress(5, "Creating Player...");

		playerNode = new Player(this);
		rootNode.attachChild(playerNode);

		playerNode.updateGeometricState(0, true);

		// attach a third person camera to the player
		chaseCam = new WarehouseChaseCam(tpCam, playerNode, this);
	}

	private void buildEnvironment() {

		// build the warehouse and attach to scene graph
		world = new WarehouseWorld(this);

		if (infoIconsEnabled) {
			// build and place the info icons in the warehouse
			infoIconManagerNode = new InfoIconManager(this);
			rootNode.attachChild(infoIconManagerNode);
		}
	}

	private void buildLighting()
	{
		//openGL limits lights to eight
		LightState lightState = display.getRenderer().createLightState();
	
		lightState.detachAll();
		
		lightState.attach(createPointLight(4.93f,-3.65f));
		lightState.attach(createPointLight(4.93f,-33.036f));
		lightState.attach(createPointLight(18.065f,-44.32f));
		lightState.attach(createPointLight(18.065f,-15.561f));
		lightState.attach(createPointLight(45.205f,-3.473f));
		lightState.attach(createPointLight(45.205f,-19.784f));
		lightState.attach(createPointLight(45.205f,-40.178f));
		
		lightState.setEnabled(true);
		
		rootNode.setRenderState(lightState);
	}

	private PointLight createPointLight(float x,float z)
	{
		PointLight light = new PointLight();
		light.setDiffuse(mainLight);
		light.setAmbient(mainLight);
		light.setSpecular(mainLight);
		
		light.setAttenuate(true);
		light.setShadowCaster(false);
		light.setLinear(.26f);
	
		light.setLocation(new Vector3f(x, 4.583f, z));
		light.setEnabled(true);
		
		return light;
	}

	private void makeScene() {

		// Add player
		buildPlayer();

		// Add lights
		buildLighting();
		// Add warehouse and objects to the world
		buildEnvironment();
		
		FogState fs = display.getRenderer().createFogState();
		
		fs.setDensity(0.3f);
		fs.setColor(ColorRGBA.lightGray);
		
		fs.setStart(farPlane-2);
		fs.setEnd(farPlane+1);
		
		fs.setDensityFunction(FogState.DensityFunction.Linear);
		fs.setQuality(FogState.Quality.PerPixel);
		
		rootNode.setRenderState(fs);
		
		rootNode.updateGeometricState(0.0f, true);
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
	}

	/**
	 * Ends the game. Displays the final score before allowing the player to
	 * exit.
	 */
	public void showFinalScoreDisplay() {
		finalScoreDisplay = new ScoreDisplay(font, this);
		hudNode.attachChild(finalScoreDisplay);
		paused = true;
		gameComplete = true;
	}

	public int loadingProgress = 0;
	/**
	 * During system and game initialization, when the loading window is
	 * displayed, the progress bar can be updated to show the user some idea of
	 * when the game will be ready to be played.
	 * 
	 * @param valueToAdd
	 *            a percentage value to add to the existing percentage indicated
	 *            by the progress bar on the loading window
	 */
	public void addToLoadingProgress(int valueToAdd) {
		if (loadingScreen != null) {
			loadingScreen.addProgress(valueToAdd);
			loadingProgress += valueToAdd;
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}
	
	public int getLoadingPercent(){
		return loadingProgress;
	}

	/**
	 * During system and game initialization, when the loading window is
	 * displayed, the progress bar can be updated to show the user some idea of
	 * when the game will be ready to be played.
	 * 
	 * @param valueToAdd
	 *            a percentage value to add to the existing percentage indicated
	 *            by the progress bar on the loading window
	 * @param newMessage
	 *            a descriptive string telling the user what is currently
	 *            happening to get the game ready (what is being initialized
	 *            now)
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
	
	public BoundingBox2D[] get2DCollidables(){
		return boundingBoxes;
	}
	
	public static BoundingBox2D getBoundingBoxByID(int id){
		for(int i=0; i<boundingBoxes.length; i++){
			if(boundingBoxes[i].getID() == id) return boundingBoxes[i];
		}
		return null;
	}
	
	public static void setBoundingBoxByID(int id, BoundingBox2D b){
		for(int i=0; i<boundingBoxes.length; i++){
			if(boundingBoxes[i].getID() == id){
				boundingBoxes[i] = b;
				return;
			}
		}
	}
	
	public DebugHUD getDebugHUD(){
		return debugHUD;
	}
	
	public static AutoCompletionHUD getAutoHUD(){
		return autoHUD;
	}

	public float getNearPlane()
	{
		return nearPlane;
	}
	
	public float getFarPlane()
	{
		return farPlane;
	}
	
	public float getAngleOfView()
	{
		return angleOfView;
	}
}
