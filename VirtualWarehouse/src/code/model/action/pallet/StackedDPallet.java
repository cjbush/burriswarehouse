package code.model.action.pallet;

import code.world.WarehouseWorld;

import com.jme.math.Quaternion;
import com.jme.scene.Node;

import code.model.action.pick.Product;
import code.util.DUtility;


public class StackedDPallet extends Node
{
	private DPallet palletStack[];
	
	private float height;
	
	private DUtility util;
	
	private String _p = "_p00";
	
	public StackedDPallet(int height, WarehouseWorld ww)
	{
		this(height,ww,null,null,false,0);
	}
	
	public StackedDPallet(int height, WarehouseWorld ww, String binNumber, String name)
	{
		this(height,ww,binNumber,name,false,0);
	}
	
	public StackedDPallet(int height, WarehouseWorld ww, String binNumber, String name, boolean pickable, int productHeight)
	{		
		if (height <= 0)
		{
			height = 1;
		}
		
		util = new DUtility(10f,.4f,null,.01f,.07f);
		
		this.height = height;
		
		palletStack = new DPallet[height];
		
		for (int i=0;i<height;i++)
		{
			if (i+1 != height)
			{
				palletStack[i] = new DPallet(ww,binNumber,name+_p+i,pickable);
			}
			else
			{
				palletStack[i] = new DPallet(ww,binNumber,name+_p+i,pickable,productHeight);
			}
			
			float rot = util.rotation();
			float trans1 = util.translation();
			float trans2 = util.translation();
			
			palletStack[i].setLocalTranslation(trans1, util.getH()*i, trans2);
			palletStack[i].setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f));
			
			this.attachChild(palletStack[i]);
		}
	}
	
	private DPallet getTop()
	{
		return this.palletStack[(int)(this.height-1)];
	}
	
	public float getHeight()
	{
		return this.height;
	}
	
	public DPallet[] getPallets()
	{
		return palletStack;
	}
	
	public DPallet getPallet(int i)
	{
		if (i <= this.height - 1)
		{
			return palletStack[i];
		}
		return null;
	}
	
	public Product pickSmallProduct()
	{
		if (isPickable() && this.palletStack != null)
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