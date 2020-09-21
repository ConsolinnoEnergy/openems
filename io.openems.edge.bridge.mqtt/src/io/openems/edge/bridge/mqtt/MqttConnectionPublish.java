package io.openems.edge.bridge.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MqttConnectionPublish extends AbstractMqttConnection {

    MqttConnectionPublish(boolean timeStampEnabled, String timeDataFormat, String locale) {
        super(timeStampEnabled, timeDataFormat, locale);
    }

    //TODO MAYBE MORE STUFF
    void sendMessage(String topic, String message, int qos, boolean retainFlag, boolean addTime) throws MqttException {
        MqttMessage messageMqtt;
        if (super.timeStampEnabled && addTime) {
            message = super.addTimeToPayload(message);
        }
        messageMqtt = new MqttMessage(message.getBytes());
        messageMqtt.setQos(qos);
        messageMqtt.setRetained(retainFlag);
        super.mqttClient.publish(topic, messageMqtt);
        System.out.println("Message published: " + messageMqtt);
    }

    void sendMessage(String topic, String message, int qos) throws MqttException {
        sendMessage(topic, message, qos, false, false);
    }

}
