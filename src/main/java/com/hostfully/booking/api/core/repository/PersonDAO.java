package com.hostfully.booking.api.core.repository;

import com.hostfully.booking.api.core.domain.Person;

import java.util.Optional;
import java.util.UUID;

public interface PersonDAO {
    void save(final Person person);

    Optional<Person> getById(final UUID id);

    Optional<Person> getByName(final String name);

    void deleteById(final UUID id);
}
