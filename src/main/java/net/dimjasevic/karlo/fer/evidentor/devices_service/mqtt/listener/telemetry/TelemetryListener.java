package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.listener.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.dimjasevic.karlo.fer.evidentor.devices_service.grpc.client.DecisionClient;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.MqttService;
import net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.annotation.MqttListener;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.Device;
import net.dimjasevic.karlo.fer.evidentor.domain.devices.DeviceRepository;
import net.dimjasevic.karlo.fer.evidentor.domain.proto.CheckAccessRequest;
import net.dimjasevic.karlo.fer.evidentor.domain.proto.CheckAccessResponse;
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
import java.time.ZoneId;

@AllArgsConstructor
@MqttListener(topic = "/v1/devices/+/telemetry")
public class TelemetryListener implements IMqttMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryListener.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final TelemetryRepository telemetryRepository;
    private final DecisionClient decisionClient;
    private final MqttService mqttService;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOGGER.info("Received message from topic '{}': {}", topic, mqttMessage.toString());

        byte[] payload = mqttMessage.getPayload();

        TelemetryMessage telemetryMessage = MAPPER.readValue(payload, TelemetryMessage.class);

        // Get deviceId from serialNumber that is in topic name
        String deviceSerialNumber = topic.split("/")[3];
        Device device = deviceRepository.getBySerialNumber(deviceSerialNumber).orElseThrow();

        Room room = device.getRoom();
        CheckAccessRequest request = CheckAccessRequest
                .newBuilder()
                .setDeviceId(device.getId())
                .setCardId(telemetryMessage.cardId())
                .setRoomId(room == null ? -1 : device.getRoom().getId())
                .build();
        CheckAccessResponse response = decisionClient.checkAccess(request);
        LOGGER.info(response.toString());
        boolean accessGranted = response.getAccessGranted();

        if (accessGranted && room != null) {
            User user = userRepository.findByCardId(telemetryMessage.cardId()).orElseThrow();

            Telemetry telemetry = new Telemetry(user, device, room, LocalDateTime.now(ZoneId.of("UTC")));
            telemetryRepository.save(telemetry);

            LOGGER.info(
                    "Telemetry: {} saved. [userId={}] [deviceId={}] [roomId={}],",
                    telemetry.getId(),
                    telemetry.getUser().getId(),
                    telemetry.getDevice().getId(),
                    telemetry.getRoom().getId()
            );
        }

        // Inform device about access
        String ackTopic = String.format("/v1/devices/%s/telemetry/ack", deviceSerialNumber);
        TelemetryAckMessage message = new TelemetryAckMessage(accessGranted);
        mqttService.publish(ackTopic, MAPPER.writeValueAsString(message), 1, false);
    }
}
