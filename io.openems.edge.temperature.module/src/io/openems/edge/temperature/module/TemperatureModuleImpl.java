package io.openems.edge.temperature.module;

import io.openems.edge.bridge.spi.api.BridgeSpi;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.spi.mcp.api.Adc;
import io.openems.edge.temperature.module.api.TemperatureModule;
import io.openems.edge.temperature.module.api.TemperatureModuleVersions;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Designate(ocd = Config.class, factory = true)
@Component(name = "Module.Temperature", immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE)

public class TemperatureModuleImpl extends AbstractOpenemsComponent implements OpenemsComponent, TemperatureModule {

    @Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
    BridgeSpi bridgeSpi;

    private String circuitBoardId;
    private String versionId;
    private Set<Adc> adcSet = new HashSet<>();

    public TemperatureModuleImpl() {
        super(OpenemsComponent.ChannelId.values());
    }


    @Activate
    public void activate(ComponentContext context, Config config) throws ConfigurationException {
        super.activate(context, config.id(), config.alias(), config.enabled());
        this.circuitBoardId = config.id();
        this.versionId = config.versionNumber();
        String adcFrequency = config.adcFrequency();
        String dipSwitches = config.dipSwitches();
        List<String> frequency = new ArrayList<>();
        List<Integer> dipSwitch = new ArrayList<>();

        if (adcFrequency.contains(";")) {
            String[] parts = adcFrequency.split(";");
            frequency.addAll(Arrays.asList(parts));
        } else {
            frequency.add(adcFrequency);
        }

        for (Character dipSwitchUse : dipSwitches.toCharArray()) {
            dipSwitch.add(Character.getNumericValue(dipSwitchUse));
        }
        createTemperatureBoard(this.versionId, frequency, dipSwitch);
    }

    /**
     * Creates the mcps from the enum list and initializes them.
     *
     * @param versionNumber what version is the board --> what mcps are built in.
     * @param frequency     what frequency should be used with the allocated mcp.
     * @param dipSwitch     dip-switch == spi channel.
     *                      <p>
     *                      the mcp-container has all the mcp's without initializing them --> basic properties are set
     *                      e.g. pin values.
     *                      </p>
     */
    private void createTemperatureBoard(String versionNumber, List<String> frequency, List<Integer> dipSwitch) {
        switch (versionNumber) {
            //more to come with further Versions + development of the hardware
            case "1":
                short counter = 0;
                for (Adc mcpWantToCreate : TemperatureModuleVersions.TEMPERATURE_MODULE_V_1.getMcpContainer()) {
                    createAdc(mcpWantToCreate, frequency.get(counter), dipSwitch.get(counter));
                    counter++;
                }
                break;
        }
    }

    /**
     * Initializes the Adc.
     *
     * @param mcpWantToCreate the Mcp (it's an adc) from the Enum.
     * @param frequency       set by the user.
     * @param dipSwitch       == spi channel.
     *
     *<p>Adds the Adc to the spi bridge.</p>
     */
    private void createAdc(Adc mcpWantToCreate, String frequency, int dipSwitch) {
        mcpWantToCreate.initialize(dipSwitch, Integer.parseInt(frequency), this.circuitBoardId, this.versionId);
        this.adcSet.add(mcpWantToCreate);
        bridgeSpi.addAdc(mcpWantToCreate);
    }

    @Deactivate
    public void deactivate() {
        super.deactivate();
        this.adcSet.forEach(adc -> {
            bridgeSpi.removeAdc(adc);
        });
    }

    @Override
    public String getCircuitBoardId() {
        return circuitBoardId;
    }

    @Override
    public String getVersionId() {
        return versionId;
    }

    @Override
    public Set<Adc> getAdcSet() {
        return adcSet;
    }
}