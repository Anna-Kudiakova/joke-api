package com.example.securetracktraining.dao.impl;

import com.example.securetracktraining.dao.FlagsRepository;
import com.example.securetracktraining.dao.model.Flags;
import io.ebean.Database;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FlagsRepositoryImpl implements FlagsRepository {

    private final Database database;

    public FlagsRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public void save(Flags flags) {
        database.save(flags);
    }

    @Override
    public void update(Flags flags) {
        database.update(flags);
    }

    @Override
    public Optional<Flags> findById(Long id) {
        return Optional.ofNullable(database.find(Flags.class, id));
    }

}

