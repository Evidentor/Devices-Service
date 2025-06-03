package net.dimjasevic.karlo.fer.evidentor.devices_service.mqtt.listener.telemetry;

public record TelemetryMessage (String cardId, Long deviceId, Long roomId) {}
