package com.joke_api.dao;

import com.joke_api.dao.model.Flags;

import java.util.Optional;

public interface FlagsRepository {

    void save(Flags flags);

    void update(Flags flags);

    Optional<Flags> findById(Long id);

}
