package code.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.jme.input.MouseInput;
import com.jme.util.Timer;
import com.jmex.bui.BButton;
import com.jmex.bui.BInputBox;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;

import code.app.WarehouseTrainer;
import code.gui.TransitionFadeOut.GoToState;
import code.model.ModelLoader;

/**
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Gives the user some basic run time options
 * 
 */

public class OptionMenu extends MenuState {
	
	private BWindow mainWindow;
	private BWindow backWindow;
	
	BTextArea area;
	BTextField text;
	BButton backButton;
	BButton saveButton;
	BButton rebuildModelCache;
	private WarehouseTrainer app;
	public OptionMenu(WarehouseTrainer wt) {
		super("Options");
		app = wt;
		MouseInput.get().setCursorVisible(true);
		
		BuiSystem.init(new PolledRootNode(Timer.getTimer(), null), "/data/gbuiStyle/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        
		mainWindow = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
		mainWindow.setStyleClass("champion");
        BuiSystem.addWindow(mainWindow);
        
        mainWindow.setSize(180, 180);
        mainWindow.center();
        
        area = new BTextArea ("Please enter a number between 0 and 7 for the number of characters");
        area.setPreferredSize(50, 30);
        
        text = new BTextField();
        text.setLocation(100, 25);
        
		
        
        saveButton = new BButton ("Save");
        saveButton.setPreferredSize(100, 70);
        saveButton.setLocation(25, 25);
        
        rebuildModelCache = new BButton("Rebuild Model Cache");
        rebuildModelCache.setPreferredSize(100, 70);
        rebuildModelCache.setLocation(50, 25);
        
        mainWindow.add(area);
        mainWindow.add(text);
        mainWindow.add(saveButton);
        mainWindow.add(rebuildModelCache);
        
        backWindow = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
		backWindow.setStyleClass("champion");
        BuiSystem.addWindow(backWindow);
        
        backWindow.setSize(100, 70);
        backWindow.setLocation(25, 25);
        
        backButton = new BButton("Back");
        backButton.setPreferredSize(100, 70);
        backButton.setLocation(25, 25);
        
        rebuildModelCache.addListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		ModelLoader.rebuildCache();
        	}
        });
        
        saveButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	// fill in with some "write to a file code" here...could be interesting...
            	// perhaps I should think about making the "file" on the database, just so I know
            	// that I have the "same" location for the "file"...but maybe not...
            	BufferedWriter bw;
            	String num;
            	try {
					bw = new BufferedWriter (new FileWriter("numcharacters.cfg"));
					num = text.getText();
					bw.write(num);
					bw.close();
					
					area.setText("number of characters saved as: " + num);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
            }
        });
        
        
        final MenuState optionsState = this;
        final WarehouseTrainer app = wt;
        
        backButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	String woot = text.getText();
            	if (text.getText().equals("")){
            		BufferedWriter bw;
                	
                	try {
    					bw = new BufferedWriter (new FileWriter("numcharacters.cfg"));
    					
    					String characterLimit = "0";
    					
    					bw.write(characterLimit);
    					bw.close();
    					
    					
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
            	}
            	TransitionFadeOut t = new TransitionFadeOut(0.1f, optionsState, GoToState.MAIN_MENU, app);
                rootNode.addController(t);
            }
        });
        backWindow.add(backButton);
       
        

        TransitionFadeIn t = new TransitionFadeIn(0.1f, this, app);
        rootNode.addController(t);
		
	}

	@Override
	public void setAlpha(float alpha) {
		mainWindow.setAlpha(alpha);
		backWindow.setAlpha(alpha);
		text.setAlpha(alpha);
		saveButton.setAlpha(alpha);
		backButton.setAlpha(alpha);
	}
}
