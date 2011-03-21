package code.model.action.product;

import code.model.ModelLoader;
import code.model.action.pick.Pick;
import code.world.WarehouseWorld;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class DProduct extends Node
{
	private String binNumber;
	private WarehouseWorld ww;
	private boolean pickable;
	
	public DProduct(WarehouseWorld ww)
	{
		this(ww,null,null,false);
	}

	public DProduct(WarehouseWorld ww, String binNumber, String name, boolean top)
	{
		this.ww = ww;
		this.setName(name);
		this.binNumber = binNumber;
		loadModel(top);
		pickable = top;
		
		if (top)
		{
			ww.getProductList().add(this);
		}
	}
	
	private void loadModel(boolean top)
	{
		Spatial m;
		m = ModelLoader.loadModel("obj", "data/models/boxes/generic/box.obj","data/models/boxes/generic/", ww.getVirtualWarehouse().getSharedNodeManager(), true, ww.getVirtualWarehouse().getDisplay().getRenderer(), "ignore");
		this.attachChild(m);
	}
	
	public Pick pickSmallProduct()
	{
		if (pickable)
		{
			Pick smallBox = new SmallProductBox(ww.getVirtualWarehouse().getSharedNodeManager(), "SmallProduct", this.ww);
			return smallBox;
		}
		return null;
	}

	public String getBinNumber()
	{
		return binNumber;
	}
	
	public boolean isPickable()
	{
		return pickable;
	}
	
	public String getMainName()
	{
		return name;
	}
}
