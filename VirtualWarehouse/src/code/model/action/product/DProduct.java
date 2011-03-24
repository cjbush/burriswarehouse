package code.model.action.product;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

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

public class DProduct extends Node
{
	private String binNumber;
	private WarehouseWorld ww;
	private boolean pickable;
	
	private float width = .39f;
	private float depth = .39f;
	private float height = .27f;
	
	private final static String productTextureLocation = "D:/Dan Jewett/Docstoc/Senior Design/VirtualWarehouse/src/data/productTextures/";
	
	public DProduct(WarehouseWorld ww)
	{
		this(ww,null,null,false);
	}

	public DProduct(WarehouseWorld ww, String binNumber, String name, boolean top)
	{
		this.ww = ww;
		this.setName(name);
		this.binNumber = binNumber;
		loadModel(binNumber);
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
	
	private void createProductTexture(Box b, String num)
	{
		try
		{
			DisplaySystem d = ww.getVirtualWarehouse().getDisplay();
			
			TextureState ts = d.getRenderer().createTextureState();
			
			File f = getTexture(num);
			
			Texture t = TextureManager.loadTexture(f.toURI().toURL(),Texture.MinificationFilter.BilinearNearestMipMap,Texture.MagnificationFilter.Bilinear);
			
			ts.setTexture(t);
			
			MaterialState ms = d.getRenderer().createMaterialState();
			
			ms.setDiffuse(new ColorRGBA(.294f, .180f, .081f, 1f));

			b.setRenderState(ms);
			
			b.updateRenderState();
			
			b.setRenderState(ts);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private File getTexture(String binNumber)
	{
		File file;
		if (binNumber != null)
		{
			File folder = new File(productTextureLocation+binNumber+"/");
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

	public String getBinNumber()
	{
		return binNumber;
	}
	
	public boolean isPickable()
	{
		return pickable;
	}
	
	public String getMainName()
	{
		return name;
	}
}
