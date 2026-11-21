CREATE TABLE organizations (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               name VARCHAR(100) NOT NULL,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT chk_organizations_name
                                   CHECK (LENGTH(TRIM(name)) > 0)
);

CREATE UNIQUE INDEX uq_organizations_name
    ON organizations (LOWER(name));
