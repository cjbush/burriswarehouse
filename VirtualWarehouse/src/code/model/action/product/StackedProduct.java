package code.model.action.product;

import code.model.action.pick.Pick;
import code.util.DUtility;
import code.world.WarehouseWorld;

import com.jme.math.Quaternion;
import com.jme.scene.Node;

public class StackedProduct extends Node
{
	private DUtility util;
	private String productType;
	
	private Product productStack[];
	
	private float height;
	
	private String _b = "_b00";
	
	public StackedProduct(int height, WarehouseWorld ww)
	{
		this(height,ww,null,null,false, "random");
	}
	
	public StackedProduct(int height, WarehouseWorld ww, String binNumber, String name)
	{
		this(height,ww,binNumber,name,false, "random");
	}
	
	public StackedProduct(int height, WarehouseWorld ww, String binNumber, String name, boolean pickable, String productType)
	{		
		util = new DUtility(20f,null,.01f,.27f);
		
		if (height <= 0)
		{
			height = 1;
		}
		
		this.height = height;
		this.setName(name);
		
		productStack = new Product[height];
		
		for (int i=0;i<height;i++)
		{
			if (i+1 != height)
			{
				productStack[i] = new Product(ww,binNumber,name+_b+i,false, productType);
			}
			else
			{
				productStack[i] = new Product(ww,binNumber,name+_b+i,pickable, productType);
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
	
	public Product getTop()
	{
		return this.productStack[(int)(this.height-1)];
	}
	
	public Product[] getBoxes()
	{
		return productStack;
	}
	
	public Pick pickSmallProduct()
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
