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

public class Product extends Node
{
	private String productType;
	private String binNumber;
	private WarehouseWorld ww;
	private boolean pickable;
	
	private float width = .39f;
	private float depth = .39f;
	private float height = .27f;
	
	private final static String productTextureLocation = "src/data/productTextures/";
	
	public Product(WarehouseWorld ww)
	{
		this(ww,null,null,false,null);
	}

	public Product(WarehouseWorld ww, String binNumber, String name, boolean top, String productType)
	{
		this.ww = ww;
		this.setName(name);
		
		this.binNumber = binNumber;
		
		this.productType = productType;
		
		loadModel(productType);
		pickable = top;
		
		if (top)
		{
			ww.getProductList().add(this);
		}
	}
	
	private void loadModel(String num)
	{
		Box b = new Box("My box", new Vector3f(-width/2, 0, -depth/2), new Vector3f(width/2, height, depth/2));
		
		createProductTexture(b,num);
		
		this.attachChild(b);
	}
	
	private void createProductTexture(Box b, String str)
	{
		DisplaySystem d = ww.getVirtualWarehouse().getDisplay();
		
		try
		{
			TextureState ts = d.getRenderer().createTextureState();
			
			File f = getTexture(str);
			
			Texture t = TextureManager.loadTexture(f.toURI().toURL(),Texture.MinificationFilter.BilinearNearestMipMap,Texture.MagnificationFilter.Bilinear);
			
			ts.setTexture(t);
			
			b.setRenderState(ts);
			
			b.updateRenderState();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//System.out.println(str);
		}
		finally
		{
			MaterialState ms = d.getRenderer().createMaterialState();
			
			ms.setDiffuse(new ColorRGBA(.294f, .180f, .081f, 1f));

			b.setRenderState(ms);
			
			b.updateRenderState();
		}
	}
	
	private File getTexture(String productType)
	{
		File file;
		
		if (productType != null)
		{
			File folder = new File(productTextureLocation+productType+"/");
			Object path[] = ridSVN(folder.list());
			
			file = new File(folder.getPath() + "/" + (String)path[(int)FastMath.floor(FastMath.nextRandomFloat()*path.length)]);
		}
		else
		{
			File root = new File(productTextureLocation);
			Object pick[] = ridSVN(root.list());
			
			File folder = new File(root.getPath() + "/" + (String)pick[(int)FastMath.floor(FastMath.nextRandomFloat()*pick.length)]);
			Object path[] = ridSVN(folder.list());
			
			file = new File(folder.getPath() + "/" + (String)path[(int)FastMath.floor(FastMath.nextRandomFloat()*path.length)]);
		}

		return file;
	}
	
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
	
	public Pick pickSmallProduct()
	{
		if (pickable)
		{
			Pick smallBox = new SmallProductBox(ww.getVirtualWarehouse().getSharedNodeManager(), "SmallProduct", this.ww);
			return smallBox;
		}
		return null;
	}

	public String getProductType()
	{
		return productType;
	}
	
	public boolean isPickable()
	{
		return pickable;
	}
	
	public String getMainName()
	{
		return name;
	}
	
	public String getBinNumber()
	{
		return binNumber;
	}
}
