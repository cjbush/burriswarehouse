package code.model.player;

import com.jme.math.FastMath;

public class RandomPerson 
{
	private static final int numTextures = 6;
	private static final int variations = 2;
	private static final int zeros = 3;
	
	private String textures[] = new String[numTextures];
	private Integer count = 0;
	
	public RandomPerson()
	{
		for (int i=0;i<numTextures;i++)
		{
			int num = FastMath.nextRandomInt(1,variations);
			textures[i] = "";
			for (int j=zeros;j>1;j--)
			{
				if (num < FastMath.pow(10,j-1))
				{
					textures[i] += "0";
				}
			}
			textures[i] += num;
		}
	}
	
	public String getTexture(int i)
	{
		try
		{
			return textures[i];
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void setTexture(int i, String s)
	{
		try
		{
			textures[i] = s;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String renameTexture(String oldTexture)
	{
		int i = oldTexture.lastIndexOf(".");
		String extension = oldTexture.substring(i);
		String newTexture = oldTexture.substring(0,i) + "/";
		newTexture += getTexture(count) + extension;
		setTexture(count++,newTexture);
		return newTexture;
	}
}
