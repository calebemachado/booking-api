package com.hostfully.booking.api.domain;

import com.hostfully.booking.api.infrastructure.exception.BusinessException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Place extends BaseEntity {
    private final String address;
    private final String description;
    private final UUID ownerId;

    private Place(
            final UUID id,
            final String address,
            final String description,
            final UUID ownerId
    ) {
        super(id);
        this.address = address;
        this.description = description;
        this.ownerId = ownerId;
    }

    private Place(
            final UUID id,
            final String address,
            final String description,
            final UUID ownerId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.address = address;
        this.description = description;
        this.ownerId = ownerId;
    }

    public static Place create(
            final String address,
            final String description,
            final UUID ownerId
    ) {
        if (address == null || address.isBlank()) {
            throw new BusinessException("Invalid address");
        }

        if (ownerId == null) {
            throw new BusinessException("Place should have a owner");
        }

        return new Place(UUID.randomUUID(), address, description, ownerId);
    }

    public static Place restore(
            final UUID id,
            final String address,
            final String description,
            final UUID ownerId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        return new Place(id, address, description, ownerId, createdAt, updatedAt);
    }
}
