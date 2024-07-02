package com.hostfully.booking.api.core.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.hostfully.booking.api.core.domain.ReservationType.BLOCK;
import static com.hostfully.booking.api.core.domain.ReservationType.BOOKING;

@Getter
public class Reservation {
    private final UUID id;
    private final UUID placeId;
    private final ReservationType type;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final UUID tenantId;

    private Reservation(
            final UUID id,
            final UUID placeId,
            final ReservationType type,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId
    ) {
        this.id = id;
        this.placeId = placeId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tenantId = tenantId;
    }

    public static Reservation create(
            final UUID placeId,
            final ReservationType type,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId
    ) {
        validateDates(startDate, endDate);
        validateReservationType(type);
        validatePlace(placeId, type);
        validateBlock(type, tenantId);
        validateBooking(type, tenantId);

        return new Reservation(UUID.randomUUID(), placeId, type, startDate, endDate, tenantId);
    }

    private static void validatePlace(UUID placeId, ReservationType type) {
        if (placeId == null) {
            throw new Error(type.name() + " reservation should have a place");
        }
    }

    private static void validateBooking(ReservationType type, UUID tenantId) {
        if (BOOKING.equals(type) && tenantId == null) {
            throw new Error("Booking reservation should have a tenant");
        }
    }

    private static void validateBlock(ReservationType type, UUID tenantId) {
        if (BLOCK.equals(type) && tenantId != null) {
            throw new Error("Block reservation should not have tenant");
        }
    }

    private static void validateReservationType(ReservationType type) {
        if (type == null) {
            throw new Error("Reservation should be of types: BLOCK or BOOKING");
        }
    }

    private static void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)
                || startDate.equals(endDate)
                || startDate.isBefore(LocalDateTime.now())
                || endDate.isBefore(LocalDateTime.now())
        ) {
            throw new Error("Dates should be valid");
        }
    }

    public static Reservation restore(
            final UUID id,
            final UUID placeId,
            final ReservationType type,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId
    ) {
        return new Reservation(id, placeId, type, startDate, endDate, tenantId);
    }
}
