package com.hostfully.booking.api.application.rest;

import com.hostfully.booking.api.application.rest.request.ChangeBookingDatesRequest;
import com.hostfully.booking.api.application.rest.request.CreateBookingRequest;
import com.hostfully.booking.api.application.rest.response.BookingResponse;
import com.hostfully.booking.api.domain.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRestController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable UUID id
    ) {
        Optional<BookingResponse> reservation = bookingService.get(id);

        if (reservation.isPresent()) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CreateBookingRequest request
    ) {
        UUID reservationId = bookingService.create(request);
        return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable UUID id
    ) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<?> changeDates(
            @PathVariable UUID id,
            @RequestBody ChangeBookingDatesRequest request
    ) {
        bookingService.changeDates(id, request.startDate(), request.endDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/guest/{guest-id}")
    public ResponseEntity<?> changeDates(
            @PathVariable UUID id,
            @PathVariable("guest-id") UUID guestId
    ) {
        bookingService.changeTenant(id, guestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable UUID id
    ) {
        bookingService.cancel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/rebook")
    public ResponseEntity<?> rebook(
            @PathVariable UUID id
    ) {
        bookingService.rebook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
