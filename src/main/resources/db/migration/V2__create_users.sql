CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       organization_id UUID,
                       username VARCHAR(50) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_users_organization
                           FOREIGN KEY (organization_id)
                               REFERENCES organizations (id)
                               ON DELETE RESTRICT,

                       CONSTRAINT chk_users_username
                           CHECK (LENGTH(TRIM(username)) > 0),

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('ADMIN', 'USER')),

                       CONSTRAINT chk_users_status
                           CHECK (status IN ('ACTIVE', 'BLOCKED')),

                       CONSTRAINT chk_users_organization
                           CHECK (role = 'ADMIN' OR organization_id IS NOT NULL)
);

CREATE UNIQUE INDEX uq_users_username
    ON users (LOWER(username));

CREATE INDEX idx_users_organization_id
    ON users (organization_id);
