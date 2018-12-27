package io.openems.edge.bridge.shiftregister.task;


public  class ShiftregisterTask {

	private final int position ; 
	private final boolean isReverse ; 
	private boolean isActive;
	public ShiftregisterTask(int position,boolean isReverse) {
		this.position = position;
		this.isReverse = isReverse;
		if(isReverse)
		{
			isActive=true;
		}
		else
		{
			isActive=false;
		}
	}

	public int getPosition()
	{
		return position;
	}
	public boolean isReverse()
	{
		return isReverse;
	}
	public boolean isActive()
	{
		return isActive;
	}

	public  void activate() {
		isActive=true;
	}
	public  void deactivate() {
		isActive=false;
	}

}
