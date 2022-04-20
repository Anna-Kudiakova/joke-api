package com.joke_api.config;

import com.joke_api.ConfigProperties;
import com.joke_api.GrpcJokeProviderService;
import com.joke_api.JokeProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@Configuration
public class GrpcConfig {

    private final JokeProvider jokeProvider;
    private final ObjectMapper objectMapper;
    private final ConfigProperties configProperties;

    public GrpcConfig(JokeProvider jokeProvider, ObjectMapper objectMapper, ConfigProperties configProperties) {
        this.jokeProvider = jokeProvider;
        this.objectMapper = objectMapper;
        this.configProperties = configProperties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startRpcServer() throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(configProperties.getGrpcPort())
                .addService(new GrpcJokeProviderService(jokeProvider, objectMapper, configProperties))
                .build()
                .start();

    }


}