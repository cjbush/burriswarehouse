package code.model.player;

import com.jme.math.FastMath;

/**
 * The RandomPerson contains constants for generating random textures within the Player model
 * The model is the same with all NPCs, but the textures applied to them are different
 * As well as the heights and weights of the NPCs.
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class RandomPerson 
{
	
	//The numTextures are these:
	
	//ForeArm1:  the arms
	//Hands_01:  the hands
	//Head_M-B:  the face and neck
	//Shirt-D:   the shirt
	//Shoe_Tr1:  the shoes
	//Trs_DesB:  the pants
	
	//These are all folders within the player class.  In these folders contains the 26 different variations that can be applied to the NPC.
	
	//these textures are all numbered from 1 to the number of variations (right now 26)  They are in the format of:
	
	//001
	//002
	//003
	//etc.
	
	//Where there are three zeroes (place holders) within the file.
	
	private static final int numTextures = 6; //the number of different kinds of textures within the model
	private static final int variations = 26; //the variations of numTextures (so there are 26*6 different textures)
	private static final int zeros = 3; //how many zeroes to look for in the string
	
	public static final String raceTextures[] = {"ForeArm1.bmp","Hands_01.bmp","Head_M-B.bmp"}; //the textures that make a man black or white
	
	private static final float heightFactor = .05f; //how much to change the height between the NPCs
	private static final float weightFactor = .15f; //how much to change the weight between the NPCs
	
	private String textures[] = new String[numTextures]; //create the different strings
	private Integer count = 0; //which texture that it is currently on
	
	public float height; //the height of the character
	public float weight; //the weight of the character
	
	public Boolean isWhite; //Whether the character is black or white
	
	public RandomPerson()
	{
		for (int i=0;i<numTextures;i++) //for each folder
		{
			int num = FastMath.nextRandomInt(1,variations); //get a random number between one and the variations
			textures[i] = ""; //initialize the string
			textures[i] += zeroes(num) + num; //create the string
		}
		
		height = (1-heightFactor/2)+FastMath.nextRandomFloat()*heightFactor*2; //create the height of the NPC
		weight = (1-weightFactor/2)+FastMath.nextRandomFloat()*weightFactor*2; //create the weight of the NPC
		
		isWhite = FastMath.nextRandomInt(0,1) == 1; //whether the NPC is black or white
	}
	
	//get the particular texture
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
	
	//create the number of zeroes needed in the string
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
	
	//get the File name of the file (stripping the file path and keeping the extension)
	private String getActualFileName(String path)
	{
		int i = path.lastIndexOf("/");
		return path.substring(i+1);
	}
	
	//sets the texture
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
	
	//renames the texture with the old name (obtained from the MD5 files) and returns the new random file name
	public String renameTexture(String oldTexture)
	{
		raceCheck(getActualFileName(oldTexture)); //sets the correct texture based on race
		
		int i = oldTexture.lastIndexOf("."); //strip the file name of its extension and obtain the number
		String extension = oldTexture.substring(i); //create the extension of the new string
		String newTexture = oldTexture.substring(0,i) + "/"; //create the file name of the new string
		newTexture += getTexture(count) + extension; //combine the two strings
		setTexture(count++,newTexture); //set the texture
		
		return newTexture; //return the newly created texture
	}
	
	//this function sets the textures up based on the race
	//the first variations/2 (13) textures in the folders are for white people
	//the second variations/2 (13) textures in the folders are for black people
	private void raceCheck(String tex)
	{
		for (int i=0;i<raceTextures.length;i++) //for how many textures are racially explicit
		{
			if (tex.equals(raceTextures[i])) //if the texture is equal to the particular race
			{
				int texNum = Integer.parseInt(getTexture(count)); //get the number of the texture
				int var = variations/2; //split the variations of the races
				
				if (isWhite) //if the person is white
				{
					if (texNum > var) //and the number obtained randomly is in the black section
					{
						texNum -= var; //subtract the offset from the random number
						tex = ""; //reinitialize the string
						tex += zeroes(texNum) + texNum; //create the new string
						setTexture(count,tex); //set the texture
					}
				}
				else //if the person is black
				{
					if (texNum <= var) //and the number obtained randomly is in the white section
					{
						texNum += var; //add the offset from the random number
						tex = ""; //reinitialize the string
						tex += zeroes(texNum) + texNum; //create the new string
						setTexture(count,tex); //set the texture
					}
				}
				break;
			}
		}
	}
}
