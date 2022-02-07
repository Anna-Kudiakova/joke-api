package com.example.securetracktraining;


import com.example.securetracktraining.config.WireMockConfig;
import com.example.securetracktraining.exceptions.JokeClientException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WireMockConfig.class})
public class JokeProviderIT {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JokeProvider jokeProvider;

    @Test
    void getSimpleJoke_isOk() throws IOException {

        wireMockServer.stubFor(WireMock.get(urlEqualTo("/Any"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new String(Files.readAllBytes(Paths.get(
                                "src/test/resources/get-simple-joke-response.json"))))));

        assertEquals(jokeProvider.getSimpleJoke(), new Joke(
                "Programming", "twopart", null,
                "Why do Java programmers hate communism?",
                "They don't want to live in a classless society.", "en"));

    }

    @Test
    void getSimpleJoke_throwException() throws IOException {

        wireMockServer.stubFor(WireMock.get(urlEqualTo("/Any"))
                .willReturn(WireMock.aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThrows(JokeClientException.class, () -> jokeProvider.getSimpleJoke());

    }

    @Test
    void getJoke_isOk() throws IOException {

        List<String> categories = List.of("Dark", "Spooky");
        List<String> blacklistFlags = List.of("nsfw", "explicit");

        wireMockServer.stubFor(WireMock.get(urlEqualTo(
                        "/Dark,Spooky?blacklistFlags=nsfw&blacklistFlags=explicit"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new String(Files.readAllBytes(Paths.get(
                                "src\\test\\resources\\get-joke-response.json"))))));

        assertEquals(jokeProvider.getJoke(categories, blacklistFlags), new Joke(
                "Spooky", "twopart", null,
                "What does a turkey dress up as for Halloween?",
                "A gobblin'!", "en"));

    }

    @Test
    void getJokeWithWrongCategory_isOk() throws IOException {

        List<String> categories = List.of("Sweet", "Spooky");

        wireMockServer.stubFor(WireMock.get(urlEqualTo("/Any"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new String(Files.readAllBytes(Paths.get(
                                "src/test/resources/get-joke-response.json"))))));

        assertEquals(jokeProvider.getJoke(categories, null), new Joke(
                "Spooky", "twopart", null,
                "What does a turkey dress up as for Halloween?",
                "A gobblin'!", "en"));

    }

    @Test
    void getJokeWithWrongBlacklistFlag_throwException() throws IOException {

        List<String> blacklistFlags = List.of("sweet");

        JokeClientException exception = assertThrows(
                JokeClientException.class, () -> jokeProvider.getJoke(null, blacklistFlags));

        assertEquals("Blacklist flags contain unknown value", exception.getMessage());

    }

    @Test
    void getJoke_throwException() throws IOException {

        wireMockServer.stubFor(WireMock.get(urlEqualTo("/Any"))
                .willReturn(WireMock.aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThrows(JokeClientException.class, () -> jokeProvider.getJoke(null, null));

    }


}

