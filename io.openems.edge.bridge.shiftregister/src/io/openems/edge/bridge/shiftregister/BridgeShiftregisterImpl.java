package io.openems.edge.bridge.shiftregister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.edge.bridge.shiftregister.task.ShiftregisterTask;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.worker.AbstractCycleWorker;

import org.osgi.service.metatype.annotations.Designate;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Bridge.Shiftregister",
immediate = true, //
configurationPolicy = ConfigurationPolicy.REQUIRE, //
property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_EXECUTE_WRITE)
public class BridgeShiftregisterImpl extends AbstractOpenemsComponent
		implements BridgeShiftregister, EventHandler, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(BridgeShiftregisterImpl.class);

	private final Map<String, ShiftregisterTask> tasks = new HashMap<>();
	private final ShiftWorker worker = new ShiftWorker();

	@Activate
	protected void activate(ComponentContext context, Config config) {
		super.activate(context, config.service_pid(), config.id(), config.enabled());
		if (this.isEnabled()) {
			this.worker.initial(config);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (this.isEnabled()) {
			this.worker.deactivate();
		}
		super.deactivate();
	}

	@Override
	public void addTask(String sourceId, ShiftregisterTask task) {
		this.tasks.put(sourceId, task);
	}

	@Override
	public void removeTask(String sourceId) {
		this.tasks.remove(sourceId);
	}

	private class ShiftWorker extends AbstractCycleWorker {
		private GpioController gpio = null;
		private GpioPinDigitalOutput ser;
		private GpioPinDigitalOutput clk;
		private GpioPinDigitalOutput rclk;

		private final int millilength = 1;
		private int length;
		private boolean[] shifters;

		public void initial(Config config) {
			if (gpio == null) {
				gpio = GpioFactory.getInstance();
			}
			ser = gpio.provisionDigitalOutputPin(config.ser().getPin());
			clk = gpio.provisionDigitalOutputPin(config.clk().getPin());
			rclk = gpio.provisionDigitalOutputPin(config.rclk().getPin());
			this.length = config.amountChannels();
			this.shifters = new boolean[length];
			for (int i = 0; i < length; i++) {
				this.shifters[i] = false;
			}
			this.rclk.low();
			this.clk.low();
			this.activate(config.id());
		}

		@Override
		public void deactivate() {
			if (ser != null) {
				gpio.unprovisionPin(ser);
			}
			if (clk != null) {
				gpio.unprovisionPin(clk);
			}
			if (rclk != null) {
				gpio.unprovisionPin(rclk);
			}
			super.deactivate();
		}

		@Override
		protected void forever() {
			for (ShiftregisterTask task : tasks.values()) {
				Optional<Boolean> optional= task.getChannel().getNextWriteValueAndReset();
				if(optional.isPresent())
				{
					task.getChannel().setNextValue(optional.get());
				}
				boolean high = task.isReverse() ? !task.isActive() : task.isActive();
				if (task.getPosition() < this.length) {
					this.shifters[task.getPosition()] = high;
				} else {
					log.error("There is no such position." + task.getPosition() + " maximum is " + this.length);
				}
			}
			this.shift();
		}

		private void nextClock(boolean activate) throws InterruptedException {
			this.clk.low();
			Thread.sleep(millilength);
			if (activate) {
				ser.high();
			} else {
				ser.low();
			}
			this.clk.high();
			Thread.sleep(millilength);
			this.clk.low();
		}

		private void shift() {
			shift(false);
		}

		private void shift(boolean reverse) {
			boolean success = false;
			int runs = 0;
			while (!success && runs < 5) {
				try {
					for (int i = length - 1; i >= 0; i--) {
						this.nextClock(reverse ? !this.shifters[i] : this.shifters[i]);
					}
					this.clk.low();
					Thread.sleep(millilength);
					rclk.high();
					this.clk.high();
					Thread.sleep(millilength);
					rclk.low();
					this.clk.low();
					success = true;
				} catch (InterruptedException ex) {
					runs++;
				}
			}
		}

	}

	@Override
	public void handleEvent(Event event) {
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_EXECUTE_WRITE:
			this.worker.triggerNextCycle();
			break;
		}
	}

}
