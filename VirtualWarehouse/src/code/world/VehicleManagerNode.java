package code.world;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import code.model.ModelLoader;
import code.model.action.pallet.Pallet;
import code.model.vehicle.Vehicle;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * A node that is a parent to all the vehicles in the warehouse. When created,
 * it loads the vehicle placement data from a placement file and creates the
 * corresponding vehicles.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class VehicleManagerNode extends Node {

	public static final String VEHICLE_DATA_FILE = "src/data/placement/vehicles.xml";
	
	private static final Logger logger = Logger.getLogger(VehicleManagerNode.class.getName());
	
	/**
	 * Creates a node containing the vehicles loaded from a placement files.
	 * @param ww
	 */
	public VehicleManagerNode(WarehouseWorld ww) {
		super("VehicleManager");
		
		//read vehicle placement data from an XML file
		try {
			
			//set up the XML parser
			File file = new File(VEHICLE_DATA_FILE);
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
			float scale;
			float translationX, translationY, translationZ;
			float rotationX, rotationY, rotationZ;
						
			//read in XML data for the vehilces and place them in the warehouse
			NodeList objectList = doc.getElementsByTagName("AutoPalletJack");
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
					
					nodeList = objectElement.getElementsByTagName("scale");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					scale = Float.parseFloat(((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("translationX");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationX = Float.parseFloat(((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim());
					
					nodeList = objectElement.getElementsByTagName("translationY");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationY = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("translationZ");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					translationZ = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("rotationX");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					rotationX = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));

					nodeList = objectElement.getElementsByTagName("rotationY");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					rotationY = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
					
					nodeList = objectElement.getElementsByTagName("rotationZ");
					element = (Element) nodeList.item(0);
					nl = element.getChildNodes();
					rotationZ = Float.parseFloat((((org.w3c.dom.Node) nl.item(0)).getNodeValue().trim()));
	
					//create an auto pallet jack with the information
					Vehicle autoPalletJack = new Vehicle(ww.getVirtualWarehouse());
					
					//set name, scale, translation, rotation
					autoPalletJack.setName(name);
					autoPalletJack.setLocalScale(scale);
					autoPalletJack.setLocalTranslation(translationX, translationY, translationZ);
					
					Quaternion q = new Quaternion();
					q.fromAngles((float)(rotationX*(Math.PI/180)),(float)(rotationY*(Math.PI/180)),(float)(rotationZ*(Math.PI/180)));
					autoPalletJack.setLocalRotation(q);
					
					this.attachChild(autoPalletJack);
					
					autoPalletJack.updateWorldBound();
					//autoPalletJack.lock();
					
				}
			}
		}
		
		catch (Exception e) {
			 e.printStackTrace();
		}
		
	}
	
}
