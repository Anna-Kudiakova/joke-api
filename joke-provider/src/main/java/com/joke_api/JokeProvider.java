package com.joke_api;

import java.util.List;

public interface JokeProvider {

    Joke getJoke(List<String> categories, List<String> blacklistFlags);

    Joke getSimpleJoke();

}