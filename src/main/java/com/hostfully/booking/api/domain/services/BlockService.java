package com.hostfully.booking.api.domain.services;

import com.hostfully.booking.api.application.rest.request.CreateBlockRequest;
import com.hostfully.booking.api.application.rest.response.BlockResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BlockService {
    UUID create(CreateBlockRequest blockRequest);

    void changeDates(UUID reservationId, LocalDateTime startDate, LocalDateTime endDate);

    void delete(UUID reservationId);

    Optional<BlockResponse> get(UUID reservationId);
}
