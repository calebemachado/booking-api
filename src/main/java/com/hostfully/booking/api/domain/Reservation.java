package com.hostfully.booking.api.domain;

import com.hostfully.booking.api.infrastructure.exception.BusinessException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.hostfully.booking.api.domain.ReservationType.BLOCK;
import static com.hostfully.booking.api.domain.ReservationType.BOOKING;

@Getter
public class Reservation {
    private final UUID id;
    private final UUID placeId;
    private final ReservationType type;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final UUID tenantId;
    private final ReservationStatus status;

    private Reservation(
            final UUID id,
            final UUID placeId,
            final ReservationType type,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId,
            ReservationStatus status) {
        this.id = id;
        this.placeId = placeId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tenantId = tenantId;
        this.status = status;
    }

    public static Reservation createBlock(
            final UUID placeId,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        validateDates(startDate, endDate);
        validateReservationType(BLOCK);
        validatePlace(placeId, BLOCK);
        validateBlock(BLOCK, null);

        return new Reservation(UUID.randomUUID(), placeId, BLOCK, startDate, endDate, null, null);
    }

    public static Reservation createBooking(
            final UUID placeId,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId
    ) {
        validateDates(startDate, endDate);
        validateReservationType(BOOKING);
        validatePlace(placeId, BOOKING);
        validateBooking(BOOKING, tenantId);

        return new Reservation(UUID.randomUUID(), placeId, BOOKING, startDate, endDate, tenantId, ReservationStatus.OPEN);
    }

    public static Reservation cancel(
            final Reservation reservation
    ) {
        if (!ReservationStatus.OPEN.equals(reservation.getStatus())) {
            throw new BusinessException(reservation.getType() + " reservation can't be canceled");
        }

        if (reservation.getStartDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Ongoing reservation can't be canceled");
        }

        return new Reservation(reservation.id, reservation.placeId, reservation.type, reservation.startDate, reservation.endDate, reservation.tenantId, ReservationStatus.CANCELED);
    }

    public static Reservation reopen(
            final Reservation reservation
    ) {
        if (!ReservationStatus.CANCELED.equals(reservation.getStatus())) {
            throw new BusinessException(reservation.getType() + " reservation can't be reopened");
        }

        validateDates(reservation.startDate, reservation.endDate);

        return new Reservation(reservation.id, reservation.placeId, reservation.type, reservation.startDate, reservation.endDate, reservation.tenantId, ReservationStatus.OPEN);
    }

    public static Reservation close(
            final Reservation reservation
    ) {
        return new Reservation(reservation.id, reservation.placeId, reservation.type, reservation.startDate, reservation.endDate, reservation.tenantId, ReservationStatus.CLOSED);
    }

    public static Reservation changeDates(
            final Reservation reservation,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        validateDates(startDate, endDate);

        return new Reservation(reservation.id, reservation.placeId, reservation.type, startDate, endDate, reservation.tenantId, reservation.status);
    }

    public static Reservation changeTenant(
            Reservation reservation,
            UUID tenantId
    ) {
        if(BLOCK.equals(reservation.getType())) {
            throw new BusinessException("Block reservation don't have guest to be changed");
        }

        if (reservation.getStartDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Ongoing reservation can't have guest changed");
        }

        return new Reservation(reservation.id, reservation.placeId, reservation.type, reservation.startDate, reservation.endDate, tenantId, reservation.status);
    }

    public static Reservation restore(
            final UUID id,
            final UUID placeId,
            final String type,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final UUID tenantId,
            final String status
    ) {
        return new Reservation(id, placeId, ReservationType.valueOf(type), startDate, endDate, tenantId, ReservationStatus.valueOf(status));
    }

    private static void validatePlace(UUID placeId, ReservationType type) {
        if (placeId == null) {
            throw new BusinessException(type.name() + " reservation should have a place");
        }
    }

    private static void validateBooking(ReservationType type, UUID tenantId) {
        if (BOOKING.equals(type) && tenantId == null) {
            throw new BusinessException("Booking reservation should have a tenant");
        }
    }

    private static void validateBlock(ReservationType type, UUID tenantId) {
        if (BLOCK.equals(type) && tenantId != null) {
            throw new BusinessException("Block reservation should not have tenant");
        }
    }

    private static void validateReservationType(ReservationType type) {
        if (type == null) {
            throw new BusinessException("Reservation should be of types: BLOCK or BOOKING");
        }
    }

    private static void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)
                || startDate.equals(endDate)
                || startDate.isBefore(LocalDateTime.now())
                || endDate.isBefore(LocalDateTime.now())
        ) {
            throw new BusinessException("Dates should be valid");
        }
    }
}
