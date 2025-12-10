CREATE TABLE IF NOT EXISTS certificate_storage (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL UNIQUE,
    cert_ciphertext BYTEA NOT NULL,
    password_ciphertext BYTEA NOT NULL,
    kms_key_id VARCHAR NOT NULL,
    subject_dn TEXT NULL,
    issuer_dn TEXT NULL,
    valid_from TIMESTAMPTZ NULL,
    valid_to TIMESTAMPTZ NULL,
    serial_number VARCHAR(64) NULL,
    fingerprint_sha1 VARCHAR(64) NULL,
    fingerprint_sha256 VARCHAR(128) NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_certificate_storage_company_id ON certificate_storage(company_id);