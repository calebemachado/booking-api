package com.hostfully.booking.api.integration;

import com.hostfully.booking.api.application.rest.request.CreateBookingRequest;
import com.hostfully.booking.api.domain.services.adapter.BookingAdapter;
import com.hostfully.booking.api.infrastructure.exception.BusinessException;
import com.hostfully.booking.api.infrastructure.exception.NotFoundException;
import com.hostfully.booking.api.infrastructure.repository.PersonDAODatabase;
import com.hostfully.booking.api.infrastructure.repository.PlacesDAODatabase;
import com.hostfully.booking.api.infrastructure.repository.ReservationDAODatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(value = {"/database/person-data.sql", "/database/place-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class BookingServiceIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private BookingAdapter bookingAdapter;

    @BeforeEach
    public void init() {
        ReservationDAODatabase reservationDAO = new ReservationDAODatabase(jdbcTemplate);
        PersonDAODatabase personDAO = new PersonDAODatabase(jdbcTemplate);
        PlacesDAODatabase placesDAO = new PlacesDAODatabase(jdbcTemplate);
        bookingAdapter = new BookingAdapter(reservationDAO, personDAO, placesDAO);
    }

    @Test
    void whenBookingIsCreated_shouldThrowException_invalidTenantId() {
        UUID randomUUID = UUID.randomUUID();
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                randomUUID
        );

        NotFoundException error = assertThrows(NotFoundException.class, () -> bookingAdapter.create(createBookingRequest));
        assertEquals(String.format("Person with informed id %s not found", randomUUID), error.getMessage());
    }

    @Test
    void whenBookingIsCreated_shouldThrowException_invalidPlaceId() {
        UUID randomUUID = UUID.randomUUID();
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                randomUUID,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        NotFoundException error = assertThrows(NotFoundException.class, () -> bookingAdapter.create(createBookingRequest));
        assertEquals(String.format("Place with informed id %s not found", randomUUID), error.getMessage());
    }

    @Sql(value = {"/database/block.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/block-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void whenBlockWithSameDatesExists_shouldNotCreateBooking() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 7, 21, 5, 59, 59),
                LocalDateTime.of(2025, 7, 28, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        BusinessException error = assertThrows(BusinessException.class, () -> bookingAdapter.create(createBookingRequest));
        assertEquals("Can't do booking because place is not available", error.getMessage());
    }

    @Sql(value = {"/database/block.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/block-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void shouldCreateBooking() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);
    }

    @Test
    void shouldNotChangeDates_invalidStartDate() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        BusinessException error = assertThrows(BusinessException.class, () -> bookingAdapter.changeDates(
                uuid,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59)));
        assertEquals("Dates should be valid", error.getMessage());
    }

    @Sql(value = {"/database/block.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/block-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void whenReservationWithSameDatesExists_shouldNotChangeDates() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        BusinessException error = assertThrows(BusinessException.class, () -> bookingAdapter.changeDates(
                uuid,
                LocalDateTime.of(2025, 7, 21, 5, 59, 59),
                LocalDateTime.of(2025, 7, 28, 5, 59, 59)
        ));
        assertEquals("Can't do booking because place is not available", error.getMessage());
    }

    @Test
    void whenTenantDontExists_shouldNotChangeTenants() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        UUID randomUUID = UUID.randomUUID();
        NotFoundException error = assertThrows(NotFoundException.class, () -> bookingAdapter.changeTenant(uuid, randomUUID));
        assertEquals(String.format("Person with informed id %s not found", randomUUID), error.getMessage());
    }

    @Test
    void whenTenantExists_shouldChangeTenants() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        UUID janeSmithUUID = UUID.fromString("dadee8e6-38cf-11ef-860f-325096b39f47");
        bookingAdapter.changeTenant(uuid, janeSmithUUID);
    }

    @Test
    void shouldCancelReservation() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        bookingAdapter.cancel(uuid);
    }

    @Test
    void shouldDeleteReservation() {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                UUID.fromString("48a10f26-39ac-11ef-9ffb-325096b39f47"),
                LocalDateTime.of(2025, 3, 15, 5, 59, 59),
                LocalDateTime.of(2025, 3, 18, 5, 59, 59),
                UUID.fromString("ea0bc988-38cf-11ef-803c-325096b39f47")
        );

        UUID uuid = bookingAdapter.create(createBookingRequest);
        assertNotNull(uuid);

        bookingAdapter.delete(uuid);

        NotFoundException error = assertThrows(NotFoundException.class, () -> bookingAdapter.get(uuid));
        assertEquals(String.format("Booking with informed id %s not found", uuid), error.getMessage());
    }
}
