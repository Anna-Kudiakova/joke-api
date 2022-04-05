package com.example.securetracktraining.controllers;

import com.example.securetracktraining.dao.impl.CategoriesRepositoryImpl;
import com.example.securetracktraining.dao.impl.FlagsRepositoryImpl;
import com.example.securetracktraining.services.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferences")
public class PreferenceController {

    private final CategoriesRepositoryImpl categoriesRepository;

    private final FlagsRepositoryImpl flagsRepository;

    private final PreferenceService preferenceService;

    @PutMapping
    public void setPreferences(@RequestParam Long id,
                               @RequestParam(required = false) List<String> categories,
                               @RequestParam(required = false) List<String> flags) {

        preferenceService.setPreferences(id, categories, flags);
    }

}

