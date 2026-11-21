package io.github.jccrisostomo.auctionhouse;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class DatabaseIntegrationTest {

    @Container
    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:17")
                    .withDatabaseName("auction_house_test")
                    .withUsername("test_user")
                    .withPassword("test_password");

    @BeforeAll
    static void migrateDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        POSTGRES.getJdbcUrl(),
                        POSTGRES.getUsername(),
                        POSTGRES.getPassword()
                )
                .locations("classpath:db/migration")
                .cleanDisabled(true)
                .load();

        flyway.migrate();
        flyway.validate();
    }

    @Test
    void migrationsCreateRequiredTables() throws Exception {
        String sql = """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = 'public'
              AND table_name IN ('organizations', 'users')
            """;

        try (
                var connection = DriverManager.getConnection(
                        POSTGRES.getJdbcUrl(),
                        POSTGRES.getUsername(),
                        POSTGRES.getPassword()
                );
                var statement = connection.prepareStatement(sql);
                var result = statement.executeQuery()
        ) {
            assertTrue(result.next());
            assertEquals(2, result.getInt(1));
        }
    }

    @Test
    void readinessEndpointReturnsUpWhenDatabaseIsAvailable()
            throws Exception {

        DatabaseConfig config = new DatabaseConfig(
                POSTGRES.getHost(),
                POSTGRES.getMappedPort(5432),
                POSTGRES.getDatabaseName(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        );

        Vertx vertx = Vertx.vertx();

        try {
            AuctionHouseVerticle verticle =
                    new AuctionHouseVerticle(0, config);

            vertx.deployVerticle(verticle)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "http://localhost:" + verticle.actualPort() + "/ready"
                    ))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                assertEquals(200, response.statusCode());
                assertEquals(
                        "UP",
                        new JsonObject(response.body()).getString("status")
                );
            }
        } finally {
            vertx.close()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);
        }
    }
}
