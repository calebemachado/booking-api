package com.hostfully.booking.api.application.rest;

import com.hostfully.booking.api.application.rest.request.ChangeDatesRequest;
import com.hostfully.booking.api.application.rest.request.CreateBlockRequest;
import com.hostfully.booking.api.application.rest.response.BlockResponse;
import com.hostfully.booking.api.domain.services.BlockService;
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
@RequestMapping("/api/v1/blocks")
public class BlockRestController {

    private final BlockService blockService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable UUID id
    ) {
        Optional<BlockResponse> reservation = blockService.get(id);

        if (reservation.isPresent()) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CreateBlockRequest request
    ) {
        UUID reservationId = blockService.create(request);
        return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable UUID id
    ) {
        blockService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<?> changeDates(
            @PathVariable UUID id,
            @RequestBody ChangeDatesRequest request
    ) {
        blockService.changeDates(id, request.startDate(), request.endDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
