package io.openems.edge.bridge.shiftregister;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;



@ObjectClassDefinition( //
		name = "Bridge Shiftregister for Raspberry", //
		description = "Provides a service for writing to a shiftregister.")
@interface Config {
	String service_pid();

	String id() default "shift0";

	@AttributeDefinition(name = "RCLK", description = "Clock for latching the data.")
	PinRaspberry rclk();
	@AttributeDefinition(name = "CLK", description = "Clock for data.")
	PinRaspberry clk();
	@AttributeDefinition(name = "SER", description = "data line.")
	PinRaspberry ser();
	
	int amountChannels() default 8;
	boolean enabled() default true;

	String webconsole_configurationFactory_nameHint() default "Shiftregister Raspberry [{id}]";
}