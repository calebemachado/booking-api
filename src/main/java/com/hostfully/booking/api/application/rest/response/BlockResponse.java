package com.hostfully.booking.api.application.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record BlockResponse(
        UUID id,
        @JsonProperty("place_id") UUID placeId,
        String type,
        @JsonProperty("start_date") LocalDateTime startDate,
        @JsonProperty("end_date") LocalDateTime endDate,
        @JsonProperty("guest_id") UUID tenantId,
        String status
) {
}
