package io.openems.edge.bridge.shiftregister;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.openems.edge.bridge.shiftregister.BridgeShiftregisterImpl;

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
		BridgeShiftregisterImpl impl = new BridgeShiftregisterImpl();
		assertNotNull(impl);
	}

}
