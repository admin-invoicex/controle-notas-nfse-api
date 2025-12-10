-- Flyway V1: Initial IAM schema (users, roles, users_roles)

CREATE TABLE IF NOT EXISTS roles (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(150)      NOT NULL,
    email          VARCHAR(180)      NOT NULL UNIQUE,
    password_hash  VARCHAR(255)      NOT NULL,
    document       VARCHAR(20)       NOT NULL UNIQUE,
    document_type  VARCHAR(10)       NOT NULL, -- CPF or CNPJ
    active         BOOLEAN           NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP         NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP         NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
    PRIMARY KEY (user_id, role_id)
);

-- Seed base roles
INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('COMPANY') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ACCOUNTANT') ON CONFLICT DO NOTHING;

-- Trigger function to keep updated_at current
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_proc p
        JOIN pg_namespace n ON n.oid = p.pronamespace
        WHERE p.proname = 'trg_set_updated_at' AND n.nspname = 'public'
    ) THEN
        CREATE OR REPLACE FUNCTION trg_set_updated_at()
        RETURNS TRIGGER AS $func$
        BEGIN
            NEW.updated_at = NOW();
            RETURN NEW;
        END;
        $func$ LANGUAGE plpgsql;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'set_updated_at'
    ) THEN
        CREATE TRIGGER set_updated_at
        BEFORE UPDATE ON users
        FOR EACH ROW EXECUTE FUNCTION trg_set_updated_at();
    END IF;
END $$;