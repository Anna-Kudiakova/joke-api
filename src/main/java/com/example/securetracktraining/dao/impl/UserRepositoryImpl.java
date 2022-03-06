package com.example.securetracktraining.dao.impl;

import com.example.securetracktraining.dao.UserRepository;
import com.example.securetracktraining.dao.model.User;
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
        return Optional.ofNullable(database
                .find(User.class)
                .where().eq("username", username)
                .findOne()
        );
    }
}
