package code.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import code.app.WarehouseTrainer;
import code.component.Score;
import code.gui.TransitionFadeOut.GoToState;
import com.jme.input.MouseInput;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.bui.BButton;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;

/**
 * Shows the high score menu.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class HighScoreMenu extends MenuState {

	private BWindow headingWindow;
	private BWindow highScoreTable;
	private BButton backButton;
	
	private WarehouseTrainer app;
	
	public HighScoreMenu(WarehouseTrainer wt) {
		super("High Score Menu");
		
		app = wt;
		
		MouseInput.get().setCursorVisible(true);
		
		BuiSystem.init(new PolledRootNode(Timer.getTimer(), null), "/data/gbuiStyle/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        
        //set up a heading label
        headingWindow = new BWindow(BuiSystem.getStyle(), GroupLayout.makeHStretch());
        headingWindow.setStyleClass("champion");
        BuiSystem.addWindow(headingWindow);
        
        BLabel label = new BLabel("High Scores");
        headingWindow.add(label);
        
        headingWindow.setSize(100, 10);
        headingWindow.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2-headingWindow.getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()-50);
        
        //set up the high score table
        highScoreTable = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        highScoreTable.setStyleClass("champion");
        BuiSystem.addWindow(highScoreTable);
        
        BContainer container = new BContainer(GroupLayout.makeHStretch());
        highScoreTable.add(container);
        container.add(new BLabel("Name"));
        container.add(new BLabel("Time"));
        container.add(new BLabel("Errors"));
        
        //read in scores
        File file = new File(Score.HIGH_SCORE_FILE);
        if (file.exists())
        {
        	try {
				Scanner sc = new Scanner(file);
				
				while (sc.hasNextLine())
				{
					String[] line = sc.nextLine().split(",");
					
					String name = line[0];
					String timeInSeconds = line[1];
					int totalMinutes = (int)(Float.valueOf(timeInSeconds) / 60);
					int totalSeconds = (int)(Float.valueOf(timeInSeconds) % 60);
					String time = totalMinutes + ":";
					if (totalSeconds < 10)
					{
						time += "0";
					}
					time += totalSeconds;
					String errors = line[2];
					
					container = new BContainer(GroupLayout.makeHStretch());
		        	highScoreTable.add(container);
		        	container.add(new BLabel(name));
		        	container.add(new BLabel(time));
		        	container.add(new BLabel(errors));
				}
				sc.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
        
        //highScoreTable.setSize(160, 250);
        highScoreTable.setSize(200, 5*DisplaySystem.getDisplaySystem().getWidth()/12);
        //highScoreTable.setLocation(DisplaySystem.getDisplaySystem().getWidth()/2-100, DisplaySystem.getDisplaySystem().getHeight()/4);
        highScoreTable.center();
        
        //set up the buttons
        BWindow window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        window.setStyleClass("champion");
        BuiSystem.addWindow(window);
        
        window.setSize(100, 70);
        window.setLocation(25, 25);

        backButton = new BButton("Back");
        backButton.setPreferredSize(100, 70);
        
		final WarehouseTrainer app = wt;
		final MenuState highScoreState = this;
        
        backButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	TransitionFadeOut t = new TransitionFadeOut(0.1f, highScoreState, GoToState.MAIN_MENU, app);
                rootNode.addController(t);
            }
        });
        
        window.add(backButton);
        
        TransitionFadeIn t = new TransitionFadeIn(0.1f, this, app);
        rootNode.addController(t);
	}
	
	public void setAlpha(float alpha) {
		headingWindow.setAlpha(alpha);
		backButton.setAlpha(alpha);
		highScoreTable.setAlpha(alpha);
	}

}
