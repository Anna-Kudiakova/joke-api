package com.joke_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Joke {

    private String category;
    private String type;
    private String joke;
    private String setup;
    private String delivery;
    private String lang;

}