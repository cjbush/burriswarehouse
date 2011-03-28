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

public class Pallet extends Node
{
	private WarehouseWorld ww;
	
	private DUtility util;
	
	private StackedProduct products;
	
	public Pallet(WarehouseWorld ww)
	{
		this(ww,null,null,false,0,null);
	}

	public Pallet(WarehouseWorld ww, String binNumber, String name, boolean pickable)
	{
		this(ww,binNumber,name,pickable,0,null);
	}
	
	public Pallet(WarehouseWorld ww, String binNumber, String name, boolean pickable,int height,String file)
	{
		util = new DUtility(null,null,.04f,.07f);
		
		this.ww = ww;
		this.setName(name);

		if (file == null)
		{
			file = "palletMain.obj";
		}
		
		loadModel(file);
			
		if (height > 0)
		{
			products = new StackedProduct(height,ww,binNumber,name,pickable);
			
			float trans1 = util.translation();
			float trans2 = util.translation();
			
			products.setLocalTranslation(trans1, util.getH(), trans2);

			this.attachChild(products);
		}
		else
		{
			products = null;
		}
	}
	
	private Product getTop()
	{
		return this.products.getBox((int)(this.products.getHeight()-1));
	}
	
	private void loadModel(String file)
	{		
		Spatial m = ModelLoader.loadModel("obj", "data/models/pallets/main/"+file,"data/models/pallets/main/", ww.getVirtualWarehouse().getSharedNodeManager(), true, ww.getVirtualWarehouse().getDisplay().getRenderer(), "pallet");
		this.attachChild(m);
	}
	
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.products != null)
		{
			return getTop().pickSmallProduct();
		}
		return null;
	}
	
	public boolean isPickable()
	{
		return getTop().isPickable();
	}
	
	public StackedProduct getProducts()
	{
		return products;
	}
	
	public String getBinNumber()
	{
		return getTop().getBinNumber();
	}
	
	public String getMainName()
	{
		return getTop().getMainName();
	}
}