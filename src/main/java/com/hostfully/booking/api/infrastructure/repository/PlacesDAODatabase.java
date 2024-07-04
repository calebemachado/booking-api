package com.hostfully.booking.api.infrastructure.repository;

import com.hostfully.booking.api.domain.Place;
import com.hostfully.booking.api.domain.repository.PlacesDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class PlacesDAODatabase implements PlacesDAO {

    private final JdbcTemplate jdbcTemplate;

    public PlacesDAODatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Place> getById(UUID id) {
        try {
            List<Place> places = jdbcTemplate
                    .query("SELECT id, address, description, created_at, updated_at, owner_id from places where id = ?",
                            this::mapPlaces,
                            id
                    );

            if (places.isEmpty()) return Optional.empty();

            return Optional.of(places.get(0));
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    private Place mapPlaces(ResultSet rs, int rowNum) throws SQLException {
        return Place.restore(
                rs.getObject("id", UUID.class),
                rs.getString("address"),
                rs.getString("description"),
                rs.getObject("owner_id", UUID.class),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
