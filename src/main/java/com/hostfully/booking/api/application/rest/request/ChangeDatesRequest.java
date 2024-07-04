package com.hostfully.booking.api.application.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ChangeDatesRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @JsonProperty("start_date") LocalDateTime startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @JsonProperty("end_date") LocalDateTime endDate
) {
}
