package io.github.jccrisostomo.auctionhouse.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record User(
        UUID id,
        UUID organizationId,
        String username,
        Role role,
        UserStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
