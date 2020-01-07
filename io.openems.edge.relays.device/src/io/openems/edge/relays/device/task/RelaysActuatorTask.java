package io.openems.edge.relays.device.task;

import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.i2c.mcp.api.Mcp;
import io.openems.edge.i2c.mcp.api.task.McpTask;


public class RelaysActuatorTask extends McpTask {
    private int position;
    private WriteChannel<Boolean> writeOnOrOff;
    private boolean active = false;
    private boolean reverse;
    private Mcp register;

    public RelaysActuatorTask(Mcp register, int position, boolean isOpener, WriteChannel<Boolean> writeOnOrOff, String relaysBoard) {
        super(relaysBoard);
        this.position = position;
        this.reverse = isOpener;
        this.writeOnOrOff = writeOnOrOff;
        this.register = register;
        if (reverse) {
            active = true;
        }
    }

    @Override
    public WriteChannel<Boolean> getWriteChannel() {
        return this.writeOnOrOff;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    //No Usage here, just for the ChpModule
    @Override
    public WriteChannel<Integer> getPowerLevel() {
        return null;
    }

    //Same here
    @Override
    public int getDigitValue() {
        return -666;
    }
}
