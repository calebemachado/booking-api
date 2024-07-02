package com.hostfully.booking.api.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Reservation(
        UUID id,
        Place place,
        ReservationType type,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Person tenant
) {
}
