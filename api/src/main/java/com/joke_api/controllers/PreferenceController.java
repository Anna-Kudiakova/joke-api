package com.joke_api.controllers;

import com.joke_api.services.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PutMapping(path = "/categories")
    public List<String> setCategories(@RequestParam Long id, @RequestParam List<String> categories, Principal principal) {

        return preferenceService.setCategories(id, categories, principal.getName());
    }

    @PutMapping(path = "/flags")
    public List<String> setFlags(@RequestParam Long id, @RequestParam List<String> flags, Principal principal) {

        return preferenceService.setFlags(id, flags, principal.getName());
    }

}

