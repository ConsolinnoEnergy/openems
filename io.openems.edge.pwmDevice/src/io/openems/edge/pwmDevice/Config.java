package io.openems.edge.pwmDevice;

import com.sun.corba.se.spi.ior.IdentifiableFactory;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(
        name = "Consolinno Pwm Device",
        description = "Module for Pulse widening modulation"
)
@interface Config {

    String service_pid();

    @AttributeDefinition(name = "Pwm Device Name", description = "")
    String id() default "PwmDevice0";

    @AttributeDefinition(name = "Alias", description = "Human readable name for this Component.")
    String alias() default "";

    @AttributeDefinition(name = "Pwm Module Name", description = "Name of the allocated Pwm Module")
    String pwm_module() default "Pwm0";

    @AttributeDefinition(name = "I2C Bridge - ID", description = "ID of I2C Bridge - ID.")
    String i2c_id() default "I2C0";

    @AttributeDefinition(name = "Pin Position", description = "What Channelinput you are using with this PWM Device (0-7)")
    short pinPosition() default 0;

//    @AttributeDefinition(name = "pulseDuration", description = "pulseDuration of Pwm Module")
//    int pwm_pulseDuration()default 600;

    @AttributeDefinition(name = "Is Inverse", description = "If the Device is powered at a low Flank set true")
    boolean isInverse() default false;

    @AttributeDefinition(name = "Initial Value", description = "Percentage of the High Value of the Device")
            float percentage_Initial()default 100;

    boolean enabled() default true;

    String webconsole_configurationFactory_nameHint() default "Pwm Device [{id}]";

}
