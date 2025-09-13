package com.modding.mp.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt) {

    public record Jwt(
        String issuer,
        String audience,
        @DurationUnit(ChronoUnit.MINUTES) Duration accessMins,
        @DurationUnit(ChronoUnit.DAYS) Duration refreshDays,
        String secretB64
    ) {}
}