package code.model.action.product;

import code.model.action.pick.Pick;
import code.util.DUtility;
import code.world.WarehouseWorld;

import com.jme.math.Quaternion;
import com.jme.scene.Node;

/**
 * The Base Class for a StackedProduct
 * Creates a bunch of products stacked on top of each other
 * 
 * Rack->StackedPallets->Pallets->[StackedProducts]->Product
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class StackedProduct extends Node
{
	private DUtility util; //a utility for randomness
	private String productType; //the type of product
	
	private Product productStack[]; //the array of products (top product is the one to pick from)
	
	private float height; //how many products there are in the stack
	
	private String _b = "_b00"; //the header name for the products
	
	public StackedProduct(int height, WarehouseWorld ww)
	{
		this(height,ww,null,null,false,null);
	}
	
	public StackedProduct(int height, WarehouseWorld ww, String binNumber, String name)
	{
		this(height,ww,binNumber,name,false,null);
	}
	
	//the height of the products, warehouse world, the bin number, the name of the products, if it is pickable, the type of product
	public StackedProduct(int height, WarehouseWorld ww, String binNumber, String name, boolean pickable, String productType)
	{		
		util = new DUtility(20f,null,.01f,.27f); //20 degrees of randomness, .01 units of random translation, .27 units for each height of the box
		
		if (height <= 0) //there has to be at least 1 product in the stack
		{
			height = 1;
		}
		
		this.height = height;
		this.setName(name);
		
		productStack = new Product[height]; //make the stack
		
		for (int i=0;i<height;i++) //for each product
		{
			if (i+1 != height) //if it is not the top one
			{
				productStack[i] = new Product(ww,binNumber,name+_b+i,false, productType);//make a product that is not pickable
			}
			else
			{
				productStack[i] = new Product(ww,binNumber,name+_b+i,pickable, productType);//make a product that could be pickable based on param pickable
			}
			
			float rot = util.rotation(); //random rotation
			float trans1 = util.translation(); //random translation (x)
			float trans2 = util.translation(); //(z)
			
			productStack[i].setLocalTranslation(trans1, util.getH()*i, trans2); //set the product translation
			productStack[i].setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f));//set the product ortation
			
			this.attachChild(productStack[i]); //attach the product to the productStack node
		}
	}
	
	//return the height of the product stack
	public float getHeight()
	{
		return this.height;
	}
	
	//get a particular box/product
	public Product getBox(int i)
	{
		if (i <= this.height - 1)
		{
			return productStack[i];
		}
		else
		{
			return null;
		}
	}
	
	//get the top product
	public Product getTop()
	{
		return this.productStack[(int)(this.height-1)];
	}
	
	//get the stack
	public Product[] getBoxes()
	{
		return productStack;
	}
	
	//simulate a pick
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.productStack != null)
		{
			return getTop().pickSmallProduct();
		}
		return null;
	}
	
	//get if it is pickable
	public boolean isPickable()
	{
		return getTop().isPickable();
	}
	
	//bin number
	public String getBinNumber()
	{
		return getTop().getBinNumber();
	}
	
	//the name of it
	public String getMainName()
	{
		return getTop().getMainName();
	}
}
