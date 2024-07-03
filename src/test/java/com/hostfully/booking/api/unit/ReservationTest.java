package com.hostfully.booking.api.unit;

import com.hostfully.booking.api.domain.Reservation;
import com.hostfully.booking.api.domain.ReservationStatus;
import com.hostfully.booking.api.domain.ReservationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationTest {

    @Test
    void shouldCreateReservation() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId);

        assertThat(reservation.getId()).isNotNull();
        assertEquals(reservation.getType(), ReservationType.BOOKING);
        assertEquals(reservation.getStartDate(), startDate);
        assertEquals(reservation.getEndDate(), endDate);
    }

    @Test
    void shouldNotCreateReservationWithInvalidType() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, null, startDate, endDate, tenantId));
        assertEquals("Reservation should be of types: BLOCK or BOOKING", error.getMessage());
    }

    @Test
    void shouldNotCreateReservationWithStartDateBeforeNow() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDate = startDate.plusDays(5);
        UUID tenantId = UUID.randomUUID();

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Test
    void shouldNotCreateReservationWithEndDateBeforeNow() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(1);
        UUID tenantId = UUID.randomUUID();

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Test
    void shouldNotCreateReservationWithEqualDates() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(3);
        LocalDateTime endDate = startDate;
        UUID tenantId = UUID.randomUUID();

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Test
    void shouldNotCreateReservationWithStartDateAfterEndDate() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(15);
        LocalDateTime endDate = startDate.minusMinutes(5);
        UUID tenantId = UUID.randomUUID();

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Test
    void shouldNotCreateBookingReservationWithInvalidPlace() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();
        ReservationType type = ReservationType.BOOKING;

        Error error = assertThrows(Error.class, () -> Reservation.create(null, type, startDate, endDate, tenantId));
        assertEquals(type.name() + " reservation should have a place", error.getMessage());
    }

    @Test
    void shouldNotCreateBlockReservationWithInvalidPlace() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();
        ReservationType type = ReservationType.BLOCK;

        Error error = assertThrows(Error.class, () -> Reservation.create(null, type, startDate, endDate, tenantId));
        assertEquals(type.name() + " reservation should have a place", error.getMessage());
    }

    @Test
    void shouldNotCreateBlockReservationWithTenant() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();
        ReservationType type = ReservationType.BLOCK;

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, type, startDate, endDate, tenantId));
        assertEquals("Block reservation should not have tenant", error.getMessage());
    }

    @Test
    void shouldNotCreateBookingReservationWithoutTenant() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        ReservationType type = ReservationType.BOOKING;

        Error error = assertThrows(Error.class, () -> Reservation.create(placeId, type, startDate, endDate, null));
        assertEquals("Booking reservation should have a tenant", error.getMessage());
    }

    @Test
    void shouldNotCancelReservationWhenStatusIsNotOpen() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        ReservationType type = ReservationType.BOOKING;
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, type, startDate, endDate, tenantId);
        Reservation closedReservation = Reservation.close(reservation);

        Error error = assertThrows(Error.class, () -> Reservation.cancel(closedReservation));
        assertEquals(reservation.getType() + " reservation can't be canceled", error.getMessage());
    }

    @Test
    void shouldCancelReservationWhenStatusIsOpen() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        ReservationType type = ReservationType.BOOKING;
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, type, startDate, endDate, tenantId);

        Reservation cancelled = Reservation.cancel(reservation);
        assertEquals(ReservationStatus.CANCELED, cancelled.getStatus());
    }

    @Test
    void shouldReopenReservationWhenStatusIsCanceled() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        ReservationType type = ReservationType.BOOKING;
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, type, startDate, endDate, tenantId);
        Reservation cancelled = Reservation.cancel(reservation);
        Reservation reopened = Reservation.reopen(cancelled);

        assertEquals(ReservationStatus.OPEN, reopened.getStatus());
    }

    @Test
    void shouldNotReopenReservationWhenStatusIsNotCanceled() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        ReservationType type = ReservationType.BOOKING;
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, type, startDate, endDate, tenantId);

        Error error = assertThrows(Error.class, () -> Reservation.reopen(reservation));
        assertEquals(reservation.getType() + " reservation can't be reopened", error.getMessage());
    }

    @Test
    void shouldNotChangeReservationDatesWithStartDateAfterEndDate() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId);

        LocalDateTime startDate2 = LocalDateTime.now().plusMinutes(15);
        LocalDateTime endDate2 = startDate2.minusMinutes(5);

        Error error = assertThrows(Error.class, () -> Reservation.changeDates(reservation, startDate2, endDate2));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Test
    void shouldChangeReservationDates() {
        UUID placeId = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(1);
        UUID tenantId = UUID.randomUUID();

        Reservation reservation = Reservation.create(placeId, ReservationType.BOOKING, startDate, endDate, tenantId);

        LocalDateTime startDate2 = startDate.plusDays(1);
        LocalDateTime endDate2 = startDate2.plusDays(5);

        Reservation changedDatesReservation = Reservation.changeDates(reservation, startDate2, endDate2);
        assertEquals(startDate2, changedDatesReservation.getStartDate());
        assertEquals(endDate2, changedDatesReservation.getEndDate());
        assertEquals(ReservationStatus.OPEN, changedDatesReservation.getStatus());
        assertEquals(ReservationType.BOOKING, changedDatesReservation.getType());
        assertEquals(reservation.getId(), changedDatesReservation.getId());
    }
}
