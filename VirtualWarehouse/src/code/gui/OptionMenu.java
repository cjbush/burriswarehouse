package code.gui;

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


public class OptionMenu extends MenuState {
	
	private BWindow window;
	private WarehouseTrainer app;
	public OptionMenu(WarehouseTrainer wt) {
		super("Options");
		app = wt;
		MouseInput.get().setCursorVisible(true);
		
		BuiSystem.init(new PolledRootNode(Timer.getTimer(), null), "/data/gbuiStyle/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        
		window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
		window.setStyleClass("champion");
        BuiSystem.addWindow(window);
        
        window.setSize(180, 280);
        window.center();
        
        BTextArea area = new BTextArea ("Please enter a number between 0 and 7");
        area.setPreferredSize(50, 30);
        
        BTextField text = new BTextField();
        text.setLocation(100, 25);
        
        BButton saveButton = new BButton ("save");
        saveButton.setPreferredSize(100, 70);
        saveButton.setLocation(25, 25);
        
        BButton backButton = new BButton("Back");
        backButton.setPreferredSize(100, 70);
        backButton.setLocation(25, 25);
        
        final MenuState optionsState = this;
        
        backButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	// fill in with some "write to a file code" here...could be interesting...
            	// perhaps I should think about making the "file" on the database, just so I know
            	// that I have the "same" location for the "file"...but maybe not...
            }
        });
        
        backButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	TransitionFadeOut t = new TransitionFadeOut(0.1f, optionsState, GoToState.MAIN_MENU, app);
                rootNode.addController(t);
            }
        });
        
        window.add(area);
        window.add(text);
        window.add(saveButton);
        window.add(backButton);

        TransitionFadeIn t = new TransitionFadeIn(0.1f, this, app);
        rootNode.addController(t);
		
	}

	@Override
	public void setAlpha(float alpha) {
		window.setAlpha(alpha);
		
	}

	
}
