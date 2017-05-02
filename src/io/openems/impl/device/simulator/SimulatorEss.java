/*******************************************************************************
 * OpenEMS - Open Source Energy Management System
 * Copyright (c) 2016, 2017 FENECON GmbH and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *   FENECON GmbH - initial API and implementation and initial documentation
 *******************************************************************************/
package io.openems.impl.device.simulator;

import java.util.concurrent.ThreadLocalRandom;

import io.openems.api.channel.ConfigChannel;
import io.openems.api.channel.ReadChannel;
import io.openems.api.channel.StaticValueChannel;
import io.openems.api.channel.StatusBitChannels;
import io.openems.api.channel.WriteChannel;
import io.openems.api.device.nature.ess.SymmetricEssNature;
import io.openems.api.doc.ThingInfo;
import io.openems.api.exception.ConfigException;
import io.openems.core.utilities.ControllerUtils;
import io.openems.impl.protocol.modbus.ModbusWriteLongChannel;
import io.openems.impl.protocol.simulator.SimulatorDeviceNature;
import io.openems.impl.protocol.simulator.SimulatorReadChannel;

@ThingInfo(title = "Simulator ESS")
public class SimulatorEss extends SimulatorDeviceNature implements SymmetricEssNature {

	/*
	 * Constructors
	 */
	public SimulatorEss(String thingId) throws ConfigException {
		super(thingId);
		minSoc.addUpdateListener((channel, newValue) -> {
			// If chargeSoc was not set -> set it to minSoc minus 2
			if (channel == minSoc && !chargeSoc.valueOptional().isPresent()) {
				chargeSoc.updateValue((Integer) newValue.get() - 2, false);
			}
		});
	}

	/*
	 * Config
	 */
	private ConfigChannel<Integer> minSoc = new ConfigChannel<Integer>("minSoc", this);
	private ConfigChannel<Integer> chargeSoc = new ConfigChannel<Integer>("chargeSoc", this);

	@Override
	public ConfigChannel<Integer> minSoc() {
		return minSoc;
	}

	@Override
	public ConfigChannel<Integer> chargeSoc() {
		return chargeSoc;
	}

	/*
	 * Inherited Channels
	 */
	private StatusBitChannels warning = new StatusBitChannels("Warning", this);;
	private SimulatorReadChannel soc = new SimulatorReadChannel("Soc", this).unit("%");
	private SimulatorReadChannel activePower = new SimulatorReadChannel("ActivePower", this);
	private SimulatorReadChannel allowedApparent = new SimulatorReadChannel("AllowedApparent", this);
	private SimulatorReadChannel allowedCharge = new SimulatorReadChannel("AllowedCharge", this);
	private SimulatorReadChannel allowedDischarge = new SimulatorReadChannel("AllowedDischarge", this);
	private SimulatorReadChannel apparentPower = new SimulatorReadChannel("ApparentPower", this);
	private SimulatorReadChannel gridMode = new SimulatorReadChannel("GridMode", this);
	private SimulatorReadChannel reactivePower = new SimulatorReadChannel("ReactivePower", this);
	private SimulatorReadChannel systemState = new SimulatorReadChannel("SystemState", this) //
			.label(1, START).label(2, STOP);
	private ModbusWriteLongChannel setActivePower = new ModbusWriteLongChannel("SetActivePower", this);
	private ModbusWriteLongChannel setReactivePower = new ModbusWriteLongChannel("SetReactivePower", this);
	private ModbusWriteLongChannel setWorkState = new ModbusWriteLongChannel("SetWorkState", this);
	private StaticValueChannel<Long> maxNominalPower = new StaticValueChannel<>("maxNominalPower", this, 40000L)
			.unit("VA");
	private StaticValueChannel<Long> capacity = new StaticValueChannel<>("capacity", this, 50000L).unit("Wh");

	@Override
	public ReadChannel<Long> gridMode() {
		return gridMode;
	}

	@Override
	public ReadChannel<Long> soc() {
		return soc;
	}

	@Override
	public ReadChannel<Long> systemState() {
		return systemState;
	}

	@Override
	public ReadChannel<Long> allowedCharge() {
		return allowedCharge;
	}

	@Override
	public ReadChannel<Long> allowedDischarge() {
		return allowedDischarge;
	}

	@Override
	public WriteChannel<Long> setWorkState() {
		return setWorkState;
	}

	@Override
	public ReadChannel<Long> activePower() {
		return activePower;
	}

	@Override
	public ReadChannel<Long> apparentPower() {
		return apparentPower;
	}

	@Override
	public ReadChannel<Long> reactivePower() {
		return reactivePower;
	}

	@Override
	public WriteChannel<Long> setActivePower() {
		return setActivePower;
	}

	@Override
	public WriteChannel<Long> setReactivePower() {
		return setReactivePower;
	}

	@Override
	public StatusBitChannels warning() {
		return warning;
	}

	@Override
	public ReadChannel<Long> allowedApparent() {
		return allowedApparent;
	}

	private long getRandom(int min, int max) {
		return ThreadLocalRandom.current().nextLong(min, max + 1);
	}

	@Override
	public ReadChannel<Long> maxNominalPower() {
		return maxNominalPower;
	}

	/*
	 * Fields
	 */
	private long lastApparentPower = 0;
	private long lastSoc = 50;
	private double lastCosPhi = 0;

	/*
	 * Methods
	 */
	@Override
	protected void update() {
		lastSoc = SimulatorTools.addRandomLong(lastSoc, 0, 100, 5);
		this.soc.updateValue(lastSoc);
		lastApparentPower = SimulatorTools.addRandomLong(lastApparentPower, -10000, 10000, 100);
		lastCosPhi = SimulatorTools.addRandomDouble(lastCosPhi, -1.5, 1.5, 0.5);
		long activePower = ControllerUtils.calculateActivePowerFromApparentPower(lastApparentPower, lastCosPhi);
		long reactivePower = ControllerUtils.calculateReactivePower(activePower, lastCosPhi);
		this.activePower.updateValue(activePower);
		this.reactivePower.updateValue(reactivePower);
		this.apparentPower.updateValue(lastApparentPower);
		this.allowedCharge.updateValue(9000L);
		this.allowedDischarge.updateValue(3000L);
		this.systemState.updateValue(1L);
		this.gridMode.updateValue(0L);
	}

	@Override
	public StaticValueChannel<Long> capacity() {
		return capacity;
	}

}
