package code.hud;

import code.app.VirtualWarehouse;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.TextureRenderer;
import com.jme.scene.CameraNode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 * 
 * Update
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 * We are no longer using this.  But still here in case.
 *
 */
public class MinimapHUD extends HUD {

	//specifies the time between renders of the minimap to help performance
	//set to -1 to render the minimap every frame (highest quality)
	private static final float TIME_BETWEEN_RENDERS = 0.3f;
	
	public static final String TOGGLE_MINIMAP_COMMAND = "toggle_minimap";
	public static final int TOGGLE_MINIMAP_KEY = KeyInput.KEY_M;
	
	private int cameraHeight = 15;
	
	private TextureRenderer tRenderer;
	private VirtualWarehouse warehouseGame;
	private Texture2D minimapTexture;
	private CameraNode minimapCamNode;
	
	private DisplaySystem display;
	
	private float lastMinimapRender = 1;
	
	private boolean showMinimap = true;
	
	public MinimapHUD(VirtualWarehouse vw) {
		
		warehouseGame = vw;
		
		display = DisplaySystem.getDisplaySystem();
		
		tRenderer = display.createTextureRenderer(256, 256, TextureRenderer.Target.Texture2D);
		
		//create a quad for showing the minimap on and position it on the screen
		Quad minimapQuad = new Quad("minimapQuad", 128, 128);
		minimapQuad.setLocalTranslation(80, display.getHeight()-80, 0);
		minimapQuad.setZOrder(1);
		this.attachChild(minimapQuad);
		
		Quad minimapBorderQuad = new Quad("minimapBorderQuad", 132, 132);
		minimapBorderQuad.setLocalTranslation(80, display.getHeight()-80, 0);
		minimapBorderQuad.setZOrder(2);
		minimapBorderQuad.setDefaultColor(ColorRGBA.white);
		this.attachChild(minimapBorderQuad);
		
		//set up the camera that is rendering the minimap
		minimapCamNode = new CameraNode("Minimap Camera Node", tRenderer.getCamera());

		minimapCamNode.setLocalTranslation(new Vector3f(0, cameraHeight, 0));
		minimapCamNode.getCamera().setFrustumPerspective(vw.getAngleOfView(), (float)display.getWidth() / (float)display.getHeight(), vw.getNearPlane(), vw.getFarPlane());
		minimapCamNode.getCamera().update();

		minimapCamNode.lookAt(new Vector3f(4, 0, 4), Vector3f.UNIT_Y);
        minimapCamNode.updateGeometricState(0, true);
		
        warehouseGame.getRootNode().attachChild(minimapCamNode);
        
		//create a texture object and texture state to render the scene to
		minimapTexture = new Texture2D();
		minimapTexture.setRenderToTextureType(Texture.RenderToTextureType.RGBA);
		
		TextureState minimapTextureState = display.getRenderer().createTextureState();
		minimapTextureState.setTexture(minimapTexture);
		minimapTextureState.setEnabled(true);
        
		//set up the texture renderer
		tRenderer.setBackgroundColor(ColorRGBA.gray.clone());
		tRenderer.setupTexture(minimapTexture);
        
		minimapQuad.setRenderState(minimapTextureState);
		minimapQuad.updateRenderState();
		
		this.updateGeometricState(0.0f, true);
        this.updateRenderState();
        
        bindKeys();
	}
	
	public void render() {
		
		if (showMinimap)
		{
			lastMinimapRender += warehouseGame.getTimePerFrame();
			
			//don't update the minimap every frame for performance reasons
			if (lastMinimapRender > TIME_BETWEEN_RENDERS) {
				
				minimapCamNode.getLocalTranslation().set(warehouseGame.getPlayerNode().getWorldTranslation().getX(), cameraHeight, warehouseGame.getPlayerNode().getWorldTranslation().getZ());
				
				tRenderer.render(warehouseGame.getRootNode(), minimapTexture);
				
				lastMinimapRender = 0;
			}
			
			//display the minimap every frame regardless of whether it is freshly
			//rendered or not
			display.getRenderer().draw(this);
		}
		
	}
	
	/**
	 * Checks for a key press to hide the minimap.
	 */
	public void update() {
		//if m was pressed, toggle the minimap on/off
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(TOGGLE_MINIMAP_COMMAND, false)) {
			showMinimap = !showMinimap;
		}
	}
	
	private void bindKeys() {
		KeyBindingManager.getKeyBindingManager().set(TOGGLE_MINIMAP_COMMAND, TOGGLE_MINIMAP_KEY);
	}
}
