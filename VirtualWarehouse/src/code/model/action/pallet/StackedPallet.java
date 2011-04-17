package code.model.action.pallet;

import code.world.WarehouseWorld;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.Node;

import code.model.action.pick.Pick;
import code.util.DUtility;

/**
 * The Base Class for a StackPallets (Used by Misc. Pallets)
 * Creates Pallets on top of pallets
 * 
 * Rack->[StackedPallets]->Pallets->StackedProducts->Product
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class StackedPallet extends Node
{
	private boolean inUse; //if it is being used by a palletJack
	private Node productNode; //the productNode (child of this)
	private String productType;
	
	private int id;
	
	private Pallet palletStack[]; //the array of pallets (first index is bottom of the stack)
	
	private float height; //how tall it is
	
	private DUtility util; // a utility for randomness
	
	private String _p = "_p00"; //pallet header name
	
	public StackedPallet(int height, WarehouseWorld ww)
	{
		this(-1, height,ww,null,null,false,0,"random");
	}
	
	public StackedPallet(int height, WarehouseWorld ww, String binNumber, String name)
	{
		this(-1, height,ww,binNumber,name,false,0, "random");
	}
	
	public int getID(){ return id; }
	public void setID(int id){ this.id = id; }
	
	//id, pallet height, warehouse world, bin Number, unique name, if it has a product, product height, type of product
	public StackedPallet(int id, int height, WarehouseWorld ww, String binNumber, String name, boolean pickable, int productHeight, String productType)
	{		
		this.productType = productType; //the type of product
		this.id = id;
		if (height <= 0) //there must be at least one pallet in the stack
		{
			height = 1;
		}
		
		productNode = new Node("product node");
		this.attachChild(productNode);
		
		util = new DUtility(10f,null,.01f,.07f);//10 degrees of random roation, .01 units of random translating , .07 units height for each pallet
		
		this.height = height;//how many pallets high
		
		palletStack = new Pallet[height]; //create it
		
		String palletType = getPalletType(); //get the type of pallet (color)
		
		//Diagram of StackedPallet (2D View)
		
		//	[]		one product (notice it can only be on top of the highest pallet
		//	___
		//	___		height - 2
		
		for (int i=0;i<height;i++) //for each pallet in the stack
		{
			if (i+1 != height) //if it is not the top pallet
			{
				palletStack[i] = new Pallet(ww,binNumber,name+_p+i,pickable,0,palletType, productType); //create a pallet with no products attached
			}
			else
			{
				palletStack[i] = new Pallet(ww,binNumber,name+_p+i,pickable,productHeight,palletType, productType); //create a pallet with the products on top
			}
			
			float rot = util.rotation(); //get a random rotation
			float trans1 = util.translation(); //get a trandom translation
			float trans2 = util.translation();
			
			palletStack[i].setLocalTranslation(trans1, util.getH()*i, trans2); //set the translation
			palletStack[i].setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f)); //set the rotation
			
			this.attachChild(palletStack[i]); //attach it
		}
		
		ww.getPalletsList().add(this); //attach the stacked pallets to the world list for the pallet jack to pick up later
	}
	
	//the type of color
	private String getPalletType()
	{
		int choice = (int)FastMath.floor((FastMath.nextRandomFloat()*6)); //number between 0,6)
		
		String file = "";
		
		switch (choice) //determine which color based on the choice
		{	
			case 3:
			case 4:
				file = "palletMain2.obj"; //brown seems to appear more often than red
				break;
			case 5:
				file = "palletMain3.obj"; //red seems to appear the least often
				break;
			case 0:
			case 1:
			case 2:
			default:
				file = "palletMain.obj"; //blue is the standard color
				break;
		}
		
		return file;
	}
	
	//get the top pallet
	public Pallet getTop()
	{
		return this.palletStack[(int)(this.height-1)];
	}
	
	//get the height of the stack
	public float getHeight()
	{
		return this.height;
	}
	
	//get all of the pallets
	public Pallet[] getPallets()
	{
		return palletStack;
	}
	
	//get a particular pallet
	public Pallet getPallet(int i)
	{
		if (i <= this.height - 1)
		{
			return palletStack[i];
		}
		return null;
	}
	
	//simulate a pick based on the stack
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.palletStack != null)
		{
			return getTop().pickSmallProduct();
		}
		return null;
	}
	
	//check to see if it is pickable
	public boolean isPickable()
	{
		return getTop().isPickable();
	}
	
	//get the bin number assoicated with
	public String getBinNumber()
	{
		return getTop().getBinNumber();
	}
	
	//get the name
	public String getMainName()
	{
		return getTop().getMainName();
	}
	
	//from Virtual Warehouse Team
	
	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	
	public Node getProductNode() {
		return productNode;
	}
}
