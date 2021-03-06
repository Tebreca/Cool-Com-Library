package com.ccl.args;

import com.ccl.enumerations.ParamType;

public abstract class Argument
{

	private final String argName;
	private final ParamType type;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	private boolean hasRange = false;
	
	public Argument(String argName, ParamType type)
	{
		this.argName = argName;
		this.type = type;
	}
	
	public Argument()
	{
		this.argName = null;
		this.type = null;
	}

	public ParamType getType()
	{
		return type;
	}
	
	public Argument setRange(int min, int max)
	{
		this.min = min;
		this.max = max;
		
		if(min != Integer.MIN_VALUE || max != Integer.MAX_VALUE)
			this.hasRange = true;
		
		return this;
	}

	public int getMin()
	{
		return min;
	}

	public int getMax()
	{
		return max;
	}

	public String getName()
	{
		return argName;
	}

	public boolean hasRange()
	{
		return hasRange;
	}
}
