package com.hostfully.booking.api.unit;

import com.hostfully.booking.api.domain.Person;
import com.hostfully.booking.api.infrastructure.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonTest {

    public static final String INVALID_NAME_MSG = "Invalid name";
    public static final String INVALID_SURNAME_MSG = "Invalid surname";

    @Test
    void shouldCreatePerson() {
        Person person = Person.create("John", "Doe");
        assertThat(person.getId()).isNotNull();
        assertThat(person.getCreatedAt()).isNotNull();
        assertThat(person.getUpdatedAt()).isNotNull();
        assertTrue(person.getActive());
    }

    @Test
    void shouldNotCreatePersonWithInvalidNames() {
        BusinessException error = assertThrows(BusinessException.class, () -> Person.create("", "Doe"));
        assertEquals(INVALID_NAME_MSG, error.getMessage());

        error = assertThrows(BusinessException.class, () -> Person.create("John", ""));
        assertEquals(INVALID_SURNAME_MSG, error.getMessage());

        error = assertThrows(BusinessException.class, () -> Person.create("John", "Doe1"));
        assertEquals(INVALID_SURNAME_MSG, error.getMessage());

        error = assertThrows(BusinessException.class, () -> Person.create("John21312", "Doe1"));
        assertEquals(INVALID_NAME_MSG, error.getMessage());
    }

    @Test
    public void testRestore() {
        UUID id = UUID.randomUUID();
        String name = "John";
        String surname = "Doe";
        Boolean active = true;
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);

        Person person = Person.restore(id, name, surname, active, createdAt, updatedAt);

        assertEquals(id, person.getId());
        assertEquals(name, person.getName());
        assertEquals(surname, person.getSurname());
        assertEquals(active, person.getActive());
        assertEquals(createdAt, person.getCreatedAt());
        assertEquals(updatedAt, person.getUpdatedAt());
    }
}
