package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.autoconfigure;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

public record MqttListenerModel(String topic, int qos, Class<? extends IMqttMessageListener> listenerClass) {
}
