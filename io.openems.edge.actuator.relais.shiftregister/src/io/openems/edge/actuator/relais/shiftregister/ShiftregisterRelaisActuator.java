package io.openems.edge.actuator.relais.shiftregister;


import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import io.openems.edge.actuator.relais.api.ActuatorRelais;
import io.openems.edge.bridge.shiftregister.BridgeShiftregister;
import io.openems.edge.bridge.shiftregister.task.ShiftregisterTask;
import io.openems.edge.common.channel.doc.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;

import org.osgi.service.metatype.annotations.Designate;


@Designate(ocd = Config.class, factory = true)
@Component(name="Actuator.Relais.Shiftregister", //
immediate = true, //
configurationPolicy = ConfigurationPolicy.REQUIRE)
public class ShiftregisterRelaisActuator extends AbstractOpenemsComponent implements ActuatorRelais, OpenemsComponent{

	@Reference
	protected ConfigurationAdmin cm;
	public ShiftregisterRelaisActuator() {
		Utils.initializeChannels(this).forEach(channel -> this.addChannel(channel));
	}
	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected BridgeShiftregister shiftregister;

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.service_pid(), config.id(), config.enabled());

		if (OpenemsComponent.updateReferenceFilter(cm, config.service_pid(), "shiftregister", config.spi_id())) {
			return;
		}

		this.shiftregister.addTask(config.id(),new ShiftregisterTask(config.position(), !config.isOpener()));
	}
	@Deactivate
	protected void deactivate() {
		this.shiftregister.removeTask(this.id());
		super.deactivate();
	}

	public enum ChannelId implements io.openems.edge.common.channel.doc.ChannelId {
		;
		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		public Doc doc() {
			return this.doc;
		}
	}

	@Override
	public String debugLog() {
		return "RelaisActive:" + this.isActive().value().asString();
	}

}
