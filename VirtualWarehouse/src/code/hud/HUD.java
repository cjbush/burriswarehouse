package code.hud;

import com.jme.image.Texture;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public abstract class HUD extends Node {
	
	private int displayHeight;
	private int displayWidth;
	
	public HUD() {
		
		// Get some info about the display
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		displayHeight = display.getHeight();
		displayWidth = display.getWidth();
		
		// Set defaults
		setCullHint(CullHint.Never);
		setRenderQueueMode(Renderer.QUEUE_ORTHO);
		setLightCombineMode(Spatial.LightCombineMode.Off);
		this.updateRenderState();
	}
	
	protected int getDisplayHeight() {
		return displayHeight;
	}
	
	protected int getDisplayWidth() {
		return displayWidth;
	}
	
	protected void setTexture(Quad button, String imagePath) {
		// create the texture state to handle the texture
	    final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    // load the image as a texture (the image should be placed in the same directory as this class)
	    final Texture texture = TextureManager.loadTexture(
	            getClass().getClassLoader().getResource(imagePath),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear,
	            1.0f,
	            true);
	    // set the texture for this texture state
	    ts.setTexture(texture);
	    // activate the texture state
	    ts.setEnabled(true);
	    button.setRenderState(ts);
	    
	    // to handle texture transparency:
	    // create a blend state
	    final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    // activate blending
	    bs.setBlendEnabled(true);
	    // set the source function
	    bs.setSourceFunctionAlpha(BlendState.SourceFunction.SourceAlpha);
	    // set the destination function
	    bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.OneMinusSourceAlpha);
	    // set the blend equation between source and destination
	    bs.setBlendEquation(BlendState.BlendEquation.Add);
	    bs.setTestEnabled(false);
	    // activate the blend state
	    bs.setEnabled(true);
	    // assign the blender state to the quad
	    button.setRenderState(bs);
	 
	}
}
