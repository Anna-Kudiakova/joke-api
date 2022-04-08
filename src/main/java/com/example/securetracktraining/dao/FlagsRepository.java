package com.example.securetracktraining.dao;

import com.example.securetracktraining.dao.model.Flags;

import java.util.Optional;

public interface FlagsRepository {

    void save(Flags flags);

    void update(Flags flags);

    Optional<Flags> findById(Long id);

}
