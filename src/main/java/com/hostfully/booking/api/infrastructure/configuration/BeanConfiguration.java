package com.hostfully.booking.api.infrastructure.configuration;

import com.hostfully.booking.api.BookingApi;
import com.hostfully.booking.api.domain.repository.PersonDAO;
import com.hostfully.booking.api.domain.repository.PlacesDAO;
import com.hostfully.booking.api.domain.repository.ReservationDAO;
import com.hostfully.booking.api.domain.services.BlockService;
import com.hostfully.booking.api.domain.services.BookingService;
import com.hostfully.booking.api.domain.services.adapter.BlockAdapter;
import com.hostfully.booking.api.domain.services.adapter.BookingAdapter;
import com.hostfully.booking.api.infrastructure.repository.PersonDAODatabase;
import com.hostfully.booking.api.infrastructure.repository.PlacesDAODatabase;
import com.hostfully.booking.api.infrastructure.repository.ReservationDAODatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan(basePackageClasses = BookingApi.class)
public class BeanConfiguration {

    @Bean
    ReservationDAO reservationDAO(final JdbcTemplate jdbcTemplate) {
        return new ReservationDAODatabase(jdbcTemplate);
    }

    @Bean
    PersonDAO personDAO(final JdbcTemplate jdbcTemplate) {
        return new PersonDAODatabase(jdbcTemplate);
    }

    @Bean
    PlacesDAO placesDAO(final JdbcTemplate jdbcTemplate) {
        return new PlacesDAODatabase(jdbcTemplate);
    }

    @Bean
    BookingService bookingService(final ReservationDAO reservationDAO, final PersonDAO personDAO, final PlacesDAO placesDAO) {
        return new BookingAdapter(reservationDAO, personDAO, placesDAO);
    }

    @Bean
    BlockService blockService(final ReservationDAO reservationDAO) {
        return new BlockAdapter(reservationDAO);
    }

}
