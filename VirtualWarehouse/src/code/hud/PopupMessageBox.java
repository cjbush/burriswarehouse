package code.hud;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * Displays a message to the user in a pop-up message box centered on the screen.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class PopupMessageBox extends HUD {

	public static final float QUAD_HEIGHT = 300;
	public static final float QUAD_WIDTH = 200;
	
	private BitmapText bodyText;
	
	public PopupMessageBox(String text, BitmapFont f) {
		//create a quad for showing the text on
		Quad messageBoxQuad = new Quad("messageBoxQuad", QUAD_WIDTH, QUAD_HEIGHT);
		messageBoxQuad.setZOrder(2);
		messageBoxQuad.setDefaultColor(ColorRGBA.gray.clone());
		setTexture(messageBoxQuad, "data/hud/infoBox.png");
		//TODO:different texture image? better text placement?
		this.attachChild(messageBoxQuad);
		
		//create the heading and body text

		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
		
		//set some defaults for the text
		bodyText = new BitmapText(font, false);
		bodyText.setBox(new Rectangle(-90, 62, QUAD_WIDTH, 35));
		bodyText.setSize(14);
		bodyText.setDefaultColor(ColorRGBA.yellow.clone());
		bodyText.setText(text);
		bodyText.setZOrder(1);
		bodyText.update();
        this.attachChild(bodyText);
        
        //TODO: animation when the box appears?
        
        this.setLocalTranslation(getDisplayWidth()/2, getDisplayHeight()/2, 0);
        this.updateRenderState();
	}
	
}
