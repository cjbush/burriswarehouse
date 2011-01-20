package code.infoicons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import code.app.VirtualWarehouse;
import code.hud.PopupMessageBox;
import code.model.ModelLoader;

/**
 * Sets up the InfoIcons in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class InfoIconManager extends Node {
	
	public static final String INFO_ICON_PLACEMENT_FILE = "src/data/placement/info_icons.xml";
	public static final String MODEL_DIR = "data/models/";
	
	public static final String READ_INFO_ICON_COMMAND = "read_info_icon";
	public static final int READ_INFO_ICON_KEY = KeyInput.KEY_SPACE;
	
	//distance the player needs to be from the icon before it becomes activated 
	public static final float ACIVATE_DISTANCE = 1.5f;
	
	private static final Logger logger = Logger.getLogger(ModelLoader.class.getName());
	
	//keep a list of all the InfoIcons
	private List<BasicInfoIcon> childrenList;
	
	//keep track of the player's location
	private Vector3f playerLocation;
	
	//keep track of the icon that is currently active (if any)
	private BasicInfoIcon activeIcon;
	private PopupMessageBox popup;
	
	private VirtualWarehouse warehouseGame;
	
	public InfoIconManager(VirtualWarehouse vw) {
		
		warehouseGame = vw;
		this.setName("Info Icon Manager Node");
		
		activeIcon = null;
		childrenList = new ArrayList<BasicInfoIcon>();
		
		buildInfoIcons();
		bindKeys();
	}
	
	/**
	 * checks if the player is in range of any infoIcons,
	 * and activate them if so
	 */
	public void updateInfoIcons() {
		
		//if space was pressed, show/remove text for the active infoIcon
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(READ_INFO_ICON_COMMAND, false)) {
			if (activeIcon != null)
			{
				if (popup == null)
				{
					//TODO:pause game when showing text? hide text after any key hit?
					showActiveIconText();
				}
				else
				{
					removeActiveIconText();
				}
			}
		}
		
		playerLocation = warehouseGame.getPlayerNode().getWorldTranslation();
		BasicInfoIcon closest = null;
		float closestDist = ACIVATE_DISTANCE;
		
		//find the closest infoIcon in activation range (if any)
        for (int i=0; i<childrenList.size(); i++)
        {
        	//find the info icons the player is close enough to activate and
        	//then find the closest one
        	BasicInfoIcon child = childrenList.get(i);
        	float dist = playerLocation.distance(child.getWorldTranslation());
        	if (dist < ACIVATE_DISTANCE)
        	{
        		if (closest != null)
        		{
        			if (dist < closestDist)
        			{
        				closest = child;
        			} 
        		}
        		else
        		{
        			closest = child;
        		}
        	}
        }
        
        //un-activate the icon if the player moved too far away
    	if (closest == null && activeIcon != null)
    	{
    		activeIcon.makeUnActive();
    		activeIcon = null;
    	}
    	//activate the new closest one icon and un-activate the old one (if any)
    	else if (closest != activeIcon)
    	{
    		if (activeIcon != null)
    		{
    			activeIcon.makeUnActive();
    			activeIcon = null;
    		}
    		closest.makeActive();
    		activeIcon = closest;
    	}
		
	}
	
	public void showActiveIconText() {
		logger.info(activeIcon.getText());
		popup = new PopupMessageBox(activeIcon.getText(), warehouseGame.getFont());
		warehouseGame.getHudNode().attachChild(popup);
	}
	
	public void removeActiveIconText() {
		logger.info("removing active icon textbox");
		warehouseGame.getHudNode().detachChild(popup);
		popup = null;
	}
	
	/**
	 * Reads InfoIcon information from an XML file and places them in the warehouse.
	 */
	private void buildInfoIcons() {
		
		//read InfoIcon placement data from an XML file
		try {
			
			//set up the XML parser
			File file = new File(INFO_ICON_PLACEMENT_FILE);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			doc.getDocumentElement().normalize();
			
			//set up some variables for the XML parser 
			NodeList nodeList;
			Element element;
			NodeList nl;
			
			//variables for holding data from XML file
			String name;
			String fileName, folderName, format;
			float translationX, translationY, translationZ;
			float scale;
			String text;
			
			//read in XML data for the icons and place them in the warehouse
			NodeList objectList = doc.getElementsByTagName("info_icon");
			for (int i = 0; i < objectList.getLength(); i++)
			{

				org.w3c.dom.Node objectNode = objectList.item(i);
			    if (objectNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
			    {
			    	
					Element objectElement = (Element) objectNode;
					
					nodeList = objectElement.getElementsByTagName("name");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					name = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					
					nodeList = objectElement.getElementsByTagName("fileName");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					fileName = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("folderName");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					folderName = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("format");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					format = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					
					nodeList = objectElement.getElementsByTagName("translationX");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationX = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("translationY");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationY = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("translationZ");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationZ = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					
					nodeList = objectElement.getElementsByTagName("scale");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					scale = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					
					nodeList = objectElement.getElementsByTagName("text");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					text = (((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					
					//load the model and attach to node
					Spatial object = null;
					object = ModelLoader.loadModel(format, MODEL_DIR + folderName + fileName, MODEL_DIR + folderName + "/", warehouseGame.getSharedNodeManager(), true);
					if (object != null)
					{
						Node infoIcon = new BasicInfoIcon(text);
						infoIcon.attachChild(object);
						
						infoIcon.setLocalScale(scale);
						infoIcon.setLocalTranslation(new Vector3f(translationX, translationY, translationZ));
						infoIcon.setName(name);

						this.attachChild(infoIcon);
						childrenList.add((BasicInfoIcon) infoIcon);
						
						/*Make the icon rotate for visual effect*/
						
						//create a controller
						SpatialTransformer st = new SpatialTransformer(1);
						st.setObject(infoIcon, 0, -1);
						
						//Assign a rotation
						Quaternion x360 = new Quaternion();
						x360.fromAngleAxis(FastMath.DEG_TO_RAD * 360, new Vector3f(0, 1, 0));
						Quaternion x180 = new Quaternion();
						x180.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 1, 0));
						Quaternion x0 = new Quaternion();
						x0.fromAngleAxis(0, new Vector3f(0, 1, 0));

						st.setRotation(0, 0, x0);
						st.setRotation(0, 2.5f, x180);
						st.setRotation(0, 5, x360);
						st.setRepeatType(Controller.RT_WRAP);
						
						st.interpolateMissing();
						infoIcon.addController(st);
						
					}
					else
					{
						logger.info("info icon model " + name + " could not be loaded at " + MODEL_DIR + folderName + "/" + fileName);
					}
					
			    }
			 }
			
		} catch (Exception e) {
				 e.printStackTrace();
		}
		
	}
	
	private void bindKeys() {
		KeyBindingManager.getKeyBindingManager().set(READ_INFO_ICON_COMMAND, READ_INFO_ICON_KEY);
	}

}
