
package io.github.jccrisostomo.auctionhouse;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuctionHouseVerticleTest {

    private Vertx vertx;
    private AuctionHouseVerticle verticle;

    @BeforeEach
    void setUp() throws Exception {
        vertx = Vertx.vertx();
        DatabaseConfig databaseConfig = new DatabaseConfig(
                "127.0.0.1",
                5432,
                "test_db",
                "test_user",
                "test_only"
        );

        verticle = new AuctionHouseVerticle(0, databaseConfig);

        vertx.deployVerticle(verticle)
                .toCompletionStage()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() throws Exception {
        vertx.close()
                .toCompletionStage()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);
    }

    @Test
    void healthEndpointReturnsUp() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + verticle.actualPort() + "/health"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            assertEquals(200, response.statusCode());
            assertEquals("UP", new JsonObject(response.body()).getString("status"));
        }
    }
}
