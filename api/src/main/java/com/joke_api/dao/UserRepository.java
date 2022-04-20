package com.joke_api.dao;

import com.joke_api.dao.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

}
