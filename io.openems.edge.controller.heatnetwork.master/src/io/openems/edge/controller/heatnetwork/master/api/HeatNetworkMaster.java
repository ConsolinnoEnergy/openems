package io.openems.edge.controller.heatnetwork.master.api;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.component.OpenemsComponent;

public interface HeatNetworkMaster extends OpenemsComponent {

    enum ChannelId implements io.openems.edge.common.channel.ChannelId {
        /**
         * PowerLevel.
         *
         * <ul>
         * <li>Interface: PassingChannel
         * <li>Type: Double
         * <li> Unit: Percentage
         * </ul>
         */

        SET_POINT_TEMPERATURE(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.READ_WRITE).unit(Unit.PERCENT).onInit(
                channel -> {
                    ((IntegerWriteChannel) channel).onSetNextWrite(channel::setNextValue);
                }
        ));

        private final Doc doc;

        private ChannelId(Doc doc) {
            this.doc = doc;
        }

        @Override
        public Doc doc() {
            return this.doc;
        }


    }

    default WriteChannel<Integer> temperatureSetPointChannel() {
        return this.channel(ChannelId.SET_POINT_TEMPERATURE);
    }


}
