package code.hud;

import com.jme.renderer.ColorRGBA;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.scene.TimedLifeController;

/**
 * Displays a message alerting the user that they made an error. 
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class PickErrorDisplay extends HUD {

	public BitmapText pausedHeading;
	
	public PickErrorDisplay(BitmapFont f) {
		
		//create the text
		
		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//set some defaults for the text
		pausedHeading = new BitmapText(font, false);
		pausedHeading.setSize(32);
		pausedHeading.setDefaultColor(ColorRGBA.white.clone());
		pausedHeading.setText("Picking Error");
		pausedHeading.setZOrder(1);
		pausedHeading.update();
		pausedHeading.setLocalTranslation(getDisplayWidth()/2-pausedHeading.getLineWidth()/2, getDisplayHeight()/2, 0);
        this.attachChild(pausedHeading);
        
        TimedLifeController fader = new TimedLifeController( 4f ) {

            public void updatePercentage( float percentComplete ) {
                pausedHeading.setLocalScale(1-percentComplete);
            }
        };

        addController( fader );
        fader.setActive( true );
        
        this.updateGeometricState(0, true);
        this.updateRenderState();
	}
	
}
