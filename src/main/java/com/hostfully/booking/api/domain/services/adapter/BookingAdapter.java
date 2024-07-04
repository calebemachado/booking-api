package com.hostfully.booking.api.domain.services.adapter;

import com.hostfully.booking.api.application.rest.request.CreateBookingRequest;
import com.hostfully.booking.api.application.rest.response.BookingResponse;
import com.hostfully.booking.api.domain.Reservation;
import com.hostfully.booking.api.domain.ReservationType;
import com.hostfully.booking.api.domain.repository.ReservationDAO;
import com.hostfully.booking.api.domain.services.BookingService;
import com.hostfully.booking.api.infrastructure.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class BookingAdapter implements BookingService {

    private final ReservationDAO reservationDAO;

    public BookingAdapter(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public UUID create(CreateBookingRequest request) {
        Reservation reservation = Reservation.createBooking(
                request.placeId(),
                request.startDate(),
                request.endDate(),
                request.tenantId()
        );

        reservationDAO.save(reservation);

        return reservation.getId();
    }

    @Override
    public void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        Reservation changedReservation = Reservation.changeDates(reservation, startDate, endDate);
        reservationDAO.changeDates(changedReservation.getId(), changedReservation.getStartDate(), changedReservation.getEndDate());
    }

    @Override
    public void changeTenant(UUID reservationId, UUID tenantId) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        Reservation changedReservation = Reservation.changeTenant(reservation, tenantId);
        reservationDAO.changeTenant(changedReservation.getId(), changedReservation.getTenantId());
    }

    @Override
    public void cancel(UUID reservationId) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        Reservation cancelled = Reservation.cancel(reservation);
        reservationDAO.updateStatusById(cancelled.getId(), cancelled.getStatus());
    }

    @Override
    public void rebook(UUID reservationId) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        Reservation reopened = Reservation.reopen(reservation);
        reservationDAO.updateStatusById(reopened.getId(), reopened.getStatus());
    }

    @Override
    public void delete(UUID reservationId) {
        reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        reservationDAO.deleteById(reservationId);
    }

    @Override
    public Optional<BookingResponse> get(UUID reservationId) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BOOKING)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with informed id %s not found", reservationId)));

        return Optional.of(new BookingResponse(
                reservation.getId(),
                reservation.getPlaceId(),
                reservation.getType().name(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTenantId(),
                reservation.getStatus().name()
        ));
    }
}
