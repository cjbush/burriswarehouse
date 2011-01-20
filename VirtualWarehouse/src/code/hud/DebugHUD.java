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
 */
public class DebugHUD extends HUD {

	private Text2D fps;
	private Text2D location;
	
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
	
}
