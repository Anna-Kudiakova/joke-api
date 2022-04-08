package com.example.securetracktraining;

import com.example.securetracktraining.services.CustomJokeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JokeWebSocketHandlerTest {

    @Spy
    ObjectMapper mapper;

    @Mock
    CustomJokeService customJokeService;

    @Mock
    JokeProvider jokeProvider;
    @Mock
    WebSocketSession session;

    @Mock
    Principal principal;

    @Mock
    ConfigProperties configProperties;

    @InjectMocks
    JokeWebSocketHandler handler;

    @Test
    public void afterConnectionEstablished_ok() throws IOException, InterruptedException, TimeoutException {

        List<String> categories = List.of("Programming","Pun");
        List<String> flags = List.of("sexist","explicit");

        when(configProperties.getRate()).thenReturn(100L);
        when(session.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("Macron");
        when(customJokeService.getCategories("Macron")).thenReturn(categories);
        when(customJokeService.getFlags("Macron")).thenReturn(flags);
        when(jokeProvider.getJoke(categories,flags)).thenReturn(getJoke());
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

        handler.afterConnectionEstablished(session);

        w.await(200, TimeUnit.MILLISECONDS, 2);

        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        ScheduledFuture future = (ScheduledFuture) session.getAttributes().get(JokeWebSocketHandler.class.getName());

        assertTrue(future.isCancelled());

    }

    @Test
    public void afterConnectionEstablished_IOException()
            throws IOException, InterruptedException, TimeoutException {

        List<String> categories = List.of("Programming","Pun");
        List<String> flags = List.of("sexist","explicit");

        when(configProperties.getRate()).thenReturn(100L);
        when(session.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("Macron");
        when(customJokeService.getCategories("Macron")).thenReturn(categories);
        when(customJokeService.getFlags("Macron")).thenReturn(flags);
        when(jokeProvider.getJoke(categories,flags)).thenReturn(getJoke());

        String joke = mapper.writeValueAsString(getJoke());

        Waiter w = new Waiter();

        doThrow(IOException.class)
                .doAnswer(answerVoid(((TextMessage a) -> {
                    w.assertEquals(joke, a.getPayload());
                    w.resume();
                }))).when(session).sendMessage(any(TextMessage.class));

        handler.afterConnectionEstablished(session);

        w.await(200, TimeUnit.MILLISECONDS, 1);

    }

    private Joke getJoke() {
        return new Joke("Programming", "twopart", null,
                "Why do Java programmers hate communism?",
                "They don't want to live in a classless society.", "en");
    }


}
