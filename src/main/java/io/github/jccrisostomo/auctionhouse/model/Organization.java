package io.github.jccrisostomo.auctionhouse.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Organization(
        UUID id,
        String name,
        OffsetDateTime createdAt
) {
}

