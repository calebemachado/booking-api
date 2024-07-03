package com.hostfully.booking.api.infrastructure.repository;

import com.hostfully.booking.api.domain.Reservation;
import com.hostfully.booking.api.domain.ReservationStatus;
import com.hostfully.booking.api.domain.repository.ReservationDAO;
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
public class ReservationDAODatabase implements ReservationDAO {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDAODatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Reservation reservation) {
        try {
            jdbcTemplate.update("INSERT INTO reservations (id, place_id, type, start_date, end_date, tenant_id, status) values (?,?,?,?,?,?,?)",
                    reservation.getId(),
                    reservation.getPlaceId(),
                    reservation.getType().name(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getTenantId(),
                    reservation.getStatus().name());
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Reservation> getById(UUID id) {
        try {
            List<Reservation> reservations = jdbcTemplate
                    .query("SELECT id, place_id, type, start_date, end_date, tenant_id, status from reservations where id = ?",
                            this::mapReservation,
                            id
                    );

            if (reservations.isEmpty()) return Optional.empty();

            return Optional.of(reservations.get(0));
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            jdbcTemplate.update("UPDATE reservations SET status = ? WHERE id = ?", ReservationStatus.CLOSED.name(), id);
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void changeDates(UUID id, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            jdbcTemplate.update("UPDATE reservations SET start_date = ? , end_date = ? WHERE id = ?", startDate, endDate, id);
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void changeTenant(UUID id, UUID tenantId) {
        try {
            jdbcTemplate.update("UPDATE reservations SET tenant_id = ? WHERE id = ?", tenantId, id);
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateStatusById(UUID id, ReservationStatus status) {
        try {
            jdbcTemplate.update("UPDATE reservations SET status = ? WHERE id = ?", status.name(), id);
        } catch (DataAccessException ex) {
            log.error("Error performing database operation: " + ex.getMessage(), ex);
        }
    }

    private Reservation mapReservation(ResultSet rs, int rowNum) throws SQLException {
        return Reservation.restore(
                rs.getObject("id", UUID.class),
                rs.getObject("place_id", UUID.class),
                rs.getString("type"),
                rs.getObject("start_date", LocalDateTime.class),
                rs.getObject("end_date", LocalDateTime.class),
                rs.getObject("tenant_id", UUID.class),
                rs.getString("status")
        );
    }
}
