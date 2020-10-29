package io.openems.edge.dachs.gltinterface.api;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.chp.device.api.ChpBasic;

public interface DachsGltInterfaceChannel extends ChpBasic {

    public enum ChannelId implements io.openems.edge.common.channel.ChannelId {


        /**
         * Run setting of the chp. Dachs-Lauf-Anforderung, Hka_Bd.UHka_Anf.usAnforderung
         * <ul>
         * <li>Type: string
         * </ul>
         */

        RUN_SETTING(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Requested modules setting. Anzahl der angeforderten Module, Hka_Bd.Anforderung.ModulAnzahl
         * <ul>
         * <li>Type: integer
         * </ul>
         */

        NUMBER_OF_MODULES(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_ONLY)),

        /**
         * Not ready message of the chp. Dachs-Lauf-Freigabe, Hka_Bd.UHka_Frei.usFreigabe
         * <ul>
         * <li>Type: string
         * </ul>
         */

        NOT_READY_CODE(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Clearance flags for electricity guided operation of the chp. Strom-Freigabe, Freigabe Stromführung, Hka_Bd.UStromF_Frei.bFreigabe
         * <ul>
         * <li>Type: string
         * </ul>
         */

        ELECTRICITY_GUIDED_OPERATION_CLEARANCE(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Result of the electricity guided operation settings. Should the chp be running now or not. Anforderungen Strom, Hka_Bd.UHka_Anf.Anforderung.fStrom
         * <ul>
         * <li>Type: boolean
         * </ul>
         */

        ELECTRICITY_GUIDED_OPERATION_RUNFLAG(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_ONLY)),

        /**
         * Settings for electricity guided operation of the chp. Strom-Anforderung, Anforderungen Stromführung, Hka_Bd.Anforderung.UStromF_Anf.bFlagSF
         * <ul>
         * <li>Type: string
         * </ul>
         */

        ELECTRICITY_GUIDED_OPERATION(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Electrical work generated by the chp since the last restart. Hka_Bd.ulArbeitElektr
         * <ul>
         * <li>Type: double
         * <li>Unit: kilowatt hours
         * </ul>
         */

        ELECTRICAL_WORK(Doc.of(OpenemsType.DOUBLE).unit(Unit.KILOWATT_HOURS).accessMode(AccessMode.READ_ONLY)),

        /**
         * Thermal work generated by the chp since the last restart. Hka_Bd.ulArbeitThermHka
         * <ul>
         * <li>Type: double
         * <li>Unit: kilowatt hours
         * </ul>
         */

        THERMAL_WORK(Doc.of(OpenemsType.DOUBLE).unit(Unit.KILOWATT_HOURS).accessMode(AccessMode.READ_ONLY)),

        /**
         * Thermal work generated by the condenser since the last restart. Hka_Bd.ulArbeitThermKon
         * <ul>
         * <li>Type: double
         * <li>Unit: kilowatt hours
         * </ul>
         */

        THERMAL_WORK_COND(Doc.of(OpenemsType.DOUBLE).unit(Unit.KILOWATT_HOURS).accessMode(AccessMode.READ_ONLY)),

        /**
         * Time since last restart. Hka_Bd.ulBetriebssekunden (<- unit is hours, even if this says otherwise)
         * <ul>
         * <li>Type: double
         * <li>Unit: hours
         * </ul>
         */

        RUNTIME(Doc.of(OpenemsType.DOUBLE).unit(Unit.HOUR).accessMode(AccessMode.READ_ONLY)),

        /**
         * Rotations per minute of the chp engine. Hka_Mw1.usDrehzahl
         * <ul>
         * <li>Type: integer
         * <li>Unit: rotation per minute
         * </ul>
         */

        RPM(Doc.of(OpenemsType.INTEGER).unit(Unit.ROTATION_PER_MINUTE).accessMode(AccessMode.READ_ONLY)),

        /**
         * Engine starts since last restart of the chp. Hka_Bd.ulAnzahlStarts
         * <ul>
         * <li>Type: integer
         * </ul>
         */

        ENGINE_STARTS(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_ONLY)),

        /**
         * Serial number. Hka_Bd_Stat.uchSeriennummer
         * <ul>
         * <li>Type: string
         * </ul>
         */

        SERIAL_NUMBER(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Parts number. Hka_Bd_Stat.uchTeilenummer
         * <ul>
         * <li>Type: string
         * </ul>
         */

        PARTS_NUMBER(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * Maintenance needed. Wartung_Cache.fStehtAn
         * <ul>
         * <li>Type: boolean
         * </ul>
         */

        MAINTENANCE(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.READ_ONLY)),

        /**
         * Warning message of the chp. Hka_Bd.bWarnung
         * <ul>
         * <li>Type: string
         * </ul>
         */

        WARNING_CODE(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY)),

        /**
         * All occuring Errors as String. Hka_Bd.bStoerung
         * <ul>
         * <li>Type: string
         * </ul>
         */

        ERROR_CODE(Doc.of(OpenemsType.STRING).accessMode(AccessMode.READ_ONLY));


        private final Doc doc;

        private ChannelId(Doc doc) {
            this.doc = doc;
        }

        public Doc doc() {
            return this.doc;
        }

    }


    /**
     * Run setting of the chp. Dachs-Lauf-Anforderung, Hka_Bd.UHka_Anf.usAnforderung.
     *
     * @return the Channel
     */

    default Channel<String> getRunSetting() {
        return this.channel(ChannelId.RUN_SETTING);
    }

    /**
     * Number of requested modules. Only available (!=0) if Dachs has more than one module.
     * Anzahl der angeforderten Module, Hka_Bd.Anforderung.ModulAnzahl
     *
     * @return the Channel
     */

    default Channel<Integer> getNumberOfRequestedModules() { return this.channel(ChannelId.RPM); }

    /**
     * Not ready message of the chp. Dachs-Lauf-Freigabe, Hka_Bd.UHka_Frei.usFreigabe.
     *
     * @return the Channel
     */

    default Channel<String> getNotReadyCode() {
        return this.channel(ChannelId.NOT_READY_CODE);
    }

    /**
     * Clearance flags for electricity guided operation of the chp. Strom-Freigabe, Freigabe Stromführung, Hka_Bd.UStromF_Frei.bFreigabe
     *
     * @return the Channel
     */

    default Channel<String> getElecGuidedClearance() {
        return this.channel(ChannelId.ELECTRICITY_GUIDED_OPERATION_CLEARANCE);
    }

    /**
     * Result of the electricity guided operation settings. Should the chp be running now or not. Anforderungen Strom, Hka_Bd.UHka_Anf.Anforderung.fStrom
     *
     * @return the Channel
     */

    default Channel<Boolean> getElecGuidedRunFlag() {
        return this.channel(ChannelId.ELECTRICITY_GUIDED_OPERATION_RUNFLAG);
    }

    /**
     * Settings for electricity guided operation of the chp. Strom-Anforderung, Anforderungen Stromführung, Hka_Bd.Anforderung.UStromF_Anf.bFlagSF
     *
     * @return the Channel
     */

    default Channel<String> getElecGuidedSettings() {
        return this.channel(ChannelId.ELECTRICITY_GUIDED_OPERATION);
    }

    /**
     * Electrical work generated by the chp since the last restart, in kilowatt hours. Hka_Bd.ulArbeitElektr
     *
     * @return the Channel
     */

    default Channel<Double> getElectricWork() { return this.channel(ChannelId.ELECTRICAL_WORK); }

    /**
     * Thermal work generated by the chp since the last restart, in kilowatt hours. Hka_Bd.ulArbeitThermHka
     *
     * @return the Channel
     */

    default Channel<Double> getThermalWork() { return this.channel(ChannelId.THERMAL_WORK); }

    /**
     * Thermal work generated by the condenser since the last restart, in kilowatt hours. Hka_Bd.ulArbeitThermKon
     *
     * @return the Channel
     */

    default Channel<Double> getThermalWorkCond() { return this.channel(ChannelId.THERMAL_WORK_COND); }

    /**
     * Time since last restart in hours. Hka_Bd.ulBetriebssekunden (<- unit is hours, even if this says otherwise)
     *
     * @return the Channel
     */

    default Channel<Double> getRuntimeSinceRestart() { return this.channel(ChannelId.RUNTIME); }

    /**
     * Rotations per minute of the chp engine. Hka_Mw1.usDrehzahl
     *
     * @return the Channel
     */

    default Channel<Integer> getRpm() { return this.channel(ChannelId.RPM); }

    /**
     * Engine starts since last restart of the chp. Hka_Bd.ulAnzahlStarts
     *
     * @return the Channel
     */

    default Channel<Integer> getEngineStarts() { return this.channel(ChannelId.ENGINE_STARTS); }

    /**
     * Serial number. Hka_Bd_Stat.uchSeriennummer
     *
     * @return the Channel
     */

    default Channel<String> getSerialNumber() { return this.channel(ChannelId.SERIAL_NUMBER); }

    /**
     * Parts number. Hka_Bd_Stat.uchTeilenummer
     *
     * @return the Channel
     */

    default Channel<String> getPartsNumber() { return this.channel(ChannelId.PARTS_NUMBER); }

    /**
     * Flag signaling that a maintenance is needed soon. Wartung_Cache.fStehtAn
     *
     * @return the Channel
     */

    default Channel<Boolean> getMaintenanceFlag() { return this.channel(ChannelId.MAINTENANCE); }

    /**
     * If a warning occurred, the warning code is available via this channel. Hka_Bd.bWarnung
     *
     * @return the Channel
     */

    default Channel<String> getWarningMessages() {
        return this.channel(ChannelId.WARNING_CODE);
    }

    /**
     * If an error occurred, the error messages are available via this channel. Hka_Bd.bStoerung
     *
     * @return the Channel
     */

    default Channel<String> getErrorMessages() {
        return this.channel(ChannelId.ERROR_CODE);
    }

}