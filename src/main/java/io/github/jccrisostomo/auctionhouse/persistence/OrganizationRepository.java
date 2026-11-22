
package io.github.jccrisostomo.auctionhouse.persistence;

import io.github.jccrisostomo.auctionhouse.model.Organization;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.Optional;
import java.util.UUID;

public class OrganizationRepository {

    private final Pool pool;

    public OrganizationRepository(Pool pool) {
        this.pool = pool;
    }

    public Future<Organization> create(String name) {
        String sql = """
            INSERT INTO organizations (name)
            VALUES ($1)
            RETURNING id, name, created_at
            """;

        return pool.preparedQuery(sql)
                .execute(Tuple.of(name))
                .map(rows -> toOrganization(rows.iterator().next()));
    }

    public Future<Optional<Organization>> findById(UUID id) {
        String sql = """
            SELECT id, name, created_at
            FROM organizations
            WHERE id = $1
            """;

        return pool.preparedQuery(sql)
                .execute(Tuple.of(id))
                .map(rows -> {
                    if (rows.size() == 0) {
                        return Optional.<Organization>empty();
                    }

                    return Optional.of(
                            toOrganization(rows.iterator().next())
                    );
                });
    }

    private Organization toOrganization(Row row) {
        return new Organization(
                row.getUUID("id"),
                row.getString("name"),
                row.getOffsetDateTime("created_at")
        );
    }
}
