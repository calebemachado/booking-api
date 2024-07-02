package com.hostfully.booking.api.core.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
public class Person extends BaseEntity {

    private final String name;
    private final String surname;
    private final Boolean active;

    private Person(
            final UUID id,
            final String name,
            final String surname,
            final Boolean active
    ) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.active = active;
    }

    private Person(
            final UUID id,
            final String name,
            final String surname,
            final Boolean active,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.surname = surname;
        this.active = active;
    }

    public static Person create(
            final String name,
            final String surname
    ) {
        Pattern pattern = Pattern.compile("^[\\p{L} '-]+$");

        if (name == null || name.isBlank() || !pattern.matcher(name).matches()) {
            throw new Error("Invalid name");
        }

        if (surname == null || surname.isBlank() || !pattern.matcher(surname).matches()) {
            throw new Error("Invalid surname");
        }

        return new Person(UUID.randomUUID(), name, surname, true);
    }

    public static Person restore(
            final UUID id,
            final String name,
            final String surname,
            final Boolean active,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        return new Person(id, name, surname, active, createdAt, updatedAt);
    }
}
