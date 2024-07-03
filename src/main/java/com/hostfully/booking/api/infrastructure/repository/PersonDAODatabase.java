package com.hostfully.booking.api.infrastructure.repository;

import com.hostfully.booking.api.domain.Person;
import com.hostfully.booking.api.domain.repository.PersonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PersonDAODatabase implements PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAODatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Person person) {
        try {
            jdbcTemplate.update("INSERT INTO persons (id, name, surname, active, created_at, updated_at) values (?,?,?,?,?,?)", person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getActive(),
                    person.getCreatedAt(),
                    person.getUpdatedAt());
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Person> getById(UUID id) {
        try {
            List<Person> person = jdbcTemplate
                    .query("SELECT id, name, surname, active, created_at, updated_at from persons where active = true and id = ?",
                            this::mapPerson,
                            id
                    );

            if (person.isEmpty()) return Optional.empty();

            return Optional.of(person.get(0));
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    @Override
    public List<Person> getByName(String name) {
        try {
            return jdbcTemplate
                    .query("SELECT id, name, surname, active, created_at, updated_at from persons where active = true and name ilike ?",
                            this::mapPerson,
                            name.toLowerCase()
                    );
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            jdbcTemplate.update("UPDATE persons SET active = false WHERE id = ?", id);
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    private Person mapPerson(ResultSet rs, int rowNum) throws SQLException {
        return Person.restore(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getBoolean("active"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
