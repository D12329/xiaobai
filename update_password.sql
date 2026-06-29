USE recruitment_system;
-- 请使用以下命令设置密码（将 YOUR_ENCRYPTED_PASSWORD 替换为实际加密后的密码）
-- UPDATE sys_user SET password = 'YOUR_ENCRYPTED_PASSWORD' WHERE username = 'your_username';
-- 
-- 注意：密码应使用 BCrypt 加密，可通过 AuthController 中的 register 接口生成
-- 或者使用 Spring Security 的 BCryptPasswordEncoder.encode() 方法生成
