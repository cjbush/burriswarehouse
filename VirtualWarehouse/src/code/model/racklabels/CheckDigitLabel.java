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
public class CheckDigitLabel extends Label {

	public static final float LABEL_WIDTH = 0.13f;
	public static final float LABEL_HEIGHT = 0.07f;
	public static final float TEXT_SIZE = 0.067f;
	
	public static final String BACKGROUND_QUAD_NAME = "checkDigitBackgroundQuad";
	
	public CheckDigitLabel(String labelText, BitmapFont f, SharedMeshManager smm) {
		super(labelText, f, smm, LABEL_WIDTH, LABEL_HEIGHT, TEXT_SIZE, BACKGROUND_QUAD_NAME);
	}
	
}
