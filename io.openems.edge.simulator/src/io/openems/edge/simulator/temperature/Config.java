package io.openems.edge.simulator.temperature;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(//
        name = "Simulator Temperature", //
        description = "This simulates a the Temperature Change if the Controller Passing and Overseer are active.")
@interface Config {

    @AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
    String id() default "TemperatureSensor0";

    @AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
    String alias() default "";

    @AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
    boolean enabled() default true;

    @AttributeDefinition(name = "Datasource-ID", description = "ID of Simulator Datasource.",
            options = {
                    @Option(label = "primary Forward", value = "primary Forward"),
                    @Option(label = "primary Rewind", value = "primary Rewind"),
                    @Option(label = "secundary Forward", value = "secundary Forward"),
                    @Option(label = "secundary Rewind", value = "secundary Rewind"),
                    @Option(label = "Overseer", value = "Overseer")
            })
    String datasource_id() default "primary Forward";

    @AttributeDefinition(name = "Datasource target filter", description = "This is auto-generated by 'Datasource-ID'.")
    String datasource_target() default "";

    @AttributeDefinition(name = "Temperature Sensor", description = "Temperature Sensor Id want to be used for .")
            String sensor() default "TemperatureSensor0";

    String webconsole_configurationFactory_nameHint() default "Simulator Tenoerature-Passing [{id}]";

}