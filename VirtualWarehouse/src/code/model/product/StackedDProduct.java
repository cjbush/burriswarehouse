package code.model.product;

import code.world.WarehouseWorld;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;

/**
 * The large boxes found on the pallets on the racks in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class StackedDProduct extends Node
{
	private float rotationFactor = .1f;
	private float scaleFactor = .08f;
	private float transFactor = .01f;
	
	private float h = .22f;
	
	private DProduct productStack[];
	
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
		if (height <= 0)
		{
			height = 1;
		}
		
		productStack = new DProduct[height];
		
		for (int i=0;i<height;i++)
		{
			
			if (i+1 != height)
			{
				productStack[i] = new DProduct(ww);
			}
			else
			{
				productStack[i] = new DProduct(ww,binNumber,name,pickable);
			}
			
			float scale = (1-scaleFactor/2)+FastMath.nextRandomFloat()*scaleFactor*2;
			float rot = (5-rotationFactor/2)+FastMath.nextRandomFloat()*rotationFactor*2;
			float trans1 = (transFactor/2)+FastMath.nextRandomFloat()*transFactor*2;
			float trans2 = (transFactor/2)+FastMath.nextRandomFloat()*transFactor*2;
			
			productStack[i].setLocalTranslation(trans1, h*i, trans2);
			productStack[i].setLocalRotation(new Quaternion(0, rot, 0, 1));
			productStack[i].setLocalScale(scale);	
			
			this.attachChild(productStack[i]);
		}
	}
}
