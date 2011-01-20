package code.hud;

import com.jme.renderer.ColorRGBA;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

/**
 * Displays text that should be shown when the game is paused.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class PauseDisplay extends HUD {

	public PauseDisplay(BitmapFont f) {
		
		//create the text
		
		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//set some defaults for the text
		BitmapText pausedHeading = new BitmapText(font, false);
		pausedHeading.setSize(32);
		pausedHeading.setDefaultColor(ColorRGBA.white.clone());
		pausedHeading.setText("Paused");
		pausedHeading.setZOrder(1);
		pausedHeading.update();
		pausedHeading.setLocalTranslation(getDisplayWidth()/2-pausedHeading.getLineWidth()/2, getDisplayHeight()/2, 0);
        this.attachChild(pausedHeading);
        
        BitmapText pausedSubText = new BitmapText(font, false);
        pausedSubText.setSize(22);
        pausedSubText.setDefaultColor(ColorRGBA.white.clone());
        pausedSubText.setText("Press Escape to Exit Or Spacebar to Continue");
        pausedSubText.setZOrder(1);
        pausedSubText.update();
        pausedSubText.setLocalTranslation(getDisplayWidth()/2-pausedSubText.getLineWidth()/2, getDisplayHeight()/2-pausedHeading.getLineHeight(), 0);
        this.attachChild(pausedSubText);
        
        this.updateGeometricState(0, true);
        this.updateRenderState();
	}
	
}
