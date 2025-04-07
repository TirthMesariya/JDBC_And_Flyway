package com.dev;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestContainerTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        assertThat(postgresSQLContainer.isRunning()).isTrue();
        assertThat(postgresSQLContainer.isCreated()).isTrue();
    }

}