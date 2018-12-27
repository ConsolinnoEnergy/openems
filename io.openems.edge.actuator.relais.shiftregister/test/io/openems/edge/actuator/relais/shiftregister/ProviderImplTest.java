package io.openems.edge.actuator.relais.shiftregister;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.openems.edge.actuator.relais.shiftregister.ShiftregisterRelaisActuator;

/*
 * Example JUNit test case
 *
 */

public class ProviderImplTest {

	/*
	 * Example test method
	 */

	@Test
	public void simple() {
		ShiftregisterRelaisActuator impl = new ShiftregisterRelaisActuator();
		assertNotNull(impl);
	}

}
