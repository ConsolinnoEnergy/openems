package io.openems.edge.consolinno.leaflet.mainmodule.api.sc16;

import org.osgi.service.cm.ConfigurationException;

public interface DoubleUart {

    int getSpiChannel();

    String getId();

    //Current Version only uses Sc16...maybe in Future different DoubleUARTs

    /**
     * Add a Sc16Task. In Future it will add a UArtTask; But for now Sc16Task is enough.
     *
     * @param id   Unique Id of this task, usually from the DoubleUArtDevice you activate in Config.
     * @param task Task created by the DoubleUARTDevice. Either Read or Write.
     * @throws ConfigurationException if the Id is already in tasks list --> Not Unique Id.
     */
    void addTask(String id, Sc16Task task) throws ConfigurationException;

    /**
     * Removes the Task given previously by the DobuleUARTDevice.
     *
     * @param id unique ID of the Task.
     */
    void removeTask(String id);


    /**
     * Shift starts the Communication of the DoubleUART via SPI. Utilizing it's given Tasks.
     * E.G. SC16 will communicate with SPI and sends Outputs and receive Data afterwards with the states of the GPIOs.
     *
     * @throws Throwable if Somethings wrong with the Communication.
     */
    void shift() throws Throwable;


}
