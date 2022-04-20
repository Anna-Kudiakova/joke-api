package com.joke_api.dao;

import com.joke_api.dao.model.Categories;

import java.util.Optional;

public interface CategoriesRepository {

    void save(Categories categories);

    void update(Categories categories);

    Optional<Categories> findById(Long id);

}
