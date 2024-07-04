package com.hostfully.booking.api.domain;

import java.util.UUID;

public record Place(
        UUID id,
        String address,
        String description,
        UUID ownerId
) {
}
