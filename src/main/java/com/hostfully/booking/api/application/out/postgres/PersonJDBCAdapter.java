package com.hostfully.booking.api.application.out.postgres;

import com.hostfully.booking.api.core.domain.Person;
import com.hostfully.booking.api.core.repository.PersonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PersonJDBCAdapter implements PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonJDBCAdapter(JdbcTemplate jdbcTemplate) {
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
            return Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject("SELECT id, name, surname, active, created_at, updated_at from persons where active = true and id = ?",
                                    this::mapPerson,
                                    id
                            ));
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Optional.empty();
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

    @Override
    public Optional<Person> getByName(String name) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate
                            .queryForObject("SELECT id, name, surname, active, created_at, updated_at from persons where active = true and name ilike ?",
                                    this::mapPerson,
                                    name.toLowerCase()
                            ));
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Optional.empty();
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
}
