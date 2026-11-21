
package io.github.jccrisostomo.auctionhouse;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class AuctionHouseVerticle extends VerticleBase {

    private final int port;
    private int actualPort;

    public AuctionHouseVerticle(int port) {
        this.port = port;
    }

    @Override
    public Future<?> start() {
        Router router = Router.router(vertx);

        router.get("/health").handler(ctx -> {
            JsonObject response = new JsonObject()
                    .put("status", "UP");

            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(response.encode());
        });

        return vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(server -> actualPort = server.actualPort());
    }

    public int actualPort() {
        return actualPort;
    }
}
