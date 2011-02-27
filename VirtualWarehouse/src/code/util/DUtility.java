package code.util;

import com.jme.math.FastMath;

public class DUtility
{
	private Float rotationAngle;
	private Float rotationFactor;
	private Float scaleFactor;
	private Float transFactor;
	
	private Float h;
	
	public DUtility(Float scaFactor, Float traFactor)
	{
		this(null, null, scaFactor, traFactor, null);
	}
	
	public DUtility(Float scaFactor, Float traFactor, Float h)
	{
		this(null, null, scaFactor, traFactor, h);
	}
	
	public DUtility(Float rotFactor, Float scaFactor, Float traFactor, Float h)
	{
		this(null, rotFactor, scaFactor, traFactor, h);
	}
	
	public DUtility(Float rotAngle, Float rotFactor, Float scaFactor, Float traFactor, Float h)
	{
		this.rotationAngle = rotAngle;
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
		return rotationAngle * ((rotationFactor/2)+FastMath.nextRandomFloat()*rotationFactor*2);
	}
	
	public float getRotationAngle()
	{
		return rotationAngle;
	}
	
	public float getRotationFactor()
	{
		return rotationFactor;
	}
	
	public float getTranslationFactor()
	{
		return rotationAngle;
	}
	
	public float getScaleFactor()
	{
		return rotationAngle;
	}
	
	public float getH()
	{
		return h;
	}
}
