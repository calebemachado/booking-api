package com.hostfully.booking.api.domain.repository;

import com.hostfully.booking.api.domain.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDAO {
    void save(final Person person);

    Optional<Person> getById(final UUID id);

    List<Person> getByName(final String name);

    void deleteById(final UUID id);
}
