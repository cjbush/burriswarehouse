package code.hud;

import java.util.ArrayList;

import com.jme.animation.SpatialTransformer;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class SceneChooser extends HUD {
	
	public static final float QUAD_HEIGHT = 100;
	public static final float QUAD_WIDTH = 128;
	public static final float BORDER_QUAD_HEIGHT = 104;
	public static final float BORDER_QUAD_WIDTH = 132;
	
	public static final float HEIGHT_OFFSET = 80;
	public static final float WIDTH_OFFSET = 100;
	
	//keep a list of all the scenes to choose from
	private ArrayList<Node> scenes = new ArrayList<Node>();
	
	private int sceneMouseOn = -1;
	private boolean mouseOver = false;
	
	//have to keep track of mouse button being clicked down,
	//otherwise one click will register multiple mouse
	//button down events
	private boolean mouseClicked = false;
	
	private float maxScenesOnScreen;
	
	public SceneChooser() {
		
		//TODO kind of close but not accurate
		maxScenesOnScreen = getDisplayHeight()/(BORDER_QUAD_HEIGHT + HEIGHT_OFFSET);
		
		//create all the scenes choices
		addScene();
		addScene();
		addScene();
		addScene();
		addScene();
		
		//draw the scene choices on the screen
		float heightOffset = HEIGHT_OFFSET;
		float widthOffset = WIDTH_OFFSET;
		for(int i=0; i<scenes.size(); i++)
		{
			Node n = scenes.get(i);
			n.setLocalTranslation(getDisplayWidth()-widthOffset, getDisplayHeight()-heightOffset, 0);
			
			heightOffset += HEIGHT_OFFSET + (BORDER_QUAD_HEIGHT/2);
		}
		
		this.updateRenderState();
	}
	
	private void addScene() {
		
		//make a node for holding the scene quad and background quad
		Node sceneChoice = new Node("Scene Choice");
		
		//create a quad for showing the scene on and position it on the screen
		Quad sceneQuad = new Quad("sceneQuad", QUAD_WIDTH, QUAD_HEIGHT);
		sceneQuad.setZOrder(1);
		sceneChoice.attachChild(sceneQuad);
		
		//create a quad for a background
		Quad sceneBorderQuad = new Quad("sceneBorderQuad", BORDER_QUAD_WIDTH, BORDER_QUAD_HEIGHT);
		sceneBorderQuad.setZOrder(2);
		sceneBorderQuad.setDefaultColor(ColorRGBA.black.clone());
		sceneChoice.attachChild(sceneBorderQuad);
		
		this.attachChild(sceneChoice);
		scenes.add(sceneChoice);
	}
	
	/**
	 * Checks for mouse clicks on the scene selection icons
	 */
	public void update() {
		MouseInput m = MouseInput.get();

		float mouseX = m.getXAbsolute();
		float mouseY = m.getYAbsolute();
		int mouseWheelDelta = m.getWheelDelta();
		
		if (!m.isButtonDown(0))
		{
			mouseClicked = false;
		}
		
		if (m.isButtonDown(0) && mouseClicked == false)
		{
			mouseClicked = true;
			
			//loop through all the scene choices to see if they were clicked
			for(int i=0; i<scenes.size(); i++)
			{
				Spatial borderQuad = scenes.get(i).getChild("sceneBorderQuad");
				
				//get boundaries of object
				float x0 = borderQuad.getWorldTranslation().x-(BORDER_QUAD_WIDTH/2);
				float y0 = borderQuad.getWorldTranslation().y-(BORDER_QUAD_HEIGHT/2);
				float x1 = x0 + BORDER_QUAD_WIDTH;
				float y1 = y0 + BORDER_QUAD_HEIGHT;
				
				//check if mouse was clicked within the boundary
				if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
				{
					System.out.println("clicked!");
				}
			}
		}
		
		//check if mouse is over one of the scenes
		for(int i=0; i<scenes.size(); i++)
		{
			Spatial borderQuad = scenes.get(i).getChild("sceneBorderQuad");
			
			//get boundaries of object
			float x0 = borderQuad.getWorldTranslation().x-(BORDER_QUAD_WIDTH/2);
			float y0 = borderQuad.getWorldTranslation().y-(BORDER_QUAD_HEIGHT/2);
			float x1 = x0 + BORDER_QUAD_WIDTH;
			float y1 = y0 + BORDER_QUAD_HEIGHT;
			
			//check if mouse was moved within the boundary
			if (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1)
			{
				if (mouseOver == false)
				{
					//System.out.println("in");
					sceneMouseOn = i;
					mouseOver = true;
					
					SpatialTransformer st = new SpatialTransformer(1);
					st.setObject(scenes.get(i), 0, -1);
					
					st.setScale(0, 0, new Vector3f(1f, 1f, 0));
					st.setPosition(0, 0.2f, new Vector3f(scenes.get(i).getLocalTranslation().x, scenes.get(i).getLocalTranslation().y, 0));
					st.setScale(0, 0.2f, new Vector3f(1.2f, 1.2f, 0));
					
					st.interpolateMissing();
					scenes.get(i).addController(st);
				}
			}
		}
		
		//need to check if the mouse was moved off if last seen on a scene
		if (mouseOver == true)
		{
			Spatial borderQuad = scenes.get(sceneMouseOn).getChild("sceneBorderQuad");
			
			//get boundaries of object
			float x0 = borderQuad.getWorldTranslation().x-(BORDER_QUAD_WIDTH/2);
			float y0 = borderQuad.getWorldTranslation().y-(BORDER_QUAD_HEIGHT/2);
			float x1 = x0 + BORDER_QUAD_WIDTH;
			float y1 = y0 + BORDER_QUAD_HEIGHT;
			
			//check if mouse is outside the boundary
			if ( !(mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY < y1) )
			{
				//System.out.println("out");
				
				SpatialTransformer st = new SpatialTransformer(1);
				st.setObject(scenes.get(sceneMouseOn), 0, -1);
				
				st.setScale(0, 0, new Vector3f(1.2f, 1.2f, 0));
				st.setPosition(0, 0.2f, new Vector3f(scenes.get(sceneMouseOn).getLocalTranslation().x, scenes.get(sceneMouseOn).getLocalTranslation().y, 0));
				st.setScale(0, 0.2f, new Vector3f(1f, 1f, 0));
				
				st.interpolateMissing();
				scenes.get(sceneMouseOn).addController(st);
				
				sceneMouseOn = -1;
				mouseOver = false;
			}
		}
		
		//check for scrolling
		if (mouseWheelDelta != 0 && scenes.size() > maxScenesOnScreen)
		{
			if (mouseWheelDelta > 0 && scenes.get(scenes.size()-1).getWorldTranslation().y < 300)
			{
				//scroll up
				//System.out.println("up");
				
				SpatialTransformer st = new SpatialTransformer(1);
				st.setObject(this, 0, -1);
				
				st.setPosition(0, 0f, new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y, 0));
				st.setPosition(0, 0.3f, new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y+100, 0));
				
				st.interpolateMissing();
				this.addController(st);
			}
			else if (mouseWheelDelta < 0 && scenes.get(0).getWorldTranslation().y > 400)
			{
				//scroll down
				//System.out.println("down");
				
				SpatialTransformer st = new SpatialTransformer(1);
				st.setObject(this, 0, -1);
				
				st.setPosition(0, 0f, new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y, 0));
				st.setPosition(0, 0.3f, new Vector3f(this.getLocalTranslation().x, this.getLocalTranslation().y-100, 0));
				
				st.interpolateMissing();
				this.addController(st);
			}
		}
		
	}
}
