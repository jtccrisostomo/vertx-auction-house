
package io.github.jccrisostomo.auctionhouse;

import io.vertx.core.Vertx;

public class AuctionHouseApplication {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new AuctionHouseVerticle(8080))
                .onSuccess(id -> System.out.println("Auction House started on port 8080"))
                .onFailure(error -> {
                    error.printStackTrace();
                    vertx.close();
                });
    }
}
