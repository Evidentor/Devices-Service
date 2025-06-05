package net.dimjasevic.karlo.fer.evidentor.devices_service.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.dimjasevic.karlo.fer.evidentor.devices_service.grpc.annotation.GrpcClient;
import net.dimjasevic.karlo.fer.evidentor.domain.proto.CheckAccessRequest;
import net.dimjasevic.karlo.fer.evidentor.domain.proto.CheckAccessResponse;
import net.dimjasevic.karlo.fer.evidentor.domain.proto.DecisionServiceGrpc;

@GrpcClient
public class DecisionClient {
    private final DecisionServiceGrpc.DecisionServiceBlockingStub blockingStub;

    public DecisionClient(ClientConfiguration clientConfiguration) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(
                        clientConfiguration.getHost(),
                        clientConfiguration.getPort()
                ).usePlaintext()
                .build();
        blockingStub = DecisionServiceGrpc.newBlockingStub(channel);
    }

    public CheckAccessResponse checkAccess(CheckAccessRequest request) {
        return blockingStub.checkAccess(request);
    }
}
