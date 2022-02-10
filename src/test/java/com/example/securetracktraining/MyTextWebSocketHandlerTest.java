package com.example.securetracktraining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jodah.concurrentunit.Waiter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
public class MyTextWebSocketHandlerTest {

    @Test
    public void afterConnectionEstablished_ok() throws IOException, InterruptedException, TimeoutException {

        ObjectMapper mapper = new ObjectMapper();
        JokeProvider jokeProvider = mock(JokeProvider.class);
        WebSocketSession session = mock(WebSocketSession.class);
        ConfigProperties configProperties = mock(ConfigProperties.class);

        when(jokeProvider.getSimpleJoke()).thenReturn(getJoke());
        when(configProperties.getPeriodicity()).thenReturn(100L);


        Map<String, Object> attributes = new HashMap<>();

        when(session.getAttributes()).thenReturn(attributes);


        String joke = mapper.writeValueAsString(getJoke());

        Waiter w = new Waiter();

        doAnswer(answerVoid((TextMessage a) -> {
            w.assertEquals(joke, a.getPayload());
            w.resume();
        }))
                .when(session)
                .sendMessage(any(TextMessage.class));

        MyTextWebSocketHandler handler = new MyTextWebSocketHandler(jokeProvider, mapper, configProperties);

        handler.afterConnectionEstablished(session);

        w.await(200, TimeUnit.MILLISECONDS, 2);

        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        ScheduledFuture future = (ScheduledFuture) session.getAttributes().get(MyTextWebSocketHandler.class.getName());

        assertTrue(future.isCancelled());

    }

    @Test
    public void afterConnectionEstablished_jsonProcessingException() throws IOException {

        ObjectMapper mapper = mock(ObjectMapper.class);
        JokeProvider jokeProvider = mock(JokeProvider.class);
        WebSocketSession session = mock(WebSocketSession.class);
        ConfigProperties configProperties = mock(ConfigProperties.class);

        when(jokeProvider.getSimpleJoke()).thenReturn(getJoke());
        when(configProperties.getPeriodicity()).thenReturn(3000L);

        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(getJoke());
        doAnswer(answerVoid(((TextMessage a) -> {
        }))).when(session).sendMessage(any(TextMessage.class));

        MyTextWebSocketHandler handler = new MyTextWebSocketHandler(jokeProvider, mapper, configProperties);
        handler.afterConnectionEstablished(session);

        assertThrows(JsonProcessingException.class, () -> mapper.writeValueAsString(getJoke()));

    }

    @Test
    public void afterConnectionEstablished_IOException() throws IOException, InterruptedException, TimeoutException {

        ObjectMapper mapper = new ObjectMapper();
        JokeProvider jokeProvider = mock(JokeProvider.class);
        WebSocketSession session = mock(WebSocketSession.class);
        ConfigProperties configProperties = mock(ConfigProperties.class);

        when(jokeProvider.getSimpleJoke()).thenReturn(getJoke());
        when(configProperties.getPeriodicity()).thenReturn(100L);

        String joke = mapper.writeValueAsString(getJoke());

        Waiter w = new Waiter();

        doThrow(IOException.class)
                .doAnswer(answerVoid(((TextMessage a) -> {
                    w.assertEquals(joke, a.getPayload());
                    w.resume();
                }))).when(session).sendMessage(any(TextMessage.class));

        MyTextWebSocketHandler handler = new MyTextWebSocketHandler(jokeProvider, mapper, configProperties);
        handler.afterConnectionEstablished(session);

        w.await(200, TimeUnit.MILLISECONDS, 1);

    }

    private Joke getJoke() {
        return new Joke("Programming", "twopart", null,
                "Why do Java programmers hate communism?",
                "They don't want to live in a classless society.", "en");
    }


}
