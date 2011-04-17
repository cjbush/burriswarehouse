package code.hud;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;

/**
 * A HUD intended to be displayed in Debug Mode, for displaying
 * information life FPS.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * Small changes, such as seeing the player's location
 */
public class DebugHUD extends HUD {

	private Text2D fps;
	private Text2D location;
	//private Text2D bblocation;
	private Text2D debug;
	private Text2D auto;
	private Text2D autocounter;
	
	public DebugHUD() {
		
		// Show FPS
        Font2D my2DFont = new Font2D();
        fps = my2DFont.createText("FPS", 10, 0);
        fps.setLocalTranslation(0, 0, 0);
        this.attachChild(fps);
        
        
        //Show location
        location = my2DFont.createText("Loc: ", 10, 0);
        location.setLocalTranslation(0, 20, 0);
        this.attachChild(location);

        debug = my2DFont.createText("Debug: No Message.", 10, 0);
        debug.setLocalTranslation(0,40,0);
        this.attachChild(debug);
        
        auto = my2DFont.createText("Autocomplete: No",10,0);
        auto.setLocalTranslation(0,60,0);
        this.attachChild(auto);
        
        autocounter = my2DFont.createText("Waypoint: ", 10, 0);
        autocounter.setLocalTranslation(0,80,0);
        this.attachChild(autocounter);
        
        this.setLocalTranslation(10, 100, 0);
        
        
        this.updateGeometricState(0, false);
        this.updateRenderState();
	}
	
	public void setFPS(Float num) {
		fps.setText(num.toString());
	}
	
	public void setPlayerLocation(Float x, Float y, Float z)
	{
		location.setText("Loc: " + x.toString() + ", " + y.toString() + ", " + z.toString());
	}
	
	public void setDebugMessage(String msg){
		debug.setText("Debug: "+msg);
	}
	
	public void setAutoMessage(boolean enabled){
		if(enabled) auto.setText("Autocomplete: Yes");
		else auto.setText("Autocomplete: No");
	}
	
	public void setAutoCount(int count){
		autocounter.setText("Waypoint: "+count);
	}
}
