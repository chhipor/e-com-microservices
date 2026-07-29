INSERT INTO permissions (name, description)
VALUES
    ('USER_READ', 'Read user records'),
    ('USER_WRITE', 'Create user records'),
    ('USER_UPDATE', 'Update user records'),
    ('USER_DELETE', 'Delete user records')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description)
VALUES
    ('ADMIN', 'Administrator role'),
    ('USER', 'Standard user role')
ON CONFLICT (name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('USER_READ', 'USER_WRITE', 'USER_UPDATE', 'USER_DELETE')
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'USER_READ'
WHERE r.name = 'USER'
ON CONFLICT DO NOTHING;
