-- Dev-only seed: one ADMIN user so there's something to log in with locally.
-- Password below is the plaintext for the bcrypt hash that follows - change
-- it (or delete this user) before this ever points at anything but a local
-- Postgres instance. See docs/security.md #34-36.
--
-- email:    admin@example.com
-- password: ChangeMe123!

INSERT INTO users (id, email, full_name, password_hash, role, status, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'admin@example.com',
    'Admin User',
    '$2a$10$CtwzeNyiFr5BZd8ihekWZ.xIVqOvpCN9AvuOG55glO0CfV4LFrwJu',
    'ADMIN',
    'ACTIVE',
    now(),
    now()
)
ON CONFLICT (email) DO NOTHING;
