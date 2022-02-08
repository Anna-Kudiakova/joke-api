package com.example.securetracktraining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyTextWebSocketHandler extends TextWebSocketHandler {

    private final JokeProvider jokeProvider;

    private final ObjectMapper objectMapper;

    private final ConfigProperties configProperties;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        log.info("Connection is established");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(() -> {
            Joke joke = jokeProvider.getSimpleJoke();
            String jsonString = "";
            try {
                jsonString = objectMapper.writeValueAsString(joke);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
            try {
                session.sendMessage(new TextMessage(jsonString));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }, 0, configProperties.getPeriodicity(), TimeUnit.MILLISECONDS);

        session.getAttributes().put(getClass().getName(), future);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (session.getAttributes().get(getClass().getName()) instanceof ScheduledFuture future) {
            future.cancel(true);
        }
        log.info("Connection is closed");

    }

}

