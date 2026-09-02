USE danceflow;

INSERT INTO sys_role (role_code, role_name)
VALUES ('USER', '普通用户'), ('ADMIN', '管理员'), ('SUPER_ADMIN', '超级管理员')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), updated_at = CURRENT_TIMESTAMP;

INSERT INTO sys_permission (permission_code, permission_name, type, path)
VALUES
    ('home:view', '首页', 'MENU', '/'),
    ('admin:view', '管理端', 'MENU', '/admin'),
    ('admin:role:view', '角色管理', 'BUTTON', '/api/admin/roles'),
    ('admin:permission:view', '权限管理', 'BUTTON', '/api/admin/permissions')
ON DUPLICATE KEY UPDATE permission_name = VALUES(permission_name), updated_at = CURRENT_TIMESTAMP;

INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM sys_role r CROSS JOIN sys_permission p WHERE r.role_code = 'USER' AND p.permission_code = 'home:view';

INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM sys_role r CROSS JOIN sys_permission p WHERE r.role_code IN ('ADMIN', 'SUPER_ADMIN') AND p.permission_code IN ('home:view', 'admin:view', 'admin:role:view', 'admin:permission:view');
