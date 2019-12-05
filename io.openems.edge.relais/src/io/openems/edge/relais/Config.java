package io.openems.edge.relais;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(
        name = "Consolinno Relais",
        description = "Relais with a Channel to Open and Close."
)

@interface Config {

    String service_pid();

    @AttributeDefinition(name = "Relais Name", description = "Unique Id of the Relais.")
    String id() default "Relais0";

    @AttributeDefinition(name = "Alias", description = "Human readable name for this Component.")
    String alias() default "";

    @AttributeDefinition(name = "Relais Type", description = "Is the Relais an Opener or closer.",
            options = {
                    @Option(label = "Opener", value = "Opener"),
                    @Option(label = "Closer", value = "Closer")
            })
    String relaisType() default "Closer";

    @AttributeDefinition(name = "RelaisBoard ID", description = "Id of relaisboard allocated to this relais.")
    String relaisBoard_id() default "relaisBoard0";

    @AttributeDefinition(name = "Position", description = "The position of the Relais. Starting with 0.")
    int position() default 0;

    boolean enabled() default true;

    String webconsole_configurationFactory_nameHint() default "Relais [{id}]";
}