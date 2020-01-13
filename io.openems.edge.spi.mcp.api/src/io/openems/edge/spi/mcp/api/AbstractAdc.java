package io.openems.edge.spi.mcp.api;

import com.pi4j.wiringpi.Spi;
import io.openems.edge.spi.mcp.api.pins.PinImpl;
import io.openems.edge.spi.mcp.api.pins.Pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractAdc implements Adc {
    private List<Pin> pins = new ArrayList<>();
    private int spiChannel;
    private int inputType;
    private byte maxSize;
    private String circuitBoardId;
    private boolean initialized = false;
    private String versionId;

    public AbstractAdc(List<Long> pins, int inputType, byte maxSize) {
        int position = 0;
        for (long l : pins) {
            this.pins.add(new PinImpl(l, position++));
        }
        this.inputType = inputType;
        this.maxSize = maxSize;
    }

    /**
     * initialize the Mcp.
     *
     * @param circuitBoardId unique id of the tempereature module.
     * @param frequency      frequency set by user.
     * @param spiChannel     spiChannel set by user (== dipswitch)
     * @param versionId      the version of the temperature-module.
     */
    @Override
    public void initialize(int spiChannel, int frequency, String circuitBoardId, String versionId) {
        if (!initialized) {
            this.circuitBoardId = circuitBoardId;
            this.spiChannel = spiChannel;
            this.initialized = true;
            this.versionId = versionId;

            Spi.wiringPiSPISetup(spiChannel, frequency);
        }
    }

    @Override
    public List<Pin> getPins() {
        return pins;
    }

    @Override
    public int getSpiChannel() {
        if (!initialized) {
            return -1;
        }
        return spiChannel;
    }

    @Override
    public int getInputType() {
        return inputType;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public byte getMaxSize() {
        return maxSize;
    }

    @Override
    public String getCircuitBoardId() {
        if (!initialized) {
            return "not initialized yet";
        }
        return circuitBoardId;
    }

    @Override
    public void deactivate() {
        //Nothing for temperature Purposes bc it's just reading data.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAdc adc = (AbstractAdc) o;
        return spiChannel == adc.spiChannel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(spiChannel);
    }

    @Override
    public String getVersionId() {
        return this.versionId;
    }
}
