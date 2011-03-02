package code.util;

import com.jme.math.FastMath;

public class DUtility
{
	private Float rotationFactor;
	private Float scaleFactor;
	private Float transFactor;
	
	private Float h;
	
	public DUtility(Float rotFactor, Float scaFactor, Float traFactor, Float h)
	{
		this.rotationFactor = rotFactor;
		this.scaleFactor = scaFactor;
		this.transFactor = traFactor;
		this.h = h;
	}
	
	public float scale()
	{
		return (1-scaleFactor/2)+FastMath.nextRandomFloat()*scaleFactor*2;
	}
	
	public float translation()
	{
		return -(transFactor/2)+FastMath.nextRandomFloat()*transFactor*2;
	}
	
	public float rotation()
	{
		return -(rotationFactor/2) + (FastMath.nextRandomFloat()*rotationFactor);
	}
	
	public float getRotationFactor()
	{
		return rotationFactor;
	}
	
	public float getTranslationFactor()
	{
		return transFactor;
	}
	
	public float getScaleFactor()
	{
		return scaleFactor;
	}
	
	public float getH()
	{
		return h;
	}
}
