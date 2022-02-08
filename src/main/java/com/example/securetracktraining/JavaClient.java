package com.example.securetracktraining;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class JavaClient {


    public static void main(String[] args) throws URISyntaxException, InterruptedException, ExecutionException {

        StandardWebSocketClient client = new StandardWebSocketClient();
        ListenableFuture<WebSocketSession> future = client.doHandshake(new MyTextWebSocketHandler(),
                new WebSocketHttpHeaders(),
                new URI("ws://localhost:8080/jokes"));
        while (future.get().isOpen()) {

        }
    }

    static class MyTextWebSocketHandler extends TextWebSocketHandler {

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            log.info(message.getPayload());
        }
    }


}
