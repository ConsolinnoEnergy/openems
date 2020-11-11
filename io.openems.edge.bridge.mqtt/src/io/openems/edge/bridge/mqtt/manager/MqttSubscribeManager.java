package io.openems.edge.bridge.mqtt.manager;

import io.openems.edge.bridge.mqtt.api.MqttSubscribeTask;
import io.openems.edge.bridge.mqtt.api.MqttTask;
import io.openems.edge.bridge.mqtt.api.MqttType;
import io.openems.edge.bridge.mqtt.connection.MqttConnectionSubscribeImpl;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MqttBridge. Handles all SubscribeTasks.
 */
public class MqttSubscribeManager extends AbstractMqttManager {

    private Map<MqttType, MqttConnectionSubscribeImpl> connections = new HashMap<>();


    public MqttSubscribeManager(Map<String, List<MqttTask>> subscribeTasks, String mqttBroker, String mqttBrokerUrl,
                                String mqttUsername, String mqttPassword, String mqttClientId, int keepAlive,
                                boolean timeEnabled, DateTimeZone timeZone) throws MqttException {

        super(mqttBroker, mqttBrokerUrl, mqttUsername, mqttPassword, mqttClientId, keepAlive, subscribeTasks,
                timeEnabled, timeZone, false);
        MqttType[] types = MqttType.values();
        //Create MqttConnections for each mqttType
        for (int x = 0; x < types.length; x++) {
            this.connections.put(types[x], new MqttConnectionSubscribeImpl());
            this.connections.get(types[x]).createMqttSubscribeSession(super.mqttBroker, super.mqttClientId + "_SUBSCRIBE_" + x,
                    super.mqttUsername, super.mqttPassword, super.keepAlive);
        }

    }

    @Override
    public void forever() throws MqttException {
        super.calculateCurrentTime();
        //Get all tasks and update them.
        super.allTasks.forEach((key, value) -> {
            value.forEach(task -> {
                if (task instanceof MqttSubscribeTask) {
                    //Time can be set in each config.
                    if (task.isReady(super.getCurrentTime())) {
                        //Response to new message.
                        ((MqttSubscribeTask) task).response(this.connections.get(task.getMqttType()).getPayload(task.getTopic()));
                        ((MqttSubscribeTask) task).convertTime(super.timeZone);
                    }
                }
            });
        });
    }


    /**
     * Can subscribe to certain topic by params of the task and id.
     *
     * @param task MqttTask usually given from bride, created by component. Type, Topic, QoS is saved here.
     * @param id   id of the Component.
     * @throws MqttException throws exception if callback fails.
     */
    public void subscribeToTopic(MqttTask task, String id) throws MqttException {
        MqttConnectionSubscribeImpl connection = this.connections.get(task.getMqttType());
        connection.subscribeToTopic(task.getTopic(), task.getQos(), id);
    }

    public void deactivate() {
        super.deactivate();
        this.connections.forEach((key, value) -> {
            try {
                value.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Returns Payload from Topic.
     *
     * @param topic Topic of Payload.
     * @param type  MqttType e.g. Telemetry/Controls/events
     * @return the Payload.
     */

    public String getPayloadFromTopic(String topic, MqttType type) {
        return this.connections.get(type).getPayload(topic);
    }

    public void unsubscribeFromTopic(MqttTask task) throws MqttException {
        this.connections.get(task.getMqttType()).unsubscribeFromTopic(task.getTopic());
    }
}
