package com.joke_api.client.dao;

import com.joke_api.client.dao.model.Flags;

import java.util.Optional;

public interface FlagsRepository {

    void save(Flags flags);

    void update(Flags flags);

    Optional<Flags> findById(Long id);

}
