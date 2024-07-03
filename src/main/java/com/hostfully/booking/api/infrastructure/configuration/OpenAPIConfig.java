package com.hostfully.booking.api.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("calebelsm70@gmail.com");
        contact.setName("Calebe Machado");
        contact.setUrl("https://github.com/calebemachado");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Hostfully Booking API")
                .version("1.0")
                .contact(contact)
                .description("The BookingAPI is a RESTful service for managing bookings.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info);
    }
}
