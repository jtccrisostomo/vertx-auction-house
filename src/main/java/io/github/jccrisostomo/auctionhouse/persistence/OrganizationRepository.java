package io.github.jccrisostomo.auctionhouse.persistence;

import io.github.jccrisostomo.auctionhouse.model.Organization;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;

import java.util.Optional;
import java.util.UUID;

public class OrganizationRepository {

    public OrganizationRepository(Pool pool) {
    }

    public Future<Organization> create(String name) {
        return Future.failedFuture(
                new UnsupportedOperationException("Not implemented")
        );
    }

    public Future<Optional<Organization>> findById(UUID id) {
        return Future.failedFuture(
                new UnsupportedOperationException("Not implemented")
        );
    }
}
