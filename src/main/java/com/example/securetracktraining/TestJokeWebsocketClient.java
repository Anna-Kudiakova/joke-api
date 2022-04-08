package com.example.securetracktraining;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class TestJokeWebsocketClient {


    public static void main(String[] args) throws URISyntaxException, InterruptedException, ExecutionException {

        StandardWebSocketClient client = new StandardWebSocketClient();

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String auth = "Biba" + ":" + "bibaPassword";
        headers.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));
        ListenableFuture<WebSocketSession> future = client.doHandshake(new MyTextWebSocketHandler(),
               headers,
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
