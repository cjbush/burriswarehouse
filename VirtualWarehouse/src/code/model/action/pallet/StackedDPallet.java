package code.model.action.pallet;

import code.world.WarehouseWorld;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.Node;

import code.model.action.pick.Pick;
import code.util.DUtility;


public class StackedDPallet extends Node
{
	private boolean inUse;
	private Node productNode;
	
	
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
		
		productNode = new Node("product node");
		this.attachChild(productNode);
		
		util = new DUtility(10f,null,.01f,.07f);
		
		this.height = height;
		
		palletStack = new DPallet[height];
		
		String palletType = getPalletType();
		
		for (int i=0;i<height;i++)
		{
			if (i+1 != height)
			{
				palletStack[i] = new DPallet(ww,binNumber,name+_p+i,pickable,0,palletType);
			}
			else
			{
				palletStack[i] = new DPallet(ww,binNumber,name+_p+i,pickable,productHeight,palletType);
			}
			
			float rot = util.rotation();
			float trans1 = util.translation();
			float trans2 = util.translation();
			
			palletStack[i].setLocalTranslation(trans1, util.getH()*i, trans2);
			palletStack[i].setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f));
			
			this.attachChild(palletStack[i]);
		}
		
		ww.getPalletsList().add(this);
	}
	
	private String getPalletType()
	{
		int choice = (int)FastMath.floor((FastMath.nextRandomFloat()*6));
		
		String file = "";
		
		switch (choice)
		{	
			case 3:
			case 4:
				file = "palletMain2.obj";
				break;
			case 5:
				file = "palletMain3.obj";
				break;
			case 0:
			case 1:
			case 2:
			default:
				file = "palletMain.obj";
				break;
		}
		
		return file;
	}
	
	public DPallet getTop()
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
	
	public Pick pickSmallProduct()
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
	
	//from old file
	
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
