package com.example.securetracktraining.controllers;

import com.example.securetracktraining.dao.impl.CategoriesRepositoryImpl;
import com.example.securetracktraining.dao.impl.FlagsRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferences")
public class PreferenceController {

    private final CategoriesRepositoryImpl categoriesRepository;

    private final FlagsRepositoryImpl flagsRepository;

    @PostMapping
    public void setPreferences(@RequestParam Long id,
                               @RequestParam(required = false) List<String> categories,
                               @RequestParam(required = false) List<String> flags) {

        categoriesRepository.findById(id).ifPresent(categoriesEntity -> {
            if (Objects.nonNull(categories)) {
                categoriesEntity.setProgramming(categories.contains("programming"));
                categoriesEntity.setMisc(categories.contains("misc"));
                categoriesEntity.setDark(categories.contains("dark"));
                categoriesEntity.setPun(categories.contains("pun"));
                categoriesEntity.setSpooky(categories.contains("spooky"));
                categoriesEntity.setChristmas(categories.contains("christmas"));
                categoriesRepository.update(categoriesEntity);
            }
        });

        flagsRepository.findById(id).ifPresent(flagsEntity -> {
            if (Objects.nonNull(flags)) {
                flagsEntity.setNsfw(flags.contains("nsfw"));
                flagsEntity.setReligious(flags.contains("religious"));
                flagsEntity.setPolitical(flags.contains("political"));
                flagsEntity.setRacist(flags.contains("racist"));
                flagsEntity.setSexist(flags.contains("sexist"));
                flagsEntity.setExplicit(flags.contains("explicit"));
                flagsRepository.update(flagsEntity);
            }
        });
    }

}

