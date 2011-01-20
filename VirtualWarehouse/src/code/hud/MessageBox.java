package code.hud;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * 
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class MessageBox extends HUD {

	public static final float QUAD_HEIGHT = 170;
	public static final float QUAD_WIDTH = 175;
	//public static final float BORDER_QUAD_HEIGHT = 144;
	//public static final float BORDER_QUAD_WIDTH = 179;
	
	private BitmapText headingText;
	private BitmapText bodyText;
	
	public MessageBox(BitmapFont f) {
				
		//create a quad for showing the text on
		Quad messageBoxQuad = new Quad("messageBoxQuad", QUAD_WIDTH, QUAD_HEIGHT);
		messageBoxQuad.setZOrder(2);
		messageBoxQuad.setDefaultColor(ColorRGBA.gray.clone());
		setTexture(messageBoxQuad, "data/hud/infoBox.png");
		this.attachChild(messageBoxQuad);
		
//		//create a quad for a background
//		Quad messageBoxBorderQuad = new Quad("messageBoxBorderQuad", BORDER_QUAD_WIDTH, BORDER_QUAD_HEIGHT);
//		messageBoxBorderQuad.setZOrder(3);
//		messageBoxBorderQuad.setDefaultColor(ColorRGBA.black.clone());
//		this.attachChild(messageBoxBorderQuad);
//		
//		//create a thin quad to separate heading and body text and position it
//		Quad messageBoxSeparatorQuad = new Quad("messageBoxSeparatorQuad", BORDER_QUAD_WIDTH, 2);
//		messageBoxSeparatorQuad.setZOrder(1);
//		messageBoxSeparatorQuad.setDefaultColor(ColorRGBA.black.clone());
//		messageBoxSeparatorQuad.setLocalTranslation(0, 35, 0);
//		this.attachChild(messageBoxSeparatorQuad);
		
		this.setLocalTranslation(getDisplayWidth()-100, getDisplayHeight()-84, 0);
		
		//Text text = Text.createDefaultTextLabel("t", "This is some text");
		//this.attachChild(text);
		
		//create the heading and body text

		//get the font that's passed in or load a new one if needed
		BitmapFont font = f;
		if (font == null)
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//set some defaults for the heading text
		headingText = new BitmapText(font, false);
        headingText.setBox(new Rectangle(-80, 62, QUAD_WIDTH, 35));
        headingText.setSize(14);
        headingText.setDefaultColor(ColorRGBA.yellow.clone());
        headingText.setText("");
        headingText.setZOrder(1);
        headingText.update();
        this.attachChild(headingText);
        
        //set some defaults for the body text
        bodyText = new BitmapText(font, false);
        bodyText.setBox(new Rectangle(-80, 33, QUAD_WIDTH, QUAD_HEIGHT));
        bodyText.setSize(14);
        bodyText.setDefaultColor(ColorRGBA.yellow.clone());
        bodyText.setText("");
        bodyText.setZOrder(1);
        bodyText.update();
        this.attachChild(bodyText);
	    
		this.updateRenderState();
	}
	
	public void setText(String heading, String body) {
		headingText.setText(heading);
		headingText.update();
		
		bodyText.setText(body);
		bodyText.update();
		
		//TODO character limit?
	}
	
	public void setHeadingText(String text) {
		headingText.setText(text);
		headingText.update();
		
	}
	
	public void setBodyText(String text) {
		bodyText.setText(text);
		bodyText.update();
	}
}
