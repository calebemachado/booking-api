package com.hostfully.booking.api.domain.repository;

import com.hostfully.booking.api.domain.Place;

import java.util.Optional;
import java.util.UUID;

public interface PlacesDAO {

    Optional<Place> getById(final UUID id);
}
