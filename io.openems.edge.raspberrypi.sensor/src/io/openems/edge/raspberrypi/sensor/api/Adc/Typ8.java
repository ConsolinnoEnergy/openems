package io.openems.edge.raspberrypi.sensor.api.Adc;

import io.openems.edge.raspberrypi.sensor.api.Adc.Pins.Pin;
import io.openems.edge.raspberrypi.sensor.api.Board;

import java.util.List;

public abstract class Typ8 extends Adc {

    protected Typ8(List<Long> pins, int inputType ,Board board, int id, int SpiChannel) {
        super(pins, inputType ,(byte)8, board, id, SpiChannel );
    }
}
