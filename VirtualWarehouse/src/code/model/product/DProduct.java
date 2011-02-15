package code.model.product;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.SharedMeshManager;
import code.world.WarehouseWorld;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The large boxes found on the pallets on the racks in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
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
	}
	
	private void loadModel(boolean top)
	{
		Spatial m;
		if (!top)
		{
			m = ModelLoader.loadModel("obj", "data/models/boxes/generic/box.obj","data/models/boxes/generic/", ww.getVirtualWarehouse().getSharedNodeManager(), true);
		}
		else
		{
			m = ModelLoader.loadModel("obj", "data/models/boxes/generic/openbox.obj","data/models/boxes/generic/", ww.getVirtualWarehouse().getSharedNodeManager(), true);
		}
		this.attachChild(m);
	}
	
	public Product pickSmallProduct()
	{
		if (pickable)
		{
			Product smallBox = new SmallProductBox(ww.getVirtualWarehouse().getSharedNodeManager(), "SmallProduct");
			return smallBox;
		}
		return null;
	}

	public String getBinNumber() {
		return binNumber;
	}
	
	
}
