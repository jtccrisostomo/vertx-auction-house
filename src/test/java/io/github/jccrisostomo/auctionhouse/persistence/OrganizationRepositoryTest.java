
package io.github.jccrisostomo.auctionhouse.persistence;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.github.jccrisostomo.auctionhouse.model.Organization;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class OrganizationRepositoryTest {

    @Container
    private static final PostgreSQLContainer POSTGRES =
        new PostgreSQLContainer("postgres:17")
            .withDatabaseName("auction_house_test")
            .withUsername("test_user")
            .withPassword("test_password");

    private Vertx vertx;
    private Pool pool;
    private OrganizationRepository repository;

    @BeforeAll
    static void migrateDatabase() {
        Flyway.configure()
            .dataSource(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
            )
            .locations("classpath:db/migration")
            .cleanDisabled(true)
            .load()
            .migrate();
    }

    @BeforeEach
    void setUp() {
        vertx = Vertx.vertx();

        DatabaseConfig config = new DatabaseConfig(
            POSTGRES.getHost(),
            POSTGRES.getMappedPort(5432),
            POSTGRES.getDatabaseName(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );

        pool = DatabasePool.create(vertx, config);
        repository = new OrganizationRepository(pool);
    }

    @AfterEach
    void tearDown() throws Exception {
        try {
            pool.close()
                .toCompletionStage()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);
        } finally {
            vertx.close()
                .toCompletionStage()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    void createsAndRetrievesOrganization() throws Exception {
        String name = "Trading " + UUID.randomUUID();

        Organization created = repository.create(name)
            .toCompletionStage()
            .toCompletableFuture()
            .get(10, TimeUnit.SECONDS);

        Optional<Organization> result = repository.findById(created.id())
            .toCompletionStage()
            .toCompletableFuture()
            .get(10, TimeUnit.SECONDS);

        assertTrue(result.isPresent());

        Organization found = result.orElseThrow();

        assertNotNull(created.id());
        assertNotNull(created.createdAt());
        assertEquals(name, created.name());
        assertEquals(created, found);
    }

    @Test
    void returnsEmptyWhenOrganizationDoesNotExist() throws Exception {
        Optional<Organization> result = repository.findById(
            UUID.randomUUID()
        )
            .toCompletionStage()
            .toCompletableFuture()
            .get(10, TimeUnit.SECONDS);

        assertTrue(result.isEmpty());
    }
}
