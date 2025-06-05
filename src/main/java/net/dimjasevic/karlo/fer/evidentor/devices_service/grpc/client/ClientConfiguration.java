package net.dimjasevic.karlo.fer.evidentor.devices_service.grpc.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ClientConfiguration {
    // TODO: Do this better, for each client make a separate config (or similar solution)

    @Value("${spring.grpc.client.decision.host}")
    private String host;

    @Value("${spring.grpc.client.decision.port}")
    private int port;
}
