package io.openems.edge.bridge.shiftregister.task;



import io.openems.edge.common.channel.BooleanWriteChannel;

public  class ShiftregisterTask {

	private final int position ; 
	private final boolean isReverse ; 
	private final BooleanWriteChannel relais;
	public ShiftregisterTask(int position,boolean isReverse, BooleanWriteChannel channel) {
		this.position = position;
		this.isReverse = isReverse;
		this.relais=channel;
		this.relais.setNextValue(false);
	}

	public int getPosition()
	{
		return position;
	}
	public BooleanWriteChannel getChannel()
	{
		return relais;
	}
	public boolean isReverse()
	{
		return isReverse;
	}

	public boolean isActive()
	{
		if(this.relais.value().get() != null)
		{
			return this.relais.value().get();			
		}
		else 
			{return false;}
	}


}
