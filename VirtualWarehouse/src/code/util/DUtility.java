package code.util;

import com.jme.math.FastMath;

/**
 * A utility made by me for making randomness within the whole:
 * 
 * Rack->StackedPallets->Pallets->StackedProducts->Product combination
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class DUtility
{
	private Float rotationFactor; //a rotation number in degrees (from -rot to rot)
	private Float scaleFactor; //a scale number in units (from -sca to sca)
	private Float transFactor; //a translation number in units (from -trans to trans)
	
	private Float h; //a height to offset stacks
	
	public DUtility(Float rotFactor, Float scaFactor, Float traFactor, Float h)
	{
		this.rotationFactor = rotFactor;
		this.scaleFactor = scaFactor;
		this.transFactor = traFactor;
		this.h = h;
	}
	
	//get a random scale number
	public float scale()
	{
		return (1-scaleFactor/2)+FastMath.nextRandomFloat()*scaleFactor*2;
	}
	
	//get a random translation number
	public float translation()
	{
		return -(transFactor/2)+FastMath.nextRandomFloat()*transFactor*2;
	}
	
	//get a random rotation number
	public float rotation()
	{
		return -(rotationFactor/2) + (FastMath.nextRandomFloat()*rotationFactor);
	}
	
	//get the rot factor
	public float getRotationFactor()
	{
		return rotationFactor;
	}
	
	//get the trans factor
	public float getTranslationFactor()
	{
		return transFactor;
	}
	
	//get the sca factor
	public float getScaleFactor()
	{
		return scaleFactor;
	}
	
	//get the height offset
	public float getH()
	{
		return h;
	}
}
