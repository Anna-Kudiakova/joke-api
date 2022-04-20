package com.joke_api;


import com.joke_api.services.CustomJokeService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class JokeWebSocketHandler extends TextWebSocketHandler {

    private final CustomJokeService customJokeService;

    private final GrpcJokeProviderGrpc.GrpcJokeProviderStub asyncStub;

    public JokeWebSocketHandler(CustomJokeService customJokeService, ConfigProperties configProperties, GrpcJokeProviderGrpc.GrpcJokeProviderStub asyncStub) {
        this.customJokeService = customJokeService;
        this.asyncStub = asyncStub;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        log.info("Connection is established");
        String username = session.getPrincipal().getName();
        Request request = Request.newBuilder()
                .addAllCategories(customJokeService.getCategories(username))
                .addAllFlags(customJokeService.getFlags(username))
                .build();
        asyncStub.serverSideStreamingGetJokeList(request, new StreamResponseObserver<>(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection is closed");
    }

    public static class StreamResponseObserver<Response> implements StreamObserver<Response> {

        private final WebSocketSession session;

        public StreamResponseObserver(WebSocketSession session) {
            this.session = session;
        }

        @Override
        public void onNext(Response response) {
            try {
                session.sendMessage(new TextMessage(response.toString()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            log.error(("%s was thrown on server side: %s").formatted(throwable, throwable.getMessage()), throwable);
        }

        @Override
        public void onCompleted() {
            try {
                log.info("RPC server that has been providing jokes is shut down");
                session.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}

