package code.model.action.product;

import code.model.action.pick.Product;
import code.util.DUtility;
import code.world.WarehouseWorld;

import com.jme.math.Quaternion;
import com.jme.scene.Node;

public class StackedDProduct extends Node
{
	private DUtility util;
	
	private DProduct productStack[];
	
	private float height;
	
	private String _b = "_b00";
	
	public StackedDProduct(int height, WarehouseWorld ww)
	{
		this(height,ww,null,null,false);
	}
	
	public StackedDProduct(int height, WarehouseWorld ww, String binNumber, String name)
	{
		this(height,ww,binNumber,name,false);
	}
	
	public StackedDProduct(int height, WarehouseWorld ww, String binNumber, String name, boolean pickable)
	{		
		util = new DUtility(30f,.4f,null,.01f,.27f);
		
		if (height <= 0)
		{
			height = 1;
		}
		
		this.height = height;
		this.setName(name);
		
		productStack = new DProduct[height];
		
		for (int i=0;i<height;i++)
		{
			if (i+1 != height)
			{
				productStack[i] = new DProduct(ww,binNumber,name+_b+i,false);
			}
			else
			{
				productStack[i] = new DProduct(ww,binNumber,name+_b+i,pickable);
			}
			
			float rot = util.rotation();
			float trans1 = util.translation();
			float trans2 = util.translation();
			
			productStack[i].setLocalTranslation(trans1, util.getH()*i, trans2);
			productStack[i].setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f));
			
			this.attachChild(productStack[i]);
		}
	}
	
	public float getHeight()
	{
		return this.height;
	}
	
	public DProduct getBox(int i)
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
	
	private DProduct getTop()
	{
		return this.productStack[(int)(this.height-1)];
	}
	
	public DProduct[] getBoxes()
	{
		return productStack;
	}
	
	public Product pickSmallProduct()
	{
		if (isPickable() && this.productStack != null)
		{
			return getTop().pickSmallProduct();
		}
		return null;
	}
	
	public boolean isPickable()
	{
		return getTop().isPickable();
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
