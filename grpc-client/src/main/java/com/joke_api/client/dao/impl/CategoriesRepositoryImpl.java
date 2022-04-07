package com.joke_api.client.dao.impl;

import com.joke_api.client.dao.CategoriesRepository;
import com.joke_api.client.dao.model.Categories;
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
