package code.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import code.app.WarehouseTrainer;
import code.app.WarehouseTrainer.Mode;
import code.component.Score;
import code.gui.TransitionFadeOut.GoToState;

import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.bui.AllDialogsTest;
import com.jmex.bui.BButton;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.BDialogBox;
import com.jmex.bui.BInputBox;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTitleBar;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.bui.UserResponse;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.ComponentListener;
import com.jmex.bui.event.DialogListener;
import com.jmex.bui.headlessWindows.DialogBoxUtil;
import com.jmex.bui.headlessWindows.InputBoxUtil;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.listener.CollapsingWindowListener;
import com.jmex.bui.util.Point;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.TransitionGameState;
import com.jmex.scene.TimedLifeController;

/**
 * Provides a box for the player to enter their name for the high score list.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class EnterNameState extends MenuState {
	
	private WarehouseTrainer app;
	private String name;
	
	public EnterNameState(WarehouseTrainer wt, final Score score) {
		super("High Score Menu");
		
		app = wt;
		final EnterNameState thisState = this;
		
		MouseInput.get().setCursorVisible(true);
		
		BuiSystem.init(new PolledRootNode(Timer.getTimer(), null), "/data/gbuiStyle/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        
        DialogListener responseListener = new DialogListener() {
            public void responseAvailable(UserResponse response, BComponent source) {
            	if (source instanceof BInputBox) {
                    name = ((BInputBox) source).getInputText();
                    score.setName(name);
                    score.saveScoreData();
                    
                    //app.startMainMenuState();
                    thisState.setActive(false);
                    GameStateManager.getInstance().detachChild(thisState);
                }
            }
        };
        
        BDialogBox box = InputBoxUtil.createBasicInputBox("NewHighScore", "You set a new high score!\nPlease enter your name:");
        box.setDialogListener(responseListener);
        box.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2-box.getWidth()/2, 0);
        
        box.setModal(true);
	}
	
	public String getName() {
		return name;
	}
	
	public void setAlpha(float alpha) {
	}

}
