package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.listener.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.annotation.MqttListener;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.DeviceRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.Room;
import net.dimjasevic.karlo.fer.evidentor.domain.rooms.RoomRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.Telemetry;
import net.dimjasevic.karlo.fer.evidentor.domain.telemetry.TelemetryRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.users.User;
import net.dimjasevic.karlo.fer.evidentor.domain.users.UserRepository;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@AllArgsConstructor
@MqttListener(topic = "v1/topic")
public class TelemetryListener implements IMqttMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryListener.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final TelemetryRepository telemetryRepository;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOGGER.info("Received message from topic '{}': {}", topic, mqttMessage.toString());

        byte[] payload = mqttMessage.getPayload();

        TelemetryMessage telemetryMessage = MAPPER.readValue(payload, TelemetryMessage.class);

        // TODO: Camunda, access control dmn

        User user = userRepository.findByCardId(telemetryMessage.cardId()).orElseThrow();
        Room room = roomRepository.findById(telemetryMessage.roomId()).orElseThrow();
        Device device = deviceRepository.findById(telemetryMessage.deviceId()).orElseThrow();

        Telemetry telemetry = new Telemetry(user, device, room, LocalDateTime.now());
        telemetryRepository.save(telemetry);
    }
}
