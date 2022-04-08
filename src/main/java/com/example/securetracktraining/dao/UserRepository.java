package com.example.securetracktraining.dao;

import com.example.securetracktraining.dao.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

}
