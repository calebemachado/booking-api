package com.hostfully.booking.api.domain.repository;

import com.hostfully.booking.api.domain.Reservation;
import com.hostfully.booking.api.domain.ReservationStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ReservationDAO {
    void save(final Reservation reservation);

    Optional<Reservation> getById(final UUID id);

    void deleteById(final UUID id);

    void changeDates(final UUID id, LocalDateTime startDate, LocalDateTime endDate);

    void changeTenant(final UUID id, final UUID tenantId);

    void updateStatusById(final UUID id, final ReservationStatus status);
}
