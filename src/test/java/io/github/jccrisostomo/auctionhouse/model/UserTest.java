
package io.github.jccrisostomo.auctionhouse.model;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    @Test
    void createsRegularUser() {
        UUID id = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(
                id,
                organizationId,
                "trader",
                Role.USER,
                UserStatus.ACTIVE,
                now,
                now
        );

        assertEquals(id, user.id());
        assertEquals(organizationId, user.organizationId());
        assertEquals("trader", user.username());
        assertEquals(Role.USER, user.role());
        assertEquals(UserStatus.ACTIVE, user.status());
        assertEquals(now, user.createdAt());
        assertEquals(now, user.updatedAt());
    }

    @Test
    void createsAdminWithoutOrganization() {
        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(
                UUID.randomUUID(),
                null,
                "admin",
                Role.ADMIN,
                UserStatus.ACTIVE,
                now,
                now
        );

        assertEquals(Role.ADMIN, user.role());
        assertNull(user.organizationId());
    }

    @Test
    void representsBlockedUser() {
        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "trader",
                Role.USER,
                UserStatus.BLOCKED,
                now,
                now
        );

        assertEquals(UserStatus.BLOCKED, user.status());
    }
}
