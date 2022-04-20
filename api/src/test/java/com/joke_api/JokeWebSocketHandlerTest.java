package com.joke_api;


import com.joke_api.services.CustomJokeService;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JokeWebSocketHandlerTest {

    @Mock
    private CustomJokeService customJokeService;

    @Mock
    private WebSocketSession session;

    @Mock
    private Principal principal;

    @Mock
    private ConfigProperties configProperties;

    @Mock
    private GrpcJokeProviderGrpc.GrpcJokeProviderStub asyncStub;

    @InjectMocks
    JokeWebSocketHandler handler;


    @Test
    public void afterConnectionEstablished_ok() throws IOException, InterruptedException, TimeoutException {

        List<String> categories = List.of("Programming", "Pun");
        List<String> flags = List.of("sexist", "explicit");


        when(session.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("Macron");
        when(customJokeService.getCategories("Macron")).thenReturn(categories);
        when(customJokeService.getFlags("Macron")).thenReturn(flags);

        Request request = Request.newBuilder()
                .addAllCategories(categories)
                .addAllFlags(flags)
                .build();

        Response response = Response.newBuilder()
                .setJoke("joke")
                .build();

        Waiter w = new Waiter();

        AtomicReference<JokeWebSocketHandler.StreamResponseObserver> responseObserver = new AtomicReference<>();

        doAnswer(invocation -> {
            responseObserver.set(invocation.getArgument(1));
            responseObserver.get().onNext(response);
            w.resume();
            responseObserver.get().onNext(response);
            w.resume();
            responseObserver.get().onNext(response);
            w.resume();
            return null;
        }).when(asyncStub).serverSideStreamingGetJokeList(eq(request), any(JokeWebSocketHandler.StreamResponseObserver.class));

        handler.afterConnectionEstablished(session);

        w.await(1, TimeUnit.SECONDS, 3);

        responseObserver.get().onCompleted();
        verify(session).close();

        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

    }

}
