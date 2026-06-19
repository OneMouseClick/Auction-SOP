package edu.rutmiit.demo.grpcverificationclient.config;

import edu.rutmiit.demo.grpc.UserVerificationGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PreDestroy;

@Configuration
public class GrpcClientConfig {

    private static final Logger log = LoggerFactory.getLogger(GrpcClientConfig.class);

    @Value("${grpc.client.verification-server.host:localhost}")
    private String grpcHost;

    @Value("${grpc.client.verification-server.port:9090}")
    private int grpcPort;

    private ManagedChannel channel;

    @Bean
    public ManagedChannel managedChannel() {
        channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();

        log.info("gRPC канал создан: {}:{}", grpcHost, grpcPort);
        return channel;
    }

    @Bean
    public UserVerificationGrpc.UserVerificationBlockingStub userVerificationStub(ManagedChannel channel) {
        return UserVerificationGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            log.info("Закрытие gRPC канала...");
            channel.shutdown();
        }
    }
}