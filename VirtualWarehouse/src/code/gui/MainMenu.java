package code.gui;

import code.app.WarehouseTrainer;
import code.app.WarehouseTrainer.Mode;
import code.gui.TransitionFadeOut.GoToState;

import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.util.Timer;
import com.jmex.bui.BButton;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.bui.background.BBackground;
import com.jmex.bui.background.BlankBackground;
import com.jmex.bui.background.TintedBackground;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.util.Point;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.TransitionGameState;
import com.jmex.scene.TimedLifeController;

/**
 * 
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 */
public class MainMenu extends MenuState {

	private BWindow window;
	
	//private WarehouseTrainer app;
	
	public MainMenu(WarehouseTrainer wt) {
		super("Main Menu");
		
		//app = wt;
		
		MouseInput.get().setCursorVisible(true);
		
		int width = WarehouseTrainer.getWidth();
		int height = WarehouseTrainer.getHeight();
		
		BuiSystem.init(new PolledRootNode(Timer.getTimer(), null), "/data/gbuiStyle/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        
        window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        window.setStyleClass("champion");
        //window.setStyleClass("test");
        window.setBackground(0, new TintedBackground(new ColorRGBA(255,255,255,255)));
        BuiSystem.addWindow(window);
        
        window.setSize(180, 280);
        //window.setSize(width, height);
        window.center();

        BButton freeExploreButton = new BButton("Start Free Exploration Mode");
        //freeExploreButton.setPreferredSize(100, 70);
        freeExploreButton.setSize(100, 70);
        
        BButton trainingModeButton = new BButton("Start Training Mode");
        trainingModeButton.setPreferredSize(100, 70);
        
        BButton highScoresButton = new BButton("High Scores");
        highScoresButton.setPreferredSize(100, 70);
        
        BButton optionsButton = new BButton("Options");
        optionsButton.setPreferredSize(100, 70);
        
        BButton quitButton = new BButton("Exit");
        quitButton.setPreferredSize(100, 70);
        
		final WarehouseTrainer app = wt;
		final MenuState mainMenuState = this;
        
        freeExploreButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TransitionFadeOut t = new TransitionFadeOut(0.1f, mainMenuState, GoToState.IN_GAME,
                		Mode.FREE_EXPLORATION, app);
                rootNode.addController(t);
            }
        });
        
        trainingModeButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	TransitionFadeOut t = new TransitionFadeOut(0.1f, mainMenuState, GoToState.IN_GAME,
                		Mode.TRAINING, app);
                rootNode.addController(t);
            }
        });
        
       highScoresButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TransitionFadeOut t = new TransitionFadeOut(0.1f, mainMenuState, GoToState.HIGH_SCORES, app);
                rootNode.addController(t);
            }
        });
        
        optionsButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TransitionFadeOut t = new TransitionFadeOut(0.1f, mainMenuState, GoToState.OPTIONS, app);
                rootNode.addController(t);
            }
        });
        
        quitButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                app.setFinished();
            }
        });
        
        window.add(freeExploreButton);
        window.add(trainingModeButton);
        //window.add(highScoresButton);
        window.add(optionsButton);
        window.add(quitButton);
        
        TransitionFadeIn t = new TransitionFadeIn(0.1f, this, app);
        rootNode.addController(t);
	}
	
	public void setAlpha(float alpha) {
		window.setAlpha(alpha);
	}

}
