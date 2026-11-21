
package io.github.jccrisostomo.auctionhouse;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.vertx.core.Vertx;

public class AuctionHouseApplication {

    public static void main(String[] args) {
        DatabaseConfig databaseConfig =
                DatabaseConfig.fromEnvironment(System.getenv());

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(
                        new AuctionHouseVerticle(8080, databaseConfig)
                )
                .onSuccess(id ->
                        System.out.println("Auction House started on port 8080"))
                .onFailure(error -> {
                    error.printStackTrace();
                    vertx.close();
                });

        Runtime.getRuntime().addShutdownHook(
                new Thread(() ->
                        vertx.close()
                                .toCompletionStage()
                                .toCompletableFuture()
                                .join()
                )
        );
    }
}
