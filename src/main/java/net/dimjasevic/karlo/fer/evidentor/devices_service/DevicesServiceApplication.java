package net.dimjasevic.karlo.fer.evidentor.devices_service;

import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.MqttService;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.autoconfigure.MqttListenerProcessor;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.autoconfigure.MqttListenerModel;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AllArgsConstructor
@SpringBootApplication
@EntityScan(basePackages = "net.dimjasevic.karlo.fer.evidentor.domain")
@EnableJpaRepositories(basePackages = "net.dimjasevic.karlo.fer.evidentor.domain")
public class DevicesServiceApplication implements CommandLineRunner {
	private MqttService mqttService;
	private MqttListenerProcessor mqttListenerProcessor;
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(DevicesServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (MqttListenerModel model : mqttListenerProcessor.getListeners()) {
			IMqttMessageListener mqttMessageListener = applicationContext.getBean(model.listenerClass());
			mqttService.subscribe(
					model.topic(),
					model.qos(),
					mqttMessageListener
			);
		}
	}
}
