package com.hostfully.booking.api.domain.services;

import com.hostfully.booking.api.domain.ReservationType;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BlockService {
    UUID create(UUID placeId, ReservationType type, LocalDateTime startDate, LocalDateTime endDate);

    void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate);

    void delete(UUID reservationId);

    void get(UUID reservationId);
}
