package code.model.action.pallet;

import code.model.ModelLoader;
import code.model.action.pick.Pick;
import code.model.action.product.Product;
import code.model.action.product.StackedProduct;
import code.util.DUtility;
import code.world.WarehouseWorld;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The Base Class for a Pallet
 * Creates StackedProducts on top of a pallet
 * 
 * Rack->StackedPallets->[Pallets]->StackedProducts->Product
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Pallet extends Node
{
	private WarehouseWorld ww;
	
	private DUtility util; //a utility for randomness
	
	private StackedProduct products; //the products on top of the pallet
	
	public Pallet(WarehouseWorld ww)
	{
		this(ww,null,null,false,0,null,null);
	}

	public Pallet(WarehouseWorld ww, String binNumber, String name, boolean pickable)
	{
		this(ww,binNumber,name,pickable,0,null,null);
	}
	
	public Pallet(WarehouseWorld ww, String binNumber, String name, boolean pickable,int height,String file, String productType)
	{
		util = new DUtility(null,null,.04f,.07f); //.04 random tranlation of the product stack, the height for the stackedproducts
		
		this.ww = ww;
		this.setName(name);

		if (file == null) //if no file is associated for the color, give it the default
		{
			file = "palletMain.obj";
		}
		
		loadModel(file);
			
		if (height > 0) //if there are products on top of the pallet
		{
			products = new StackedProduct(height,ww,binNumber,name,pickable,productType); //make a new product stack
			
			float trans1 = util.translation(); //make some random translation
			float trans2 = util.translation();
			
			products.setLocalTranslation(trans1, util.getH(), trans2); //translate the product stack

			this.attachChild(products); //attach the product stack
		}
		else
		{
			products = null; //otherwise no product stack associated with this
		}
	}
	
	//get the top product box
	private Product getTop()
	{
		return this.products.getBox((int)(this.products.getHeight()-1));
	}
	
	//load the model
	private void loadModel(String file)
	{			
		Spatial m = ModelLoader.loadModel("obj", "data/models/pallets/main/"+file,"data/models/pallets/main/", true, ww.getVirtualWarehouse().getDisplay().getRenderer(), "pallet");
		this.attachChild(m);
	}
	
	//simulate a pick
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.products != null)
		{
			return getTop().pickSmallProduct();
		}
		return null;
	}
	
	//if it is pickable
	public boolean isPickable()
	{
		return getTop().isPickable();
	}
	
	//get the StackedProducts
	public StackedProduct getProducts()
	{
		return products;
	}
	
	//get the binNumber
	public String getBinNumber()
	{
		return getTop().getBinNumber();
	}
	
	//get the Name
	public String getMainName()
	{
		return getTop().getMainName();
	}
}