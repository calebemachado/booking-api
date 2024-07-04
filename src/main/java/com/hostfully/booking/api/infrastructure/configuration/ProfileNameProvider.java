package com.hostfully.booking.api.infrastructure.configuration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileNameProvider {

    private final Environment environment;

    public ProfileNameProvider(Environment environment) {
        this.environment = environment;
    }

    public APIProfile getActiveProfileName() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            return APIProfile.valueOf(activeProfiles[0]);
        }
        return APIProfile.DEFAULT;
    }
}
