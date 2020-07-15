package io.openems.edge.controller.temperature.overseer;

import io.openems.common.exceptions.OpenemsError;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.controller.temperature.passing.api.ControllerPassingChannel;
import io.openems.edge.thermometer.api.Thermometer;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Designate(ocd = Config.class, factory = true)
@Component(name = "Controller.Passing.Overseer")
public class ControllerOverseerImpl extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

    protected ControllerPassingChannel passing;
    protected List<Thermometer> temperatureSensor = new ArrayList<>();
    private int tolerance;
    private long coolDownTime;
    private boolean coolDownTimeSet;

    public ControllerOverseerImpl() {
        super(OpenemsComponent.ChannelId.values(), Controller.ChannelId.values());
    }

    @Reference
    ComponentManager cpm;

    @Activate
    public void activate(ComponentContext context, Config config) throws OpenemsError.OpenemsNamedException, ConfigurationException {
        super.activate(context, config.id(), config.alias(), config.enabled());

        allocateComponents(config.allocated_Passing_Controller(), config.allocated_Temperature_Sensor());

        passing.getMinTemperature().setNextWriteValue(config.min_Temperature());
        this.tolerance = config.tolerated_Temperature_Range();
    }

    @Deactivate
    public void deactivate() {

        super.deactivate();
        try {
            this.passing.getOnOff_PassingController().setNextWriteValue(false);
        } catch (OpenemsError.OpenemsNamedException e) {
            e.printStackTrace();
        }
    }

    private void allocateComponents(String controller, String[] temperatureSensor) throws OpenemsError.OpenemsNamedException, ConfigurationException {
        if (cpm.getComponent(controller) instanceof ControllerPassingChannel) {
            passing = cpm.getComponent(controller);

        } else {
            throw new ConfigurationException(controller,
                    "Allocated Passing Controller not a Passing Controller; Check if Name is correct and try again");
        }
        ConfigurationException[] exConfig = {null};
        OpenemsError.OpenemsNamedException[] exNamed = {null};
        Arrays.stream(temperatureSensor).forEach(thermometer -> {
            try {

                if (cpm.getComponent(thermometer) instanceof Thermometer) {
                    this.temperatureSensor.add(cpm.getComponent(thermometer));
                } else {
                    throw new ConfigurationException(thermometer,
                            "Allocated Temperature Sensor is not Correct; Check Name and try again.");
                }
            } catch (OpenemsError.OpenemsNamedException e) {
                exNamed[0] = e;
            } catch (ConfigurationException e) {
                exConfig[0] = e;
            }

        });
        if (exConfig[0] != null) {
            throw exConfig[0];
        }
        if (exNamed[0] != null) {
            throw exNamed[0];
        }
    }


    /**
     * Activates and Deactivates the PassingController, depending if the Temperature setPoint is reached or not.
     */

    @Override
    public void run() throws OpenemsError.OpenemsNamedException {

        if (passing == null) {
            throw new RuntimeException("The Allocated Passing Controller is not active, please Check.");
        } else if (!heatingReached() && passing.noError().getNextValue().get()) {
            coolDownTimeSet = false;
            this.passing.getOnOff_PassingController().setNextWriteValue(true);
        } else if (heatingReached() && passing.noError().getNextValue().get()) {
            coolDownTimeSet = false;
            this.passing.getOnOff_PassingController().setNextWriteValue(false);
        } else {
            if (!coolDownTimeSet && this.passing.getErrorCode().getNextValue().get() == 2) {
                this.coolDownTime = System.currentTimeMillis();
                coolDownTimeSet = true;
            }
            //After Cooldown set Value to true; Only happens if ErrorCode was 2.
            if (coolDownTimeSet) {
                if (System.currentTimeMillis() - coolDownTime > 30 * 1000) {
                    passing.noError().setNextValue(true);
                }
            }
            throw new OpenemsException("The Passing Controller got an Error! With ErrorCode: "
                    + this.passing.getErrorCode().getNextValue().get());
        }
    }

    /**
     * Checks if the MinTemperature is reached. (comparing with own TemperatureSensor)
     *
     * @return a boolean depending if heat is reached or not.
     */
    private boolean heatingReached() {
        if (passing.getMinTemperature().getNextWriteValue().isPresent()) {
            return this.temperatureSensor.stream().noneMatch(
                    thermometer -> thermometer.getTemperature().getNextValue().get() <= passing.getMinTemperature().getNextWriteValue().get());

        }
        return true;
    }
}
