package com.joke_api.server;


import com.example.securetracktraining.grpc.GrpcJokeProviderGrpc;
import com.example.securetracktraining.grpc.Request;
import com.example.securetracktraining.grpc.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GrpcJokeProviderService extends GrpcJokeProviderGrpc.GrpcJokeProviderImplBase {

    private final JokeProvider jokeProvider;
    private final ObjectMapper objectMapper;
    private final ConfigProperties configProperties;

    public GrpcJokeProviderService(JokeProvider jokeProvider, ObjectMapper objectMapper, ConfigProperties configProperties) {
        this.jokeProvider = jokeProvider;
        this.objectMapper = objectMapper;
        this.configProperties = configProperties;
    }


    @Override
    public void serverSideStreamingGetJokeList(Request request, StreamObserver<Response> responseObserver) {

        Runtime.getRuntime().addShutdownHook(new Thread(responseObserver::onCompleted));

        while(!Context.current().isCancelled()){
            Joke joke = jokeProvider.getJoke(request.getCategoriesList(), request.getFlagsList());
            String jsonString = "";
            try {
                jsonString = objectMapper.writeValueAsString(joke);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                responseObserver.onError(e);
            }
            Response response = Response.newBuilder()
                    .setJoke(jsonString)
                    .build();

            responseObserver.onNext(response);
            try {
                Thread.sleep(configProperties.getRate());
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                responseObserver.onError(e);
            }
        }
        log.info("gRPC client was disconnected");
    }
}
