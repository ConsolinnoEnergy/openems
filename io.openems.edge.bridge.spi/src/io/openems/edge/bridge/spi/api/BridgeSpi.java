package io.openems.edge.bridge.spi.api;

import io.openems.edge.bridge.spi.task.SpiDoubleUartTask;
import io.openems.edge.bridge.spi.task.SpiTask;
import io.openems.edge.consolinno.leaflet.mainmodule.api.sc16.DoubleUart;
import io.openems.edge.spi.mcp.api.Adc;
import org.osgi.service.cm.ConfigurationException;

import java.util.Map;
import java.util.Set;


public interface BridgeSpi {

    void addAdc(Adc adc);

    Set<Adc> getAdcs();

    void removeAdc(Adc adc);

    void addSpiTask(String id, SpiTask spiTask) throws ConfigurationException;

    void removeSpiTask(String id);

    Map<String, SpiTask> getTasks();

    void addDoubleUart(DoubleUart uart);

    void removeDoubleUart(DoubleUart uart);

    void addDoubleUartTask(String id, SpiDoubleUartTask task) throws ConfigurationException;

    void removeDoubleUartTask(String id);
}
