package code.model.player;

import com.jme.math.FastMath;

public class RandomPerson 
{
	private static final int numTextures = 6;
	private static final int variations = 26;
	private static final int zeros = 3;
	
	public static final String raceTextures[] = {"ForeArm1.bmp","Hands_01.bmp","Head_M-B.bmp"};
	
	private static final float heightFactor = .10f;
	private static final float weightFactor = .2f;
	
	private String textures[] = new String[numTextures];
	private Integer count = 0;
	
	public float height;
	public float weight;
	
	public Boolean isWhite;
	
	public RandomPerson()
	{
		for (int i=0;i<numTextures;i++)
		{
			int num = FastMath.nextRandomInt(1,variations);
			textures[i] = "";
			textures[i] += zeroes(num) + num;
		}
		
		height = (1-heightFactor/2)+FastMath.nextRandomFloat()*heightFactor*2;
		weight = (1-weightFactor/2)+FastMath.nextRandomFloat()*weightFactor*2;
		
		isWhite = FastMath.nextRandomInt(0,1) == 1;
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
	
	private String zeroes(int num)
	{
		String s = "";
		
		for (int j=zeros;j>1;j--)
		{
			if (num < FastMath.pow(10,j-1))
			{
				s += "0";
			}
		}
		return s;
	}
	
	private String getActualFileName(String path)
	{
		int i = path.lastIndexOf("/");
		return path.substring(i+1);
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
		raceCheck(getActualFileName(oldTexture));
		
		int i = oldTexture.lastIndexOf(".");
		String extension = oldTexture.substring(i);
		String newTexture = oldTexture.substring(0,i) + "/";
		newTexture += getTexture(count) + extension;
		setTexture(count++,newTexture);
		
		return newTexture;
	}
	
	private void raceCheck(String tex)
	{
		for (int i=0;i<raceTextures.length;i++)
		{
			if (tex.equals(raceTextures[i]))
			{
				int texNum = Integer.parseInt(getTexture(count));
				int var = variations/2;
				
				if (isWhite)
				{
					if (texNum > var)
					{
						texNum -= var;
						tex = "";
						tex += zeroes(texNum) + texNum;
						setTexture(count,tex);
					}
				}
				else
				{
					if (texNum <= var)
					{
						texNum += var;
						tex = "";
						tex += zeroes(texNum) + texNum;
						setTexture(count,tex);
					}
				}
				break;
			}
		}
	}
}
