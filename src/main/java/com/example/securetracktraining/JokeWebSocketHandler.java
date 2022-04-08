package com.example.securetracktraining;

import com.example.securetracktraining.services.CustomJokeService;
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
public class JokeWebSocketHandler extends TextWebSocketHandler {

    private final JokeProvider jokeProvider;

    private final CustomJokeService customJokeService;

    private final ObjectMapper objectMapper;

    private final ConfigProperties configProperties;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        log.info("Connection is established");
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(() -> {
            String username = session.getPrincipal().getName();
            Joke joke = jokeProvider.getJoke(
                    customJokeService.getCategories(username),
                    customJokeService.getFlags(username));
            String jsonString = "";
            try {
                jsonString = objectMapper.writeValueAsString(joke);
                session.sendMessage(new TextMessage(jsonString));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }, 0, configProperties.getRate(), TimeUnit.MILLISECONDS);

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

