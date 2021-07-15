package io.openems.edge.bridge.mqtt.handler;

import io.openems.common.exceptions.OpenemsError;
import io.openems.edge.bridge.mqtt.api.MqttComponent;
import io.openems.edge.bridge.mqtt.api.MqttType;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import java.io.IOException;

/**
 * This Component provides the MqttTelemetryComponent. It is possible to add ANY Component you can configure to the Config.
 * The Nature/Channel of this Components will be read and wrote into the Config (channelIdList).
 * Now you can configure the Payload way easier and map a Key to a Channel.
 * You can map multiple Payloads to multiple Publish/Subscribe tasks in config.
 * You can also use the same Payload for multiple Publish tasks (makes sense if you want to publish Payloads to multiple
 * Topics).
 */
@Designate(ocd = TelemetryComponentConfig.class, factory = true)
@Component(name = "MqttTelemetryComponent",
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        property = {EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE}
)
public class MqttTelemetryComponent extends MqttOpenemsComponentConnector implements OpenemsComponent, EventHandler {

    @Reference
    ConfigurationAdmin cm;

    @Reference
    ComponentManager cpm;

    TelemetryComponentConfig config;

    public MqttTelemetryComponent() {
        super(OpenemsComponent.ChannelId.values(), MqttComponent.ChannelId.values());
    }


    @Activate
    void activate(ComponentContext context, TelemetryComponentConfig config) throws OpenemsError.OpenemsNamedException, IOException, ConfigurationException, MqttException {
        this.config = config;
        if (super.activate(context, config.id(), config.alias(), config.enabled(), this.cpm, config.mqttBridgeId())) {
            this.configureMqtt(config);
        } else {
            throw new ConfigurationException("Something went wrong", "Somethings wrong in Activate method");
        }
    }

    @Modified
    void modified(ComponentContext context, TelemetryComponentConfig config) throws OpenemsError.OpenemsNamedException, IOException, ConfigurationException, MqttException {
        this.config = config;
        super.connectorDeactivate();
        if (super.modified(context, config.id(), config.alias(), config.enabled(), this.cpm, config.mqttBridgeId())) {
            this.configureMqtt(config);
        }
    }


    /**
     * Configures the telemetry Component with the given Config -> Ready to send/receive data via MQTT.
     *
     * @param config the Config.
     * @throws MqttException                      thrown if subscription fails.
     * @throws ConfigurationException             if the config has an error.
     * @throws IOException                        if the json file could not be found.
     * @throws OpenemsError.OpenemsNamedException if the bridge with given Id couldn't be found.
     */
    private void configureMqtt(TelemetryComponentConfig config) throws MqttException, ConfigurationException, IOException, OpenemsError.OpenemsNamedException {
        super.setCorrespondingComponent(config.otherComponentId(), this.cpm);

        super.setConfiguration(MqttType.TELEMETRY, config.subscriptionList(), config.publishList(),
                config.payloads(), config.createdByOsgi(), config.mqttId(), this.cm, config.channelIdList().length,
                config.pathForJson(), config.payloadStyle(), config.configurationDone());
    }

    @Deactivate
    protected void deactivate() {
        super.connectorDeactivate();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getTopic().equals(EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE)) {
            if (this.mqttBridge.get() != null && this.mqttBridge.get().isEnabled() && this.mqttConfigurationComponent == null) {
                this.mqttBridge.get().removeMqttComponent(this.id());
                try {
                    super.setConfiguration(MqttType.TELEMETRY, this.config.subscriptionList(), this.config.publishList(),
                            this.config.payloads(), this.config.createdByOsgi(), this.config.mqttId(), this.cm, this.config.channelIdList().length,
                            this.config.pathForJson(), this.config.payloadStyle(), this.config.configurationDone());
                } catch (IOException | MqttException | ConfigurationException e) {
                    super.log.warn("Couldn't apply config for this mqttComponent");
                }
            }
        }
    }
}