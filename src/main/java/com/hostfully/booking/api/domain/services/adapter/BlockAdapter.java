package com.hostfully.booking.api.domain.services.adapter;

import com.hostfully.booking.api.domain.ReservationType;
import com.hostfully.booking.api.domain.repository.ReservationDAO;
import com.hostfully.booking.api.domain.services.BlockService;

import java.time.LocalDateTime;
import java.util.UUID;

public class BlockAdapter implements BlockService {

    private final ReservationDAO reservationDAO;

    public BlockAdapter(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public UUID create(UUID placeId, ReservationType type, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate) {

    }

    @Override
    public void delete(UUID reservationId) {

    }

    @Override
    public void get(UUID reservationId) {

    }
}
