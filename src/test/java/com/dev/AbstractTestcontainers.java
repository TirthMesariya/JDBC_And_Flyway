package com.dev;


import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainers {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgresSQLContainer.getJdbcUrl(),
                postgresSQLContainer.getUsername(),
                postgresSQLContainer.getPassword()
        ).load();

        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgresSQLContainer =
            new PostgreSQLContainer<>("postgres:alpine")
                    .withDatabaseName("dev-dao-unit-test")
                    .withUsername("jay")
                    .withPassword("admin");



    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgresSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgresSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgresSQLContainer::getPassword
        );
    }

    private static DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create()
                .driverClassName(postgresSQLContainer.getDriverClassName())
                .url(postgresSQLContainer.getJdbcUrl())
                .username(postgresSQLContainer.getUsername())
                .password(postgresSQLContainer.getPassword());
        return builder.build();
    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    public static final Faker FAKER = new Faker();

}
