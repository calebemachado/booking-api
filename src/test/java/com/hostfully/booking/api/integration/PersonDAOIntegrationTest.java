package com.hostfully.booking.api.integration;

import com.hostfully.booking.api.domain.Person;
import com.hostfully.booking.api.infrastructure.repository.PersonDAODatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(value = {"/database/person-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = {"/database/person-data-truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class PersonDAOIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void whenInjectInMemoryDataSource_thenReturnCorrectPersonByID() {
        PersonDAODatabase personDAODatabase = new PersonDAODatabase(jdbcTemplate);

        Optional<Person> optionalPerson = personDAODatabase
                .getById(UUID.fromString("8f32b062-38cf-11ef-a688-325096b39f47"));

        assertTrue(optionalPerson.isPresent());
        assertEquals("John", optionalPerson.get().getName());
        assertEquals("Doe", optionalPerson.get().getSurname());
        assertTrue(optionalPerson.get().getActive());
    }

    @Test
    void whenInjectInMemoryDataSource_thenReturnCorrectPersonsByName() {
        PersonDAODatabase personDAODatabase = new PersonDAODatabase(jdbcTemplate);

        List<Person> personList = personDAODatabase.getByName("John");

        assertFalse(personList.isEmpty());
        assertEquals(1, personList.size());
    }

    @Test
    void whenInjectInMemoryDataSource_thenDeletePersonById() {
        PersonDAODatabase personDAODatabase = new PersonDAODatabase(jdbcTemplate);

        personDAODatabase.deleteById(UUID.fromString("f79bd93f-8e78-43c6-ab44-b10933bd693a"));

        Optional<Person> optionalPerson = personDAODatabase
                .getById(UUID.fromString("f79bd93f-8e78-43c6-ab44-b10933bd693a"));

        assertTrue(optionalPerson.isEmpty());
    }
}
