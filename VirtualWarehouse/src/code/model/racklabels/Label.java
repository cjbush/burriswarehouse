package code.model.racklabels;

import code.model.SharedMeshManager;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.BitmapFont.Align;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public abstract class Label extends Node {
	
	public Label(String labelText, BitmapFont f, SharedMeshManager smm, float width, float height, float textSize, String name) {
		
		//attach a quad for the label background
		//use SharedMeshes to reduce memory use
		if (smm != null)
		{
			if (smm.getQuad(name) == null)
			{
				Quad background = new Quad(name, width, height);
				background.setDefaultColor(ColorRGBA.white.clone());
				background.setZOrder(2);
				smm.cacheQuad(name, background);
			}
			
			SharedMesh quadMesh = smm.getQuad(name);
			this.attachChild(quadMesh);
		}
		else
		{
			Quad background = new Quad(name, width, height);
			background.setDefaultColor(ColorRGBA.white.clone());
			background.setZOrder(2);
			this.attachChild(background);
		}
		
		//get the font to be used - hopefully one is passed in to
		//reduce memory use so a new one is not loaded for each label
		BitmapFont font = null;
		if (f != null)
		{
			font = f;
		}
		else
		{
			font = BitmapFontLoader.loadDefaultFont();
		}
        
		//set some defaults for the heading text
		BitmapText text = new BitmapText(font, false);
        //text.setBox(new Rectangle(-10, -10, LABEL_WIDTH, LABEL_HEIGHT));
        text.setSize(textSize);
        text.setAlignment(Align.Center);
        text.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        text.setDefaultColor(ColorRGBA.black.clone());
        text.setText(labelText);
        //text.setZOrder(1); only seems to apply in ortho mode
        text.update();
        //flickering when quad and text have same translation
        //any beter way to fix this?
        text.setLocalTranslation(0, height/2, 0.001f);
        this.attachChild(text);
		
        this.setLightCombineMode(LightCombineMode.Off);
        
		this.updateGeometricState(0, true);
        this.updateRenderState();
	}
	
}
