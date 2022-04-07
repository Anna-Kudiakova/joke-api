package com.joke_api.client.dao.impl;

import com.joke_api.client.dao.UserRepository;
import com.joke_api.client.dao.model.User;
import io.ebean.Database;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Database database;

    public UserRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return database.find(User.class)
                .where().eq("username", username)
                .findOneOrEmpty();
    }
}
