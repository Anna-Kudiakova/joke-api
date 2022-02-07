package com.example.securetracktraining;

import com.example.securetracktraining.exceptions.JokeClientException;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        log.info("Connection is established");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(() -> {
            Joke joke = jokeProvider.getSimpleJoke();
            String strJoke;
            if ("single".equals(joke.getType()))
                strJoke = joke.getJoke();
            else if ("twopart".equals(joke.getType()))
                strJoke = joke.getSetup() + "\n" + joke.getDelivery();
            else
                throw new JokeClientException("Unknown type of joke provided");
            try {
                session.sendMessage(new TextMessage(strJoke));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);

        session.getAttributes().put(getClass().getName(), future);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        ScheduledFuture<?> scheduledFuture =
                (ScheduledFuture<?>) session.getAttributes().get(getClass().getName());
        scheduledFuture.cancel(true);
        log.info("Connection is closed");

    }

}

