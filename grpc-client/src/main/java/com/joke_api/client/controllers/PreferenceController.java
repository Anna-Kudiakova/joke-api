package com.joke_api.client.controllers;

import com.joke_api.client.services.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PutMapping
    public void setPreferences(@RequestParam Long id,
                               @RequestParam(required = false) List<String> categories,
                               @RequestParam(required = false) List<String> flags) {

        preferenceService.setPreferences(id, categories, flags);
    }

}

