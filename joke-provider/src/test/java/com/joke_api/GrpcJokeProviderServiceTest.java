package com.joke_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrpcJokeProviderServiceTest {

    private Context context;

    @Mock
    private JokeProvider jokeProvider;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ConfigProperties configProperties;

    @InjectMocks
    GrpcJokeProviderService grpcJokeProviderService;

    @Test
    public void testServerSideStreamingGetJokeList_ok() throws JsonProcessingException, InterruptedException, TimeoutException {


        List<String> categories = List.of("Spooky", "Christmas");
        List<String> flags = List.of("sexist", "racist");
        Request request = Request.newBuilder()
                .addAllCategories(categories)
                .addAllFlags(flags)
                .build();

        var responseObserver = mock(StreamObserver.class);

        var joke =  new Joke("Spooky", "twopart", null,
                "What does a turkey dress up as for Halloween?",
                "A gobblin'!", "en");

        when(jokeProvider.getJoke(categories,flags)).thenReturn(joke);

         String expectedJsonString = objectMapper.writeValueAsString(joke);

         grpcJokeProviderService.serverSideStreamingGetJokeList(request,responseObserver);

         //verify(responseObserver, never()).onError(any());

         verify(responseObserver).onNext(expectedJsonString);


    }

}
