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

/**
 * The Base Class for a Rack
 * Creates StackedPallets within the racks
 * Attaches bin labels and aisle labels
 * 
 * [Rack]->StackedPallets->Pallets->StackedProducts->Product
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Rack extends Node
{
	//a font to be used for labels
	private BitmapFont font = BitmapFontLoader.loadDefaultFont();
	
	private String productType; //what kind of product (Freezer, Produce, etc.)

	private WarehouseWorld ww;
	private String rackName; //the name of the rack
	
	private Node rack; //the rack node
	private Node SDPs; //the StackedProducts node
	//(Note, sdps used to be a child node of rack.  Because rack has transparency within it, somehow transparency of the racks were
	//being propagated down to the products (good 'ole JME).  This is used to obviously fix this
	
	private int rackHeight; //how high up to go (normally it is six)
	private String rackType; //single, double, for special cases
	
	private int smallRackHeight = 4; //constant smallest seems to be four
	private int smallSpecialHeight = 3; //constant for the special Raised Rack Height
	private int normalHeight = 6; //constant for the normal height
	
	private float normalHeightOffset = .82f;
	private float smallHeightOffset = 0.46f;
	private float largeHeightOffset = .88f;
	
	private final int totalOnShelf = 4; //the most boxes that can be on a particular shelf
	
	private StackedPallet pallets[][]; //the array of pallets (first index is total, second if height)
	private DUtility[] util; //contains a bunch of randomizing and math constants
	
	private final float spacing = .25f; //spacing used in position
	private Vector3f positions[][]; //positions of racks
	
	private float heightOffset; //space between each shelf
	
	private boolean reallySpecial;
	
	//Here are some cases
	//If it randomly came to one total on the shelf, the format will look like this
	
	//(Bird eye view below of rack)
	
	//	rack with no products	rack with one product	rack with two products	rack with three products	rack with four products
	//	_________					_________					_________				_________					_________
	//	|		|					|		|					|		|				|  		|					|		|
	//	|		|					|		|					|		|				| *	  *	|					| *   *	|
	//	|		|					|	*	|					| *   *	|				|	*	|					| *   *	|
	//	|_______|					|_______|					|_______|				|_______|					|_______|
	
	//												(Distance from middle to the stars is spacing)
	
	
	//(2D view below of rack)
	
	//				_________________
	//				|				|   \_  heightOffset
	//				|_______________|	/
	//				|				|
	//				|_______________|
	//				|				|
	//				|_______________|
	//				|				|
	//				|_______________|
	//				|				|		rackType - rackSingle106 (a normal rack)
	//				|_______________|
	//				|				|
	//				|_______________|		6 shelves, rackHeight
	
	
	public Rack(Node r, String name, WarehouseWorld ww, String productType)
	{
		this.productType = productType;
		this.rack = r;
		this.rackName = name;
		this.ww = ww;
		
		setReallySpecial(name);
		setRackHeight(name);
		setRackType(name);
		setHeightOffset(name);
		
		pallets = new StackedPallet[totalOnShelf][rackHeight]; //make the StackedPallets array
		
		util = new DUtility[totalOnShelf]; //make a utility
		
		util[0] = new DUtility(10f, null, .2f, null); //one product (10 degrees of random rotation, .2 units of random translation)
		util[1] = new DUtility(10f, null, .2f, null); //two product (10 degrees of random rotation, .2 units of random translation)
		util[2] = new DUtility(10f, null, .2f, null); //three product (10 degrees of random rotation, .2 units of random translation)
		util[3] = new DUtility(10f, null, .001f, null); //four product (10 degrees of random rotation, .001 units of random translation)
		
		positions = new Vector3f[totalOnShelf][totalOnShelf]; //hardcoded postions of where products will be on the racks
		//(this will be added with DUtil to make some randomness)
		
		//(Look at diagram above, '*' are products)
		
		//One product case
		positions[0][0] = new Vector3f(0f,0f,0f); //right in the center
		
		//Two product case
		positions[1][0] = new Vector3f(-spacing,0f,0f); //center, offset left
		positions[1][1] = new Vector3f(spacing,0f,0f); //center, offset right
		
		//three product case
		positions[2][0] = new Vector3f(0f,0f,spacing);//center, lower middle
		positions[2][1] = new Vector3f(-spacing,0f,-spacing); //upper middle, offset left
		positions[2][2] = new Vector3f(spacing,0f,-spacing); //upper middle, offset right
		
		//four product case
		positions[3][0] = new Vector3f(-spacing,0f,-spacing); //upperleft corner
		positions[3][1] = new Vector3f(-spacing,0f,spacing); //upperright corner
		positions[3][2] = new Vector3f(spacing,0f,-spacing); //lowerleft corner
		positions[3][3] = new Vector3f(spacing,0f,spacing); //lowerright corner

		this.setName(name);
	}
	
	//attaches an aisle to the rack (from Virtual Warehouse Team)
	public void attachAisleLabel(String text, String position) 
	{
		if (rackHeight == smallRackHeight) //special case, if it is a smaller rack, no aisle label is needed)
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
	
	//attaches a bin label to the rack (from Virtual Warehouse Team)
	public void attachBinLabel(String binNumber, String checkNumber, String position)
	{
		if (rackHeight == smallRackHeight) //special case, if it is a smaller rack, no bin label needed
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
	
	//get a particular row of pallets
	public StackedPallet[] getPallets(int row)
	{
		return pallets[row];
	}
	
	//get the pickable Pallets (ground level row)
	public StackedPallet[] getPickablePallets()
	{
		return pallets[0];
	}
	
	//get one pickable pallet (ground level)
	public StackedPallet getPickablePallet()
	{
		return pallets[0][0];
	}
	
	//get a pickable Product
	public Product getPickableProduct()
	{
		if (isPickable())
		{
			return getPickablePallet().getTop().getProducts().getTop();
		}
		return null;
	}
	
	//simulate a pick by calling the current rack (if you need it someday)
	public Pick pickSmallProduct()
	{
		if (isPickable() && this.pallets != null)
		{
			return getPickablePallet().pickSmallProduct();
		}
		return null;
	}
	
	//if it is actually pickable
	public boolean isPickable()
	{
		return getPickablePallet().isPickable();
	}
	
	//get the bin number
	public String getBinNumber()
	{
		return getPickablePallet().getBinNumber();
	}
	
	//get the name
	public String getMainName()
	{
		return getPickablePallet().getMainName();
	}
	
	//sets if the rack is really special (racksDoubleRaised206, racksSingleRaised206)
	private void setReallySpecial(String name)
	{		
		reallySpecial = name.toLowerCase().indexOf("raised206") > -1; //if it is, there are some extreme special cases
	}
	
	//sets the rack height, because of the special case
	private void setRackHeight(String name)
	{
		rackHeight = 0;
		
		if (reallySpecial)
		{
			rackHeight = smallSpecialHeight;
		}
		else
		{
			if (name.toLowerCase().indexOf("rackssingleraised147") > -1) //if it is a single rack, it is a smaller height
			{
				rackHeight = smallRackHeight;
			}
			else
			{
				rackHeight = normalHeight;
			}
		}
	}
	
	//set what kind of rack it is
	private void setRackType(String name)
	{
		if (name.toLowerCase().indexOf("single") > -1) //if it is a single rack
		{
			if (name.indexOf("103") > -1 || name.indexOf("106") > -1 || reallySpecial) // but in is in the 100 series (or it is a special raised rack)
			{
				rackType = "single two"; //two products can fit
			}
			else
			{
				rackType = "single"; //just one product can fit
			}
		}
		else
		{
			rackType = "double"; //normal rack
		}
	}
	
	//sets the height of set because of the special case, racks being raised
	private void setHeightOffset(String name)
	{
		if (reallySpecial)
		{
			heightOffset = largeHeightOffset; //these racks have a much bigger offset between the shelves
		}
		else if (name.toLowerCase().indexOf("raised") > -1) //if it is a raised rack
		{
			heightOffset = smallHeightOffset; //the height offsets are smaller
		}
		else
		{
			heightOffset = normalHeightOffset; //otherwise they are normal
		}
	}
	
	//Quick random function, from low to high
	private int random(int lowestNum, int maxNum) 
	{
		return (int)Math.round((double)FastMath.nextRandomFloat()*(maxNum-lowestNum))+lowestNum;
	}
	
	//create pallets in the racks
	public Node createThePallets(String binNumber, boolean withProduct)
	{
		SDPs = new Node(); //the node for the StackedPallets
		
		//for how tall it is
		for (int i=0; i<rackHeight; i++)
		{
			int h1;//pallet height
			int h2;//box/product height
			boolean tempProduct = false;//product associated
			
			int num; //how many products to put on the shelf
			
			float start = reallySpecial ? 2.46f : 0f;
			
			if (rackType.equals("single")) //special case, only one product can fit on these small racks
			{
				num = 1;
			}
			else if ((rackType.indexOf("single") > -1 && rackType.indexOf("two") > -1)) //another special case, either 1 or 2 products can fit
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
				//usual case, if it is the normal height between the shelves
				if (heightOffset == normalHeightOffset)
				{
					if (i==0) //if it is the ground row
					{
						h1 = random(1,2); //1 to 2 pallets
						h2 = 1; //always one product
						tempProduct = withProduct; //either it is pickable or not pickable based on the parameter that was passed
					}
					else
					{
						h1 = random(1,2); //1 to 2 pallets
						h2 = random(1,2); //1 to 2 products
					}
				}
				else if (heightOffset == smallHeightOffset)//there can be many pallets and products on the raised racks (especially top row), special case
				{
					if (rackHeight != smallRackHeight) //if the rackHeight is not the small one
					{
						if (i+1==rackHeight) //if it is the top row
						{
							h1 = random(3,8); //3 to 8 pallets
							h2 = random(3,8); //3 to 8 products
						}
						else if (i==0) //if it is the ground row
						{
							h1 = random(1,2); //1 to 2 pallets
							h2 = 1; //1 product
							tempProduct = withProduct; //either it is pickable or not pickable based on the parameter that was passed
						}
						else //middle row
						{
							h1 = random(1,2);
							h2 = 1;
						}
					}
					else //otherwise these are the hanging racks in the export and import areas
					{
						if (i+1==rackHeight) //top row
						{
							h1 = random(10,20); //lots of pallets
						}
						else
						{
							h1 = random(3,7); //not as many pallets
						}
						h2 = 0; //NO products on these (according to the pictures)
					}
				}
				else //special raised case
				{
					if (i+1==rackHeight) //top row
					{
						h1 = random(1,2); //1 to 2 pallets
						h2 = 1; //1 product
					}
					else
					{
						h1 = random(1,3); //1 to 3 pallets
						h2 = random(0,2); //0 to 2 products
					}
				}
				
				float trans1 = 0f; //the random translation within the rack (x)
				float trans2 = 0f; //(z)
				
				if (rackType.equals("double")) //if it is a double rack, we can translate the pallets a little bit within the shelf
				{
					switch (num) //translate randomly depending on the number of boxes we are putting on the rack
					{
						case 1:
							trans1 = util[num-1].translation(); //random translation #
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
				
				float rot = util[num-1].rotation(); //random rotation
				
				//finally, lets create it
				//id, pallet height, warehouse world, bin Number, unique name, if it has a product, product height, type of product
				StackedPallet SDP = new StackedPallet(-1, h1,this.ww,binNumber,rackName+"_"+j+i,tempProduct,h2,productType);
				
				pallets[j][i] = SDP; //put it in our array
				
				SDP.setLocalTranslation(positions[num-1][j].x+trans1, start+heightOffset*i, positions[num-1][j].z+trans2); //translate it
				SDP.setLocalRotation(new Quaternion().fromAngles(0f,(float)(rot*(Math.PI/180)),0f)); //rotate it

				SDPs.attachChild(SDP); //attach it to our node to return
			}
		}
		
		return SDPs; //return that to the world
	}
}