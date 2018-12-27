package io.openems.edge.actuator.relais.shiftregister;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition( //
		name = "Actuator Relais Shiftregister", //
		description = "Implements a Relais behind a Shiftregister.")
@interface Config {
	String service_pid();

	String id() default "relais0";

	boolean enabled() default true;

	@AttributeDefinition(name = "ShiftRegister-ID", description = "ID of Shiftregister brige.")
	String spi_id();


	@AttributeDefinition(name = "Position", description = "The position. Starting by 0")
	int position();

	@AttributeDefinition(name = "IsOpener", description = "Is the relais an opener or closer")
	boolean isOpener();

	String webconsole_configurationFactory_nameHint() default "Actuator Relais Shiftregister[{id}]";
}