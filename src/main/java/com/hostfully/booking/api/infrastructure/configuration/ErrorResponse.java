package com.hostfully.booking.api.infrastructure.configuration;

import java.util.Date;

public record ErrorResponse(
        int statusCode,
        Date timestamp,
        String message,
        String details
) {
}
