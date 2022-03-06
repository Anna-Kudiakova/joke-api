package com.example.securetracktraining.dao.impl;

import com.example.securetracktraining.dao.CategoriesRepository;
import com.example.securetracktraining.dao.model.Categories;
import io.ebean.Database;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoriesRepositoryImpl implements CategoriesRepository {

    private final Database database;

    public CategoriesRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public void save(Categories categories) {
        database.save(categories);
    }

    @Override
    public void update(Categories categories) {
        database.update(categories);
    }

    @Override
    public Optional<Categories> findById(Long id) {
        return Optional.ofNullable(database.find(Categories.class, id));
    }
}
