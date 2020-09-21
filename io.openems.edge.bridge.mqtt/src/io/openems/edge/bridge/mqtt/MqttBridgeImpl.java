package io.openems.edge.bridge.mqtt;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.bridge.mqtt.api.MqttBridge;
import io.openems.edge.bridge.mqtt.api.MqttPublishTask;
import io.openems.edge.bridge.mqtt.api.MqttSubscribeTask;
import io.openems.edge.bridge.mqtt.api.MqttTask;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import org.eclipse.paho.client.mqttv3.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Designate(ocd = Config.class, factory = true)
@Component(name = "Bridge.Mqtt",
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_CONTROLLERS)
public class MqttBridgeImpl extends AbstractOpenemsComponent implements OpenemsComponent, MqttBridge, EventHandler {
    //Add to Manager
    private Map<String, List<MqttPublishTask>> publishTasks = new ConcurrentHashMap<>();
    private Map<String, List<MqttSubscribeTask>> subscribeTasks = new ConcurrentHashMap<>();

    private MqttPublishManager publishManager;
    private MqttSubscribeManager subscribeManager;
    private String mqttUsername;
    private String mqttPassword;
    private String mqttBroker;
    private String mqttBrokerUrl;
    private String mqttClientId;

    //ONLY DEBUG SUBSCRIBE
    private String subscribeId;
    private String subscribeTopic;

    private MqttConnectionPublish bridgePublisher;
    private MqttConnectionSubscribe bridgeSubscriberTest;

    public MqttBridgeImpl() {
        super(OpenemsComponent.ChannelId.values(),
                MqttBridge.ChannelId.values());
    }


    @Activate
    public void activate(ComponentContext context, Config config) throws OpenemsException {
        super.activate(context, config.id(), config.alias(), config.enabled());
        try {
            this.bridgePublisher = new MqttConnectionPublish(config.timeStampEnabled(), config.timeFormat(), config.locale());
            this.bridgeSubscriberTest = new MqttConnectionSubscribe(config.timeStampEnabled(), config.timeFormat(), config.locale());
            this.createMqttSession(config);
        } catch (MqttException e) {
            throw new OpenemsException(e.getMessage());
        }

        publishManager = new MqttPublishManager(publishTasks, this.mqttBroker, this.mqttBrokerUrl, this.mqttUsername,
                this.mqttPassword, this.mqttClientId, config.keepAlive());
        //ClientId --> + CLIENT_SUB_0
        subscribeManager = new MqttSubscribeManager(subscribeTasks, this.mqttBroker, this.mqttBrokerUrl, this.mqttUsername,
                this.mqttPassword, this.mqttClientId, config.keepAlive());

    }

    private void createMqttSession(Config config) throws MqttException {
        //TCP OR SSL
        String broker = config.connection().equals("Tcp") ? "tcp" : "ssl";
        boolean isTcp = broker.equals("tcp");
        broker += "://" + config.ipBroker() + ":" + config.portBroker();
        this.mqttBroker = broker;
        this.mqttUsername = config.username();

        //TODO ENCRYPT PASSWORD IF TCP NOT SSL!
        this.mqttPassword = config.password();

        this.mqttClientId = config.clientId();
        this.mqttBrokerUrl = config.brokerUrl();

        this.bridgePublisher.createMqttPublishSession(this.mqttBroker, this.mqttClientId, config.keepAlive(),
                this.mqttUsername, this.mqttPassword, config.cleanSessionFlag());
        if (config.lastWillSet()) {
            this.bridgePublisher.addLastWill(config.topicLastWill(),
                    config.payloadLastWill(), config.qosLastWill(), config.timeStampEnabled(), config.retainedFlag());
        }
        //External Call bc Last will can be set
        this.bridgePublisher.connect();

        this.bridgePublisher.sendMessage(config.topicLastWill(), "\"Status\": Connected", 1,
                config.retainedFlag(), config.timeStampEnabled());

        //DEBUG AND TEST TODO DELETE LATER
        this.subscribeId = this.mqttClientId + "_CLIENT_TEST_SUBSCRIBE";
        this.bridgeSubscriberTest.createMqttSubscribeSession(this.mqttBroker, subscribeId,
                this.mqttUsername, this.mqttPassword, config.keepAlive());

        this.subscribeTopic = config.topicLastWill();

        this.bridgeSubscriberTest.subscribeToTopic(subscribeTopic, 0, subscribeId);

    }

    //TODO Password encryption etc --> Research etc; Public and private Keys etc etc on not Secure Connection.
    // TODO AT THE END!
    //TODO DO THIS IN AN EXTRA CLASS!
    /*private char[] createHashedPassword(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Arrays.toString(hashedPassword).toCharArray();
    }*/

    @Deactivate
    public void deactivate() {
        try {
            this.bridgePublisher.disconnect();
            this.bridgeSubscriberTest.disconnect();
            this.publishManager.deactivate();
            this.subscribeManager.deactivate();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addMqttTask(String id, MqttTask mqttTask) {

        if (mqttTask instanceof MqttPublishManager) {
            if (this.publishTasks.containsKey(id)) {
                this.publishTasks.get(id).add((MqttPublishTask) mqttTask);
            } else {
                List<MqttPublishTask> task = new ArrayList<>();
                task.add((MqttPublishTask) mqttTask);
                this.publishTasks.put(id, task);
            }
        }

        if (mqttTask instanceof MqttSubscribeManager) {
            if (this.subscribeTasks.containsKey(id)) {
                this.subscribeTasks.get(id).add((MqttSubscribeTask) mqttTask);
            } else {
                List<MqttSubscribeTask> task = new ArrayList<>();
                task.add((MqttSubscribeTask) mqttTask);
                this.subscribeTasks.put(id, task);
            }
        }
        return true;
    }

    @Override
    public boolean removeMqttTasks(String id) {
        this.subscribeTasks.remove(id);
        this.publishTasks.remove(id);

        return true;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getTopic().equals(EdgeEventConstants.TOPIC_CYCLE_BEFORE_CONTROLLERS)) {
            this.subscribeManager.triggerNextRun();
            this.publishManager.triggerNextRun();
            
            System.out.println("Getting Message");
            this.bridgeSubscriberTest.getTopic(this.subscribeId).forEach(entry -> {
                System.out.println(entry + this.bridgeSubscriberTest.getPayload(entry));
            });
            //System.out.println(this.bridgeSubscriberTest.getTopic(this.subscribeId) + this.bridgeSubscriberTest.getPayload(subscribeTopic));
            System.out.println("New Message Await");
        }
    }
}