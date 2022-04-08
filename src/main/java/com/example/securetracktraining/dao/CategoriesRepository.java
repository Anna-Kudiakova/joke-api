package com.example.securetracktraining.dao;

import com.example.securetracktraining.dao.model.Categories;

import java.util.Optional;

public interface CategoriesRepository {

    void save(Categories categories);

    void update(Categories categories);

    Optional<Categories> findById(Long id);

}
