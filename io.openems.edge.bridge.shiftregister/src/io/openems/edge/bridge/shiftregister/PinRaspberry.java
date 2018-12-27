package io.openems.edge.bridge.shiftregister;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum PinRaspberry {

	WIRING_00(RaspiPin.GPIO_00), //
	WIRING_01(RaspiPin.GPIO_01), //
	WIRING_02(RaspiPin.GPIO_02), //
	WIRING_03(RaspiPin.GPIO_03), //
	WIRING_04(RaspiPin.GPIO_04), //
	WIRING_05(RaspiPin.GPIO_05), //
	WIRING_06(RaspiPin.GPIO_06), //
	WIRING_07(RaspiPin.GPIO_07), //
	WIRING_21(RaspiPin.GPIO_21), //
	WIRING_22(RaspiPin.GPIO_22), //
	WIRING_23(RaspiPin.GPIO_23), //
	WIRING_24(RaspiPin.GPIO_24), //
	WIRING_25(RaspiPin.GPIO_25), //
	WIRING_26(RaspiPin.GPIO_26), //
	WIRING_27(RaspiPin.GPIO_27), //
	WIRING_28(RaspiPin.GPIO_28), //
	WIRING_29(RaspiPin.GPIO_29); //

	private final Pin pin;

	private PinRaspberry(Pin pin) {
		
		this.pin = pin;
	}

	public Pin getPin() {
		return pin;
	}
	
}
