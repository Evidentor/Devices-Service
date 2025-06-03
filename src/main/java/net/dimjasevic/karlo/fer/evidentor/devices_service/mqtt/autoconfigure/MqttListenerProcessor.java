package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.autoconfigure;

import lombok.Getter;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.annotation.MqttListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class MqttListenerProcessor implements BeanPostProcessor {

    private final List<MqttListenerModel> listeners = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(MqttListener.class) && bean instanceof IMqttMessageListener listener) {
            MqttListener mqttListener = bean.getClass().getAnnotation(MqttListener.class);
            listeners.add(new MqttListenerModel(
                    mqttListener.topic(),
                    mqttListener.qos(),
                    listener.getClass()
            ));
        }
        return bean;
    }
}
