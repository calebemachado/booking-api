package com.hostfully.booking.api.infrastructure.configuration;

import com.hostfully.booking.api.BookingApi;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BookingApi.class)
public class BeanConfiguration {


}
