package com.joke_api.client.config;

import com.joke_api.client.ConfigProperties;
import com.joke_api.client.GrpcJokeProviderGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    private final ConfigProperties configProperties;

    public GrpcClientConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public GrpcJokeProviderGrpc.GrpcJokeProviderStub asyncStub(Channel grpcChannel) {
        return GrpcJokeProviderGrpc.newStub(grpcChannel);
    }

    @Bean
    public Channel grpcChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", configProperties.getGrpcPort())
                .usePlaintext()
                .build();
    }
}
