package com.joke_api.services;


import com.joke_api.dao.CategoriesRepository;
import com.joke_api.dao.FlagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final CategoriesRepository categoriesRepository;

    private final FlagsRepository flagsRepository;

    @CachePut(value = "categories", key = "#username")
    public List<String> setCategories(Long id, List<String> categories, String username) {
        List<String> categoriesList = new ArrayList<>();
        if (Objects.nonNull(categories)) {
            categoriesRepository.findById(id).ifPresent(categoriesEntity -> {
                if(categories.contains("programming")) {
                    categoriesEntity.setProgramming(true);
                    categoriesList.add("Programming");
                } else {
                    categoriesEntity.setProgramming(false);
                }
                if(categories.contains("misc")) {
                    categoriesEntity.setMisc(true);
                    categoriesList.add("Misc");
                } else {
                    categoriesEntity.setMisc(false);
                }
                if(categories.contains("dark")) {
                    categoriesEntity.setDark(true);
                    categoriesList.add("Dark");
                } else {
                    categoriesEntity.setDark(false);
                }
                if(categories.contains("pun")) {
                    categoriesEntity.setPun(true);
                    categoriesList.add("Pun");
                } else {
                    categoriesEntity.setPun(false);
                }
                if(categories.contains("spooky")) {
                    categoriesEntity.setSpooky(true);
                    categoriesList.add("Spooky");
                } else {
                    categoriesEntity.setSpooky(false);
                }
                if(categories.contains("christmas")) {
                    categoriesEntity.setChristmas(true);
                    categoriesList.add("Christmas");
                } else {
                    categoriesEntity.setChristmas(false);
                }
                categoriesRepository.update(categoriesEntity);
            });
        }
        return categoriesList;
    }

    @CachePut(value = "flags", key = "#username")
    public List<String> setFlags(Long id, List<String> flags, String username) {
        List<String> flagsList = new ArrayList<>();
        if (Objects.nonNull(flags)) {
            flagsRepository.findById(id).ifPresent(flagsEntity -> {
                if(flags.contains("nsfw")) {
                    flagsEntity.setNsfw(true);
                    flagsList.add("nsfw");
                } else {
                    flagsEntity.setNsfw(false);
                }
                if(flags.contains("religious")) {
                    flagsEntity.setReligious(true);
                    flagsList.add("religious");
                } else {
                    flagsEntity.setReligious(false);
                }
                if(flags.contains("political")) {
                    flagsEntity.setPolitical(true);
                    flagsList.add("political");
                } else {
                    flagsEntity.setPolitical(false);
                }
                if(flags.contains("racist")) {
                    flagsEntity.setRacist(true);
                    flagsList.add("racist");
                } else {
                    flagsEntity.setRacist(false);
                }
                if(flags.contains("sexist")) {
                    flagsEntity.setSexist(true);
                    flagsList.add("sexist");
                } else {
                    flagsEntity.setSexist(false);
                }
                if(flags.contains("explicit")) {
                    flagsEntity.setExplicit(true);
                    flagsList.add("explicit");
                } else {
                    flagsEntity.setExplicit(false);
                }
                flagsRepository.update(flagsEntity);
            });
        }
        return flagsList;
    }
}
