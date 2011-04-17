package code.model.action.product;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

import code.model.ModelLoader;
import code.model.action.pick.Pick;
import code.world.WarehouseWorld;

import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Base Class for a Product
 * Allows you to pick small products from it
 * 
 * Rack->StackedPallets->Pallets->StackedProducts->[Product]
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Product extends Node
{
	private String productType; //the type of product
	private String binNumber; //bin number associated from it
	private WarehouseWorld ww;
	private boolean pickable; //if it is pickable
	
	//Dimensions
	private float width = .39f;
	private float depth = .39f;
	private float height = .27f;
	
	//where the dynamic texture path is at
	private final static String productTextureLocation = "src/data/productTextures/";
	
	public Product(WarehouseWorld ww)
	{
		this(ww,null,null,false,null);
	}

	//warehouse world, the bin number, the unique name, if it is the top box, what type of product it is
	public Product(WarehouseWorld ww, String binNumber, String name, boolean top, String productType)
	{
		this.ww = ww;
		this.setName(name);
		
		this.binNumber = binNumber;
		
		this.productType = productType;
		
		loadModel(productType); //load the model based on the product type
		pickable = top; //if it is pickable
		
		if (top) //if it's the top box, add it to the world's product list
		{
			ww.getProductList().add(this);
		}
	}
	
	//make a box
	private void loadModel(String num)
	{
		Box b = new Box("My box", new Vector3f(-width/2, 0, -depth/2), new Vector3f(width/2, height, depth/2));
		
		createProductTexture(b,num);
		
		this.attachChild(b);
	}
	
	//creates a product texture based on what type of product it is
	private void createProductTexture(Box b, String str)
	{
		DisplaySystem d = ww.getVirtualWarehouse().getDisplay();
		
		try
		{
			TextureState ts = d.getRenderer().createTextureState(); //make a texture state
			
			File f = getTexture(str); //get the file
			
			//do some JME texture creation
			Texture t = TextureManager.loadTexture(f.toURI().toURL(),Texture.MinificationFilter.BilinearNearestMipMap,Texture.MagnificationFilter.Bilinear);
			
			//set the texture to the texture state
			ts.setTexture(t);
			
			//add the texture to the box
			b.setRenderState(ts);
			
			//update the texture
			b.updateRenderState();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//System.out.println(str);
		}
		finally
		{
			MaterialState ms = d.getRenderer().createMaterialState(); //create a default material state to put on the box
			
			ms.setDiffuse(new ColorRGBA(.294f, .180f, .081f, 1f)); //makes a brownish color (like a cardboard box)

			b.setRenderState(ms); //set the box's material state
			
			b.updateRenderState(); //update it
		}
	}
	
	//gets a random texture to put on the the box based on what it is
	//format:
	//	folder - the product type
	//	files - the textures
	private File getTexture(String productType)
	{
		File file;
		
		if (productType != null) //if the string contains something
		{
			File folder = new File(productTextureLocation+productType+"/"); //get the particular folder
			Object path[] = ridSVN(folder.list()); //obtain the path
			
			file = new File(folder.getPath() + "/" + (String)path[(int)FastMath.floor(FastMath.nextRandomFloat()*path.length)]); //get a random file
		}
		else
		{
			File root = new File(productTextureLocation); //get a random folder
			Object pick[] = ridSVN(root.list()); //obtain its path
			
			File folder = new File(root.getPath() + "/" + (String)pick[(int)FastMath.floor(FastMath.nextRandomFloat()*pick.length)]); //get the particular folder
			Object path[] = ridSVN(folder.list()); //obtains its pth
			
			file = new File(folder.getPath() + "/" + (String)path[(int)FastMath.floor(FastMath.nextRandomFloat()*path.length)]); //get a random file
		}

		return file;
	}
	
	//get rid of the SVN folders from the directory
	private Object[] ridSVN(String[] file)
	{
		ArrayList<String> str = new ArrayList<String>();
		
		for (int i=0;i<file.length;i++)
		{
			if (!file[i].equals(".svn"))
			{
				str.add(file[i]);
			}
		}
		
		return str.toArray();
	}
	
	//simulates a pick and creates a small box (probably eventaully should actually be something other than a dorky box)
	public Pick pickSmallProduct()
	{
		if (pickable)
		{
			//make a small box
			Pick smallBox = new SmallProductBox(ww.getVirtualWarehouse().getSharedNodeManager(), "SmallProduct", this.ww);
			return smallBox;
		}
		return null;
	}

	//get the type of product
	public String getProductType()
	{
		return productType;
	}
	
	//get whether it is pickable
	public boolean isPickable()
	{
		return pickable;
	}
	
	//get the main name
	public String getMainName()
	{
		return name;
	}
	
	//bin number
	public String getBinNumber()
	{
		return binNumber;
	}
}
