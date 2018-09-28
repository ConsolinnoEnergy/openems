package io.openems.edge.timedata.api;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

import io.openems.common.timedata.CommonTimedataService;
import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.channel.doc.Doc;
import io.openems.edge.common.component.OpenemsComponent;

@ProviderType
public interface Timedata extends CommonTimedataService, OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.doc.ChannelId {
		;
		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public Optional<Object> getHistoricChannelValue(ChannelAddress channelAddress, ZonedDateTime at);

}
