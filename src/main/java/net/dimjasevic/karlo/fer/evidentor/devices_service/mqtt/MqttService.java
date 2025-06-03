package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt;

import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MqttService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttService.class);

    private IMqttClient mqttClient;

    public void publish(String topic, final String payload, int qos, boolean retained)
            throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setQos(qos);
        message.setRetained(retained);

        LOGGER.info("Publishing message {} to topic '{}'", payload, topic);
        mqttClient.publish(topic, message.getPayload(), qos, retained);
    }

    public void subscribe(String topic, int qos, IMqttMessageListener mqttMessageListener) throws MqttException {
        mqttClient.subscribeWithResponse(topic, qos, mqttMessageListener);
        LOGGER.info("Subscribed to topic '{}'", topic);
    }

}
