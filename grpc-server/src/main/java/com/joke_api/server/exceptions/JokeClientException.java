package com.joke_api.server.exceptions;

public class JokeClientException extends RuntimeException{

    public JokeClientException(String message) {
        super(message);
    }

}
