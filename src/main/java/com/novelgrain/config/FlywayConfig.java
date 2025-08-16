package com.novelgrain.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ensures that the Flyway schema history is repaired before running migrations.
 * This automatically clears out entries for failed migrations which can prevent
 * the application from starting.
 */
@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy repairMigrationStrategy() {
        return flyway -> {
            // Repair any failed migrations left from previous runs
            flyway.repair();
            // Then continue with the normal migration process
            flyway.migrate();
        };
    }
}
