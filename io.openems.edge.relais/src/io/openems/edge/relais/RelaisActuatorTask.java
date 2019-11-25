package io.openems.edge.relais;

import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.relaisboardapi.task.McpTask;
import io.openems.edge.relaisboardapi.Mcp;


public class RelaisActuatorTask extends McpTask {
    private int position;
    private WriteChannel<Boolean> writeOnOrOff;
    private boolean active = false;
    private boolean reverse;
    private Mcp register;

    public RelaisActuatorTask(Mcp register, int position, boolean isOpener, WriteChannel<Boolean> writeOnOrOff, String relaisBoard) {
        super(relaisBoard);
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

    //No Usage here, just for the Gaspedal Stuff
    @Override
    public WriteChannel<Integer> getPowerLevel() {
        return null;
    }


    @Override
    public int getDigitValue() {
        return -666;
    }
}
