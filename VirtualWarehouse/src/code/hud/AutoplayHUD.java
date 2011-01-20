package code.hud;

import java.nio.FloatBuffer;

import com.jme.image.Texture;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class AutoplayHUD extends HUD {

	public static final float INTERFACE_HEIGHT = 116;
	public static final float INTERFACE_WIDTH = 290;
	
	public static final float BUTTON_HEIGHT = 50;
	public static final float BUTTON_WIDTH = 50;
	
	public static final float SCENES_INDICATOR_WIDTH = 35;
	public static final float SCENES_INDICATOR_HEIGHT = 20;

	private static final String PLAY_BUTTON_IMAGE = "data/hud/play.png";
	private static final String PAUSE_BUTTON_IMAGE = "data/hud/pause.jpg";
	private static final String BACK_BUTTON_IMAGE = "data/hud/back.png";
	private static final String FORWARD_BUTTON_IMAGE = "data/hud/forward.png";
	
	private Quad playButton;
	private Quad backButton;
	private Quad forwardButton;
	private Quad scenesIndicator;
	
	private boolean mouseClicked = false;
	
	SceneChooser sceneChooser;
	private boolean showScenes = false;
	
	public AutoplayHUD() {
		
		//MouseInput.get().setCursorVisible(true);
		
		//node to hold the forward, play, and back buttons
		Node controlsNode  = new Node("ControlButtonsNode");
		this.attachChild(controlsNode);
		
		//node to hold the icon that can be clicked to make the scene
		//choices appear
		Node scenesIndicatorNode = new Node("ScenesIndicatorNode");
		this.attachChild(scenesIndicatorNode);
		
		sceneChooser = new SceneChooser();
		
		//create a quad for showing the interface on
		Quad interfaceBox = new Quad("interfaceBoxQuad", INTERFACE_WIDTH, INTERFACE_HEIGHT);
		interfaceBox.setZOrder(2);
		interfaceBox.setDefaultColor(ColorRGBA.black.clone());
		interfaceBox.setLocalTranslation(0, 0, 0);
		controlsNode.attachChild(interfaceBox);
		
		//create buttons
		playButton = new Quad("playButtonQuad", BUTTON_WIDTH, BUTTON_HEIGHT);
		playButton.setZOrder(1);
		playButton.setDefaultColor(ColorRGBA.white.clone());
		playButton.setLocalTranslation(0, 0, 0);
		setTexture(playButton, PLAY_BUTTON_IMAGE);
		controlsNode.attachChild(playButton);
		
		backButton = new Quad("backButtonQuad", BUTTON_WIDTH, BUTTON_HEIGHT);
		backButton.setZOrder(1);
		backButton.setDefaultColor(ColorRGBA.white.clone());
		backButton.setLocalTranslation(-(INTERFACE_WIDTH/2)+BUTTON_WIDTH, 0, 0);
		setTexture(backButton, BACK_BUTTON_IMAGE);
		controlsNode.attachChild(backButton);
		
		forwardButton = new Quad("forwardButtonQuad", BUTTON_WIDTH, BUTTON_HEIGHT);
		forwardButton.setZOrder(1);
		forwardButton.setDefaultColor(ColorRGBA.white.clone());
		forwardButton.setLocalTranslation((INTERFACE_WIDTH/2)-BUTTON_WIDTH, 0, 0);
		setTexture(forwardButton, FORWARD_BUTTON_IMAGE);
		controlsNode.attachChild(forwardButton);
		
		//create a button for making the scene choices appear
		scenesIndicator = new Quad("scenesIndicatorQuad", SCENES_INDICATOR_WIDTH, SCENES_INDICATOR_HEIGHT);
		scenesIndicator.setZOrder(1);
		scenesIndicator.setDefaultColor(ColorRGBA.black.clone());
		scenesIndicator.setLocalTranslation(0, 0, 0);
		scenesIndicatorNode.attachChild(scenesIndicator);
		
		controlsNode.setLocalTranslation(getDisplayWidth()/2, 150, 0);
		scenesIndicatorNode.setLocalTranslation(getDisplayWidth()-10, getDisplayHeight()/2, 0);
		
		this.updateGeometricState(0.0f, true);
		this.updateRenderState();
		
	}
	
	public void update() {
		
		if (showScenes)
		{
			sceneChooser.update();
		}
		
		MouseInput m = MouseInput.get();

		float mouseX = m.getXAbsolute();
		float mouseY = m.getYAbsolute();
		
		if (!m.isButtonDown(0))
		{
			mouseClicked = false;
		}
		
		//check if any of the buttons were clicked on
		if (m.isButtonDown(0) && mouseClicked == false)
		{
			mouseClicked = true;
			
			//check play button
			//get boundaries of object
			float x0 = playButton.getWorldTranslation().x-(BUTTON_WIDTH/2);
			float y0 = playButton.getWorldTranslation().y-(BUTTON_HEIGHT/2);
			float x1 = x0 + BUTTON_WIDTH;
			float y1 = y0 + BUTTON_HEIGHT;
			
			//check if mouse was clicked within the boundary
			if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
			{
				System.out.println("clicked!");
				//toggle play/pause and change button image
			}
			
			//check back button
			//get boundaries of object
			x0 = backButton.getWorldTranslation().x-(BUTTON_WIDTH/2);
			y0 = backButton.getWorldTranslation().y-(BUTTON_HEIGHT/2);
			x1 = x0 + BUTTON_WIDTH;
			y1 = y0 + BUTTON_HEIGHT;
			
			//check if mouse was clicked within the boundary
			if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
			{
				System.out.println("clicked!");
			}
			
			//check forward button
			//get boundaries of object
			x0 = forwardButton.getWorldTranslation().x-(BUTTON_WIDTH/2);
			y0 = forwardButton.getWorldTranslation().y-(BUTTON_HEIGHT/2);
			x1 = x0 + BUTTON_WIDTH;
			y1 = y0 + BUTTON_HEIGHT;
			
			//check if mouse was clicked within the boundary
			if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
			{
				System.out.println("clicked!");
			}
			
			//check scenes button
			//get boundaries of object
			x0 = scenesIndicator.getWorldTranslation().x-(SCENES_INDICATOR_WIDTH/2);
			y0 = scenesIndicator.getWorldTranslation().y-(SCENES_INDICATOR_HEIGHT/2);
			x1 = x0 + SCENES_INDICATOR_WIDTH;
			y1 = y0 + SCENES_INDICATOR_HEIGHT;
			
			//check if mouse was clicked within the boundary
			if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
			{
				System.out.println("clicked!");
				if (showScenes)
				{
					this.detachChild(sceneChooser);
					showScenes = false;
				}
				else
				{
					this.attachChild(sceneChooser);
					showScenes = true;
				}
			}
		}
	}
	
}
