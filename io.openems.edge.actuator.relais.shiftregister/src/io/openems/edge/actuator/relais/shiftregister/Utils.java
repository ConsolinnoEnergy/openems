package io.openems.edge.actuator.relais.shiftregister;

import java.util.Arrays;
import java.util.stream.Stream;

import io.openems.edge.actuator.relais.api.ActuatorRelais;
import io.openems.edge.common.channel.AbstractReadChannel;
import io.openems.edge.common.channel.BooleanReadChannel;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.channel.StateCollectorChannel;
import io.openems.edge.common.component.OpenemsComponent;

public class Utils {
	public static Stream<? extends AbstractReadChannel<?>> initializeChannels(ShiftregisterRelaisActuator c) {
		return Stream.of( //
				Arrays.stream(OpenemsComponent.ChannelId.values()).map(channelId -> {
					switch (channelId) {
					case STATE:
						return new StateCollectorChannel(c, channelId);
					}
					return null;
				}), Arrays.stream(ActuatorRelais.ChannelId.values()).map(channelId -> {
					switch (channelId) {
					case ON_OFF:
						return new BooleanWriteChannel(c, channelId);
					}
					return null;
				})
		).flatMap(channel -> channel);
	}
}
