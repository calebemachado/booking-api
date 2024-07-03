package com.hostfully.booking.api.domain.services;

import com.hostfully.booking.api.application.rest.request.CreateBookingRequest;
import com.hostfully.booking.api.application.rest.response.BookingResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {

    UUID create(CreateBookingRequest createBookingRequest);

    void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate);

    void changeTenant(UUID reservationId, UUID tenantId);

    void cancel(UUID reservationId);

    void rebook(UUID reservationId);

    void delete(UUID reservationId);

    Optional<BookingResponse> get(UUID reservationId);
}
