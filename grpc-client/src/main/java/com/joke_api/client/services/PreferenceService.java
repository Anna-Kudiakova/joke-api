package com.joke_api.client.services;


import com.joke_api.client.dao.CategoriesRepository;
import com.joke_api.client.dao.FlagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final CategoriesRepository categoriesRepository;

    private final FlagsRepository flagsRepository;

    public void setPreferences(Long id, List<String> categories, List<String> flags) {
        if (Objects.nonNull(categories)) {
            categoriesRepository.findById(id).ifPresent(categoriesEntity -> {
                categoriesEntity.setProgramming(categories.contains("programming"));
                categoriesEntity.setMisc(categories.contains("misc"));
                categoriesEntity.setDark(categories.contains("dark"));
                categoriesEntity.setPun(categories.contains("pun"));
                categoriesEntity.setSpooky(categories.contains("spooky"));
                categoriesEntity.setChristmas(categories.contains("christmas"));
                categoriesRepository.update(categoriesEntity);
            });
        }
        if (Objects.nonNull(flags)) {
            flagsRepository.findById(id).ifPresent(flagsEntity -> {

                flagsEntity.setNsfw(flags.contains("nsfw"));
                flagsEntity.setReligious(flags.contains("religious"));
                flagsEntity.setPolitical(flags.contains("political"));
                flagsEntity.setRacist(flags.contains("racist"));
                flagsEntity.setSexist(flags.contains("sexist"));
                flagsEntity.setExplicit(flags.contains("explicit"));
                flagsRepository.update(flagsEntity);
            });
        }
    }
}
