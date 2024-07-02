package com.hostfully.booking.api.core.domain;

import java.util.UUID;

public record Place(
        UUID id,
        String address,
        String description,
        Person owner
) {
}
