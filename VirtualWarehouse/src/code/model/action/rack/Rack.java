package code.model.action.rack;

import code.model.action.pallet.StackedPallet;
import code.model.action.pick.Pick;
import code.model.action.product.Product;
import code.model.racklabels.BinNumberLabel;
import code.model.racklabels.CheckDigitLabel;
import code.model.racklabels.RackAisleLabel;
import code.util.DUtility;
import code.world.Room;
import code.world.WarehouseWorld;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;


public class Rack extends Node
{
	//a font to be used for labels
	private BitmapFont font = BitmapFontLoader.loadDefaultFont();
	
	private String productType;

	private WarehouseWorld ww;
	private String rackName;
	private Node rack;
	
	private int rackHeight; //how high up to go
	private String rackType; //single, double, for special cases
	
	private int smallRackHeight = 4;
	
	private final int totalOnShelf = 4; //the most boxes that can be on a particular shelf
	
	private StackedPallet pallets[][];
	private DUtility[] util;
	
	private final float spacing = .25f; //spacing used in position
	private Vector3f positions[][]; //positions of racks
	
	private float heightOffset; //space between each shelf

	public Rack(Node r, String name, WarehouseWorld ww, String productType)
	{
		this.productType = productType;
		this.rack = r;
		this.rackName = name;
		this.ww = ww;
		
		setRackHeight(name);
		setRackType(name);
		setHeightOffset(name);
		
		pallets = new StackedPallet[totalOnShelf][rackHeight];
		
		util = new DUtility[totalOnShelf];
		
		util[0] = new DUtility(10f, null, .2f, null);
		util[1] = new DUtility(10f, null, .2f, null);
		util[2] = new DUtility(10f, null, .2f, null);
		util[3] = new DUtility(10f, null, .001f, null);
		
		positions = new Vector3f[totalOnShelf][totalOnShelf];
		
		positions[0][0] = new Vector3f(0f,0f,0f);
		
		positions[1][0] = new Vector3f(-spacing,0f,0f);
		positions[1][1] = new Vector3f(spacing,0f,0f);
		
		positions[2][0] = new Vector3f(0f,0f,spacing);
		positions[2][1] = new Vector3f(-spacing,0f,-spacing);
		positions[2][2] = new Vector3f(spacing,0f,-spacing);
		
		positions[3][0] = new Vector3f(-spacing,0f,-spacing);
		positions[3][1] = new Vector3f(-spacing,0f,spacing);
		positions[3][2] = new Vector3f(spacing,0f,-spacing);
		positions[3][3] = new Vector3f(spacing,0f,spacing);

		this.setName(name);
	}
	
	//attaches an aisle to the rack
	public void attachAisleLabel(String text, String position) 
	{
		if (rackHeight == smallRackHeight)
		{
			return;
		}
		
		rack.updateGeometricState(0, true);
		
		RackAisleLabel t = new RackAisleLabel(text, font, ww.getVirtualWarehouse().getSharedNodeManager());
		float xOffset = (((BoundingBox) rack.getWorldBound()).xExtent);
		
		Quaternion q = new Quaternion();
		if (position.equals("LEFT"))
		{
			q.fromAngles(0,(float)(90*(Math.PI/180)),0);
			t.setLocalTranslation(xOffset+0.001f, 0.82f, 0);
		}
		else if (position.equals("RIGHT"))
		{
			q.fromAngles(0,(float)(-90*(Math.PI/180)),0);
			t.setLocalTranslation(-xOffset-0.001f, 0.82f, 0);
		}
		t.setLocalRotation(q);
		
		rack.attachChild(t);
	}
	
	//attaches a bin label to the rack
	public void attachBinLabel(String binNumber, String checkNumber, String position)
	{
		if (rackHeight == smallRackHeight)
		{
			return;
		}
		
		rack.updateGeometricState(0, true);
		
		BinNumberLabel binLabel = new BinNumberLabel(binNumber, font, ww.getVirtualWarehouse().getSharedNodeManager());
		CheckDigitLabel checkDigitLabel = new CheckDigitLabel(checkNumber, font, ww.getVirtualWarehouse().getSharedNodeManager());
		
		float zOffset = (((BoundingBox) rack.getWorldBound()).zExtent);
		float xOffset = (((BoundingBox) rack.getWorldBound()).xExtent)/2;
		
		if (position.equals("RIGHT"))
		{
			binLabel.setLocalTranslation(xOffset, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(xOffset, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		else if (position.equals("LEFT"))
		{
			binLabel.setLocalTranslation(-xOffset, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(-xOffset, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		else if (position.equals("CENTER"))
		{
			binLabel.setLocalTranslation(0.0f, 0.82f-BinNumberLabel.LABEL_HEIGHT/3, zOffset+0.001f);
			checkDigitLabel.setLocalTranslation(0.0f, 0.82f-(BinNumberLabel.LABEL_HEIGHT/3)-BinNumberLabel.LABEL_HEIGHT+.03f, zOffset+0.001f);
		}
		
		rack.attachChild(binLabel);
		rack.attachChild(checkDigitLabel);
	}
	
	//get how tall the rack is
	public float getHeight()
	{
		return this.rackHeight;
	}
	
	//get the pallets associated
	public StackedPallet[][] getPallets()
	{
		return pallets;
	}
	
	public StackedPallet[] getPallets(int row)
	{
		return pallets[row];
	}
	
	public StackedPallet[] getPickablePallets()
	{
		return pallets[0];
	}
	
	public StackedPallet getPickablePallet()
	{
		return pallets[0][0];
	}
	
	public Product getPickableProduct()
	{
		if (isPickable())
		{
			return getPickablePallet().getTop().getProducts().getTop();
		}
		return null;
	}
	
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.pallets != null)
		{
			return getPickablePallet().pickSmallProduct();
		}
		return null;
	}
	
	public boolean isPickable()
	{
		return getPickablePallet().isPickable();
	}
	
	public String getBinNumber()
	{
		return getPickablePallet().getBinNumber();
	}
	
	public String getMainName()
	{
		return getPickablePallet().getMainName();
	}
	
	//sets the rack height, because of the special case
	private void setRackHeight(String name)
	{
		rackHeight = 0;
		
		if (name.toLowerCase().indexOf("rackssingleraised147") > -1)
		{
			rackHeight = smallRackHeight;
		}
		else
		{
			rackHeight = 6;
		}
	}
	
	private void setRackType(String name)
	{
		if (name.toLowerCase().indexOf("single") > -1)
		{
			if (name.indexOf("103") > -1 || name.indexOf("106") > -1)
			{
				rackType = "single two";
			}
			else
			{
				rackType = "single";
			}
		}
		else
		{
			rackType = "double";
		}
	}
	
	//sets the height of set because of the special case, racks being raised
	private void setHeightOffset(String name)
	{
		if (name.toLowerCase().indexOf("raised") > -1)
		{
			heightOffset = 0.48f;
		}
		else
		{
			heightOffset = 0.82f; 
		}
	}
	
	private int random(int lowestNum, int maxNum) 
	{
		return (int)Math.round((double)FastMath.nextRandomFloat()*(maxNum-lowestNum))+lowestNum;
	}
	
	//create pallets in the racks
	public void createThePallets(String binNumber, boolean withProduct)
	{
		//for how tall it is
		for (int i=0; i<rackHeight; i++)
		{
			int h1;//pallet height
			int h2;//box height
			boolean tempProduct = false;//product associated
			
			int num; //how many products to put on the shelf
			
			if (rackType.equals("single")) //special case, only one product on these small racks
			{
				num = 1;
			}
			else if ((rackType.indexOf("single") > -1 && rackType.indexOf("two") > -1)) //another special case, either 1 or 2 products
			{
				num = random(1,2);
			}
			else //either one, two, three or four products
			{
				num = random(1,4);
			}
			
			//for how many boxes it decided to put on the shelf
			for (int j=0; j<num; j++)
			{
				//usual case, if it is the normal height offset
				if (heightOffset == .82f)
				{
					if (i==0) //if it is the ground layer
					{
						h1 = random(1,2);
						h2 = 1;
						tempProduct = withProduct;
					}
					else
					{
						h1 = random(1,2);
						h2 = random(1,2);
					}
				}
				else //there cant be as many on the raised racks, special case
				{
					if (rackHeight != smallRackHeight)
					{
						if (i+1==rackHeight)
						{
							h1 = random(3,8);
							h2 = random(3,8);
						}
						else if (i==0)
						{
							h1 = random(1,2);
							h2 = 1;
							tempProduct = withProduct;
						}
						else
						{
							h1 = random(1,2);
							h2 = 1;
						}
					}
					else
					{
						if (i+1==rackHeight)
						{
							h1 = random(10,20);
						}
						else
						{
							h1 = random(5,10);
						}
						h2 = 0;
					}
				}
				
				float trans1 = 0f;
				float trans2 = 0f;
				
				if (rackType.equals("double")) //if it is a double rack, we can translate the pallets a little bit within the shelf
				{
					switch (num) //translate randomly depending on the number of boxes we are putting on the rack
					{
						case 1:
							trans1 = util[num-1].translation();
							trans2 = util[num-1].translation();
							break;
						case 2:
							trans2 = util[num-1].translation();
							break;
						case 3:
							if (j == 0) //only translate the first box in the three case
							{
								trans1 = util[num-1].translation();
							}
							break;
						case 4:
							trans1 = util[num-1].translation();
							trans2 = util[num-1].translation();
							break;
					}
				}
				
				float rot = util[num-1].rotation();
				
				StackedPallet SDP = new StackedPallet(-1, h1,this.ww,binNumber,rackName+"_"+j+i,tempProduct,h2,productType);
				
				pallets[j][i] = SDP;
				
				SDP.setLocalTranslation(positions[num-1][j].x+trans1, 0f+heightOffset*i, positions[num-1][j].z+trans2);
				SDP.setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f));

				rack.attachChild(SDP);
			}
		}
	}
}