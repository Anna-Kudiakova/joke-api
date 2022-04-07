package com.joke_api.client.dao;

import com.joke_api.client.dao.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

}
