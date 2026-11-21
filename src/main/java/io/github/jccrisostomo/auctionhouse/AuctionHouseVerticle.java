
package io.github.jccrisostomo.auctionhouse;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.github.jccrisostomo.auctionhouse.persistence.DatabasePool;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;

import java.util.concurrent.TimeUnit;

public class AuctionHouseVerticle extends VerticleBase {

    private final int port;
    private final DatabaseConfig databaseConfig;

    private int actualPort;
    private Pool databasePool;

    public AuctionHouseVerticle(int port, DatabaseConfig databaseConfig) {
        this.port = port;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Future<?> start() {
        databasePool = DatabasePool.create(vertx, databaseConfig);

        Router router = Router.router(vertx);

        router.get("/health").handler(this::handleHealth);
        router.get("/ready").handler(this::handleReadiness);

        return vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(server -> actualPort = server.actualPort());
    }

    private void handleHealth(RoutingContext ctx) {
        sendStatus(ctx, 200, "UP");
    }

    private void handleReadiness(RoutingContext ctx) {
        databasePool.query("SELECT 1")
                .execute()
                .timeout(5, TimeUnit.SECONDS)
                .onSuccess(rows -> sendStatus(ctx, 200, "UP"))
                .onFailure(error -> sendStatus(ctx, 503, "DOWN"));
    }

    private void sendStatus(
            RoutingContext ctx,
            int statusCode,
            String status
    ) {
        JsonObject response = new JsonObject()
                .put("status", status);

        ctx.response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json")
                .end(response.encode());
    }

    @Override
    public Future<?> stop() {
        return databasePool == null
                ? Future.succeededFuture()
                : databasePool.close();
    }

    public int actualPort() {
        return actualPort;
    }
}
