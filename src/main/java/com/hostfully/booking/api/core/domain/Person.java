package com.hostfully.booking.api.core.domain;

import java.util.UUID;

public record Person(
        UUID id,
        String name,
        String surname
) {
}
