package com.hostfully.booking.api.domain.services.adapter;

import com.hostfully.booking.api.application.rest.request.CreateBlockRequest;
import com.hostfully.booking.api.application.rest.response.BlockResponse;
import com.hostfully.booking.api.domain.Reservation;
import com.hostfully.booking.api.domain.ReservationType;
import com.hostfully.booking.api.domain.repository.ReservationDAO;
import com.hostfully.booking.api.domain.services.BlockService;
import com.hostfully.booking.api.infrastructure.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class BlockAdapter implements BlockService {

    private final ReservationDAO reservationDAO;

    public BlockAdapter(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public UUID create(CreateBlockRequest request) {
        Reservation reservation = Reservation.createBlock(
                request.placeId(),
                request.startDate(),
                request.endDate()
        );

        reservationDAO.save(reservation);

        return reservation.getId();
    }

    @Override
    public void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BLOCK)
                .orElseThrow(() -> new NotFoundException(String.format("Block with informed id %s not found", reservationId)));

        Reservation changedReservation = Reservation.changeDates(reservation, startDate, endDate);
        reservationDAO.changeDates(changedReservation.getId(), changedReservation.getStartDate(), changedReservation.getEndDate());
    }

    @Override
    public void delete(UUID reservationId) {
        reservationDAO.getById(reservationId, ReservationType.BLOCK)
                .orElseThrow(() -> new NotFoundException(String.format("Block with informed id %s not found", reservationId)));

        reservationDAO.deleteById(reservationId);
    }

    @Override
    public Optional<BlockResponse> get(UUID reservationId) {
        Reservation reservation = reservationDAO.getById(reservationId, ReservationType.BLOCK)
                .orElseThrow(() -> new NotFoundException(String.format("Block with informed id %s not found", reservationId)));

        return Optional.of(new BlockResponse(
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
