package io.openems.edge.bridge.shiftregister;

import io.openems.edge.bridge.shiftregister.task.ShiftregisterTask;

public interface BridgeShiftregister {

	public void addTask(String sourceId, ShiftregisterTask task);

	public void removeTask(String sourceId);
	
}
