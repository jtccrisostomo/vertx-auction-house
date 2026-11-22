
package io.github.jccrisostomo.auctionhouse.model;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrganizationTest {

    @Test
    void createsOrganization() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        Organization organization = new Organization(
                id,
                "Example Trading",
                now
        );

        assertEquals(id, organization.id());
        assertEquals("Example Trading", organization.name());
        assertEquals(now, organization.createdAt());
    }
}
