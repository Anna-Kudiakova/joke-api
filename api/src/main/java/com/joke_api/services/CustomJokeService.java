package com.joke_api.services;


import com.joke_api.dao.UserRepository;
import com.joke_api.dao.model.Categories;
import com.joke_api.dao.model.Flags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomJokeService {

    private final UserRepository userRepository;

    @Cacheable(value = "categories", key = "#username")
    public List<String> getCategories(String username) {
        List<String> categories = new ArrayList<>();

        userRepository.findByUsername(username).ifPresent(user -> {
            Categories categoriesEntity = user.getCategories();
            if (categoriesEntity.isProgramming()) {
                categories.add("Programming");
            }
            if (categoriesEntity.isMisc()) {
                categories.add("Misc");
            }
            if (categoriesEntity.isDark()) {
                categories.add("Dark");
            }
            if (categoriesEntity.isPun()) {
                categories.add("Pun");
            }
            if (categoriesEntity.isSpooky()) {
                categories.add("Spooky");
            }
            if (categoriesEntity.isChristmas()) {
                categories.add("Christmas");
            }
        });

        return categories;
    }

    @Cacheable(value = "flags", key = "#username")
    public List<String> getFlags(String username) {

        List<String> flags = new ArrayList<>();

        userRepository.findByUsername(username).ifPresent(user -> {
            Flags flagsEntity = user.getFlags();
            if (flagsEntity.isNsfw()) {
                flags.add("nsfw");
            }
            if (flagsEntity.isReligious()) {
                flags.add("religious");
            }
            if (flagsEntity.isPolitical()) {
                flags.add("political");
            }
            if (flagsEntity.isRacist()) {
                flags.add("racist");
            }
            if (flagsEntity.isSexist()) {
                flags.add("sexist");
            }
            if (flagsEntity.isExplicit()) {
                flags.add("explicit");
            }
        });
        return flags;
    }


}
