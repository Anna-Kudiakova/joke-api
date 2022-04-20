package com.joke_api;

import com.joke_api.exceptions.JokeClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JokeProviderImpl implements JokeProvider {

    private final WebClient jokeWebClient;

    private static final List<String> CATEGORIES = List.of("Programming", "Misc", "Dark",
            "Pun", "Spooky", "Christmas" );

    private static final List<String> BLACKLISTFLAGS = List.of("nsfw", "religious", "political",
            "racist", "sexist", "explicit");


    public Joke getJoke(List<String> categories, List<String> blacklistFlags) {
        String strCategories;

        if(Objects.nonNull(categories)&&CATEGORIES.containsAll(categories)){
            strCategories = "/" + String.join(",",categories);
        } else {
            strCategories = "/Any";
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if(Objects.nonNull(blacklistFlags)&&BLACKLISTFLAGS.containsAll(blacklistFlags)){
            params.put("blacklistFlags", blacklistFlags);
        }
        else if (Objects.nonNull(blacklistFlags)){
            throw new JokeClientException("Blacklist flags contain unknown value");
        }

        return this.jokeWebClient.get().uri(u -> u
                .path(strCategories)
                .queryParams(params)
                .build())
                .retrieve()
                .bodyToMono(Joke.class)
                .onErrorResume(WebClientException.class, err -> {
                    throw new JokeClientException("Error message - "+err.getMessage());
                } )
                .block();

    }

    public Joke getSimpleJoke() {

        return this.jokeWebClient.get().uri(u -> u
                        .path("/Any")
                        .build())
                .retrieve()
                .bodyToMono(Joke.class)
                .onErrorResume(WebClientException.class, err -> {
                    throw new JokeClientException("Error message - "+err.getMessage());
                })
                .block();

    }

}
