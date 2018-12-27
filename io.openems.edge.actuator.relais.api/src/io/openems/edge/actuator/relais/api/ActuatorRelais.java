package io.openems.edge.actuator.relais.api;


import org.osgi.annotation.versioning.ProviderType;

import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.doc.Doc;
import io.openems.edge.common.channel.doc.Unit;
import io.openems.edge.common.component.OpenemsComponent;

/**
 * Represents a Relais
 */
@ProviderType
public interface ActuatorRelais extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.doc.ChannelId {
		/**
		 * Temperature
		 * 
		 * <ul>
		 * <li>Interface: ActuatorRelais
		 * <li>Type: boolean
		 * <li>Unit: ON_OFF
		 * </ul>
		 */
		ON_OFF(new Doc().type(OpenemsType.BOOLEAN).unit(Unit.ON_OFF)); //

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		public Doc doc() {
			return this.doc;
		}
	}

	/**
	 * Is active or not.
	 * 
	 * @return
	 */
	default Channel<Boolean> isActive() {
		return this.channel(ChannelId.ON_OFF);

	}
}