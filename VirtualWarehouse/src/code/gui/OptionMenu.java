package code.gui;

import com.jmex.bui.BButton;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.layout.GroupLayout;

import code.app.WarehouseTrainer;


public class OptionMenu extends MenuState {
	private BButton backButton;
	private BWindow window;
	private WarehouseTrainer app;
	public OptionMenu(WarehouseTrainer wt) {
		super("Options");
		app = wt;
		window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
		window.setStyleClass("champion");
        BuiSystem.addWindow(window);
        
        window.setSize(180, 280);
        window.setLocation(25, 25);
        
        backButton = new BButton("Back");
        backButton.setPreferredSize(100, 70);
        
        
        window.add(backButton);
        TransitionFadeIn t = new TransitionFadeIn(0.1f, this, app);
        rootNode.addController(t);
		
	}

	@Override
	public void setAlpha(float alpha) {
		window.setAlpha(alpha);
		backButton.setAlpha(alpha);
	}

	
}
