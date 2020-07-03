package io.openems.edge.consolinno.leaflet.maindevice.doubleuart;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(
        name = "Consolinno LeafLet MainModule Sc16 Device",
        description = "The Connected Device on the Sc16."
)
@interface Config {
    String service_pid();

    @AttributeDefinition(name = "Sc16 Device Id", description = "Unique Id of the Sc16 Device.")
    String id() default "GPIO0";

    @AttributeDefinition(name = "Alias", description = "Human readable name for this Component.")
    String alias() default "";

    @AttributeDefinition(name = "Gpio Information Type.", description = "Information Type of gpio.",
            options = {
                    @Option(label = "Error", value = "Error"),
                    @Option(label = "OnOff", value = "OnOff")
            })
    String informationType() default "OnOff";

    @AttributeDefinition(name = "GPIO ", description = "What GPIO u want to call (0-7) is possible.")
    int pinPosition() default 0;

    @AttributeDefinition(name = "Spi Channel", description = "Channel Address of DoubleUART")
    int spiChannel() default 8;

    boolean enabled() default true;

    String webconsole_configurationFactory_nameHint() default "DoubleUART Device [{id}]";

}