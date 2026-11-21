
package io.github.jccrisostomo.auctionhouse.persistence;

import io.github.jccrisostomo.auctionhouse.config.DatabaseConfig;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

import java.util.concurrent.TimeUnit;

public final class DatabasePool {

    private DatabasePool() {
    }

    public static Pool create(Vertx vertx, DatabaseConfig config) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setHost(config.host())
                .setPort(config.port())
                .setDatabase(config.database())
                .setUser(config.username())
                .setPassword(config.password());

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(10)
                .setMaxWaitQueueSize(100)
                .setConnectionTimeout(5)
                .setConnectionTimeoutUnit(TimeUnit.SECONDS);

        return PgBuilder.pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
    }
}
