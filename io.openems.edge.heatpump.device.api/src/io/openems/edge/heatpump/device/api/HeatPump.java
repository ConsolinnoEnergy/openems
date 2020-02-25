package io.openems.edge.heatpump.device.api;

import io.openems.common.channel.Unit;
import io.openems.common.channel.AccessMode;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.component.OpenemsComponent;

public interface HeatPump extends OpenemsComponent {

    enum ChannelId implements io.openems.edge.common.channel.ChannelId {
        //Get Measured Data

        DIFFERENTIAL_PRESSURE_HEAD(Doc.of(OpenemsType.DOUBLE)),
        ELECTRONICS_TEMPERATURE(Doc.of(OpenemsType.DOUBLE).unit(Unit.DEZIDEGREE_CELSIUS)),
        CURRENT_MOTOR(Doc.of(OpenemsType.DOUBLE)),
        POWER_CONSUMPTION(Doc.of(OpenemsType.DOUBLE).unit(Unit.WATT)),
        CURRENT_PRESSURE(Doc.of(OpenemsType.DOUBLE).unit(Unit.BAR)),
        CURRENT_PUMP_FLOW(Doc.of(OpenemsType.DOUBLE).unit(Unit.CUBICMETER_PER_HOUR)),
        PUMPED_WATER_MEDIUM_TEMPERATURE(Doc.of(OpenemsType.DOUBLE).unit(Unit.DEZIDEGREE_CELSIUS)),
        ACTUAL_CONTROL_MODE(Doc.of(OpenemsType.DOUBLE)),
        ALARM_CODE_PUMP(Doc.of(OpenemsType.DOUBLE)),
        WARN_CODE(Doc.of(OpenemsType.DOUBLE)),
        ALARM_CODE(Doc.of(OpenemsType.DOUBLE)),
        WARN_BITS_1(Doc.of(OpenemsType.STRING)),
        WARN_BITS_2(Doc.of(OpenemsType.STRING)),
        WARN_BITS_3(Doc.of(OpenemsType.STRING)),
        WARN_BITS_4(Doc.of(OpenemsType.STRING)),

        //reference Values
        R_MIN(Doc.of(OpenemsType.DOUBLE)),
        R_MAX(Doc.of(OpenemsType.DOUBLE)),

        //config params
        SET_PUMP_FLOW_HI(Doc.of(OpenemsType.DOUBLE).unit(Unit.PERCENT).accessMode(AccessMode.READ_WRITE)),
        SET_PUMP_FLOW_LO(Doc.of(OpenemsType.DOUBLE).unit(Unit.PERCENT).accessMode(AccessMode.READ_WRITE)),

        SET_PRESSURE_DELTA(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE).unit(Unit.PERCENT)),
        SET_MAX_PRESSURE(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE).unit(Unit.PERCENT)),
        SET_MIN_PRESSURE(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE).unit(Unit.PERCENT)),

        H_CONST_REF_MIN(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE)),
        H_CONST_REF_MAX(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE)),

        //commands
        START(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        STOP(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        REMOTE(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        MIN_MOTOR_CURVE(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        MAX_MOTOR_CURVE(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        CONST_FREQUENCY(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        CONST_PRESSURE(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),
        AUTO_ADAPT(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_WRITE)),

        //
        REF_REM(Doc.of(OpenemsType.DOUBLE).accessMode(AccessMode.READ_WRITE));
        private final Doc doc;

        private ChannelId(Doc doc) {
            this.doc = doc;
        }

        @Override
        public Doc doc() {
            return this.doc;
        }
    }


    default Channel<Double> getDiffPressureHead() {
        return this.channel(ChannelId.DIFFERENTIAL_PRESSURE_HEAD);
    }

    default Channel<Double> getElectronicsTemperature() {
        return this.channel(ChannelId.ELECTRONICS_TEMPERATURE);
    }

    default Channel<Double> getCurrentMotor() {

        return this.channel(ChannelId.CURRENT_MOTOR);
    }

    default Channel<Double> getPowerConsumption() {
        return this.channel(ChannelId.POWER_CONSUMPTION);
    }

    default Channel<Double> getCurrentPressure() {
        return this.channel(ChannelId.CURRENT_PRESSURE);
    }

    default Channel<Double> getCurrentPumpFlow() {
        return this.channel(ChannelId.CURRENT_PUMP_FLOW);
    }

    default Channel<Double> getPumpedWaterMediumTemperature() {
        return this.channel(ChannelId.PUMPED_WATER_MEDIUM_TEMPERATURE);
    }

    default Channel<Double> getActualControlMode() {
        return this.channel(ChannelId.ACTUAL_CONTROL_MODE);
    }

    default Channel<Double> getAlarmCodePump() {
        return this.channel(ChannelId.ALARM_CODE_PUMP);
    }

    default Channel<Double> getWarnCode() {
        return this.channel(ChannelId.WARN_CODE);
    }

    default Channel<Double> getAlarmCode() {
        return this.channel(ChannelId.ALARM_CODE);
    }

    default Channel<String> getWarnBits_1() {
        return this.channel(ChannelId.WARN_BITS_1);
    }

    default Channel<String> getWarnBits_2() {
        return this.channel(ChannelId.WARN_BITS_2);
    }

    default Channel<String> getWarnBits_3() {
        return this.channel(ChannelId.WARN_BITS_3);
    }

    default Channel<String> getWarnBits_4() {
        return this.channel(ChannelId.WARN_BITS_4);
    }

    default Channel<Double> getRmin() {
        return this.channel(ChannelId.R_MIN);
    }

    default Channel<Double> getRmax() {
        return this.channel(ChannelId.R_MAX);
    }

    //Write Tasks

    default WriteChannel<Double> setPumpFlowHi() {
        return this.channel(ChannelId.SET_PUMP_FLOW_HI);
    }

    default WriteChannel<Double> setPumpFlowLo() {
        return this.channel(ChannelId.SET_PUMP_FLOW_LO);
    }

    default WriteChannel<Double> setPressureDelta() {
        return this.channel(ChannelId.SET_PRESSURE_DELTA);
    }

    default WriteChannel<Double> setMaxPressure() {
        return this.channel(ChannelId.SET_MAX_PRESSURE);
    }

    default WriteChannel<Double> setMinPressure() {
        return this.channel(ChannelId.SET_MIN_PRESSURE);
    }

    default WriteChannel<Double> setConstRefMinH() {
        return this.channel(ChannelId.H_CONST_REF_MIN);
    }

    default WriteChannel<Double> setConstRefMaxH() {
        return this.channel(ChannelId.H_CONST_REF_MAX);
    }

    //command Channel
    default WriteChannel<Boolean> setRemote() {
        return this.channel(ChannelId.REMOTE);
    }

    default WriteChannel<Boolean> setStart() {
        return this.channel(ChannelId.START);
    }

    default WriteChannel<Boolean> setStop() {
        return this.channel(ChannelId.STOP);
    }

    default WriteChannel<Boolean> setAutoAdapt() {
        return this.channel(ChannelId.AUTO_ADAPT);
    }

    default WriteChannel<Boolean> setMinMotorCurve() {
        return this.channel(ChannelId.MIN_MOTOR_CURVE);
    }

    default WriteChannel<Boolean> setMaxMotorCurve() {
        return this.channel(ChannelId.MAX_MOTOR_CURVE);
    }

    default WriteChannel<Boolean> setConstFrequency() {
        return this.channel(ChannelId.CONST_FREQUENCY);
    }

    default WriteChannel<Boolean> setConstPressure() {
        return this.channel(ChannelId.CONST_PRESSURE);
    }
    //reference Value

    default WriteChannel<Double> setRefRem() {
        return this.channel(ChannelId.REF_REM);
    }

}



