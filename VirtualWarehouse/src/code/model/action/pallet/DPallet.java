package code.model.action.pallet;

import code.model.ModelLoader;
import code.model.action.pick.Pick;
import code.model.action.product.DProduct;
import code.model.action.product.StackedDProduct;
import code.util.DUtility;
import code.world.WarehouseWorld;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class DPallet extends Node
{
	private WarehouseWorld ww;
	
	private DUtility util;
	
	private StackedDProduct products;
	
	public DPallet(WarehouseWorld ww)
	{
		this(ww,null,null,false,0);
	}

	public DPallet(WarehouseWorld ww, String binNumber, String name, boolean pickable)
	{
		this(ww,binNumber,name,pickable,0);
	}
	
	public DPallet(WarehouseWorld ww, String binNumber, String name, boolean pickable,int height)
	{
		util = new DUtility(null,null,.04f,.07f);
		
		this.ww = ww;
		this.setName(name);

		loadModel();
			
		if (height > 0)
		{
			products = new StackedDProduct(height,ww,binNumber,name,pickable);
			
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
	
	private DProduct getTop()
	{
		return this.products.getBox((int)(this.products.getHeight()-1));
	}
	
	private void loadModel()
	{
		Spatial m = ModelLoader.loadModel("obj", "data/models/pallets/main/palletMain.obj","data/models/pallets/main/", ww.getVirtualWarehouse().getSharedNodeManager(), true, ww.getVirtualWarehouse().getDisplay().getRenderer(), "pallet");
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
	
	public StackedDProduct getProducts()
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