package io.openems.edge.raspberrypi.sensors.temperaturesensor;


import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.internal.AbstractReadChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.raspberrypi.circuitboard.CircuitBoard;
import io.openems.edge.raspberrypi.circuitboard.api.adc.Adc;
import io.openems.edge.raspberrypi.circuitboard.api.adc.pins.Pin;
import io.openems.edge.raspberrypi.sensors.task.TemperatureDigitalReadTask;
import io.openems.edge.raspberrypi.spi.SpiInitial;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventConstants;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Temperature.Sensor", immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE)

public class TemperatureSensor extends AbstractOpenemsComponent implements OpenemsComponent, TemperatureSensoric {
    @Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
    private SpiInitial spiInitial;
    @Reference
    private ConfigurationAdmin cm;

    private String id;
    private String circuitBoardId;
    private String versionId;
    private int spiChannel;
    private int pinPosition;
    private String servicePid;
    private final String sensorType = "Temperature";
    private Adc adcForTemperature;
    private final Logger log = LoggerFactory.getLogger(TemperatureSensor.class);


    public TemperatureSensor() {
        super(OpenemsComponent.ChannelId.values(), TemperatureSensoric.ChannelId.values());

        //this.addChannels(TemperatureSensoric.ChannelId.values());
        //Stream<? extends AbstractReadChannel<?, ?>> stream = Utils.initializeChannels(this);
        //stream.forEach(channel -> super.addChannel(channel.channelId()));

    }


    @Activate
    public void activate(ComponentContext context, Config config) throws ConfigurationException {
        super.activate(context, config.id(), config.alias(), config.enabled());
        // if (OpenemsComponent.updateReferenceFilter(cm, config.service_pid(), "SpiInitial", config.spiInitial_id())) {
        //   return;
        //}
        //this.addChannels(TemperatureSensoric.ChannelId.values());
        this.id = config.id();
        this.circuitBoardId = config.circuitBoardId();
        this.spiChannel = config.spiChannel();
        this.pinPosition = config.pinPosition();

        for (CircuitBoard fromConsolinno : spiInitial.getCircuitBoards()) {
            if (fromConsolinno.getCircuitBoardId().equals(this.circuitBoardId)) {
                this.versionId = fromConsolinno.getVersionId();
                for (Adc adc : fromConsolinno.getAdcList()
                ) {
                    if (adc.getSpiChannel() == this.spiChannel) {
                        adcForTemperature = adc;
                        if (adc.getPins().get(this.pinPosition) != null) {
                            Optional<Pin> opt = adc.getPins().stream().filter(pin -> pin.getPosition() == this.pinPosition).findFirst();
                            if (opt.isPresent()) {
                                Pin wantToUse = opt.get();
                                if (wantToUse.isUsed() && !wantToUse.getUsedBy().equals(this.id)) {
                                    throw new ConfigurationException(
                                            "Pin is already used", "Pin is already used by "
                                            + wantToUse.getUsedBy());
                                } else {
                                    spiInitial.addTask(this.id, new TemperatureDigitalReadTask(getTemperature(),
                                            this.versionId, adcForTemperature, this.pinPosition));
                                    wantToUse.setUsedBy(this.id);
                                    return;
                                }
                            }
//                            Pin wantToUse = adc.getPins().get(this.pinPosition);
//                            if (wantToUse.isUsed() && !wantToUse.getUsedBy().equals(this.id)) {
//                                throw new ConfigurationException(
//                                        "Pin is already used", "Pin is already used by "
//                                        + wantToUse.getUsedBy());
//                            } else {
//                                spiInitial.addTask(this.id, new TemperatureDigitalReadTask(getTemperature(),
//                                        this.versionId, adcForTemperature, this.pinPosition));
//                                wantToUse.setUsedBy(this.id);
//                                return;
//                            }
                        } else {
                            throw new ConfigurationException("Wrong Pin",
                                    "The PinPosition" + this.pinPosition + "couldn't be found on the Adc");
                        }
                    } else {
                        throw new ConfigurationException("Wrong SpiChannel", "SpiChannel was wrong");
                    }
                }
            } else {
                throw new ConfigurationException("Wrong CircuitBoard ID", "CircuitBoard id was wrong");
            }
        }
    }

    public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
        ;
        private final Doc doc;

        private ChannelId(Doc doc) {
            this.doc = doc;
        }

        public Doc doc() {
            return this.doc;
        }
    }


    @Deactivate
    public void deactivate() {
        spiInitial.removeTask(this.id);
        adcForTemperature.getPins().get(this.pinPosition).setUsed(false);
        super.deactivate();
    }

    @Override
    public String debugLog() {
        return "T:" + this.getTemperature().value().asString();
    }

}
