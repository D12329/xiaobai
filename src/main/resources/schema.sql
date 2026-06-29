CREATE DATABASE IF NOT EXISTS recruitment_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE recruitment_system;

DROP TABLE IF EXISTS `rec_employee`;
DROP TABLE IF EXISTS `rec_offer`;
DROP TABLE IF EXISTS `rec_interview`;
DROP TABLE IF EXISTS `rec_candidate`;
DROP TABLE IF EXISTS `rec_resume`;
DROP TABLE IF EXISTS `rec_position`;
DROP TABLE IF EXISTS `sys_log`;
DROP TABLE IF EXISTS `sys_dict`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_department`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-启用,0-禁用)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `department_id` BIGINT COMMENT '部门ID',
    `role_id` INT COMMENT '角色ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-启用,0-禁用)',
    `avatar` VARCHAR(255) COMMENT '头像',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_department_id` (`department_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `path` VARCHAR(200) COMMENT '路由路径',
    `component` VARCHAR(200) COMMENT '组件路径',
    `icon` VARCHAR(100) COMMENT '图标',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `type` TINYINT DEFAULT 0 COMMENT '类型(0-菜单,1-按钮)',
    `permission` VARCHAR(100) COMMENT '权限标识',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-启用,0-禁用)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `sys_department` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `description` VARCHAR(500) COMMENT '部门描述',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-启用,0-禁用)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE IF NOT EXISTS `sys_dict` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '字典ID',
    `type` VARCHAR(50) NOT NULL COMMENT '字典类型',
    `code` VARCHAR(50) NOT NULL COMMENT '字典编码',
    `label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `value` VARCHAR(255) NOT NULL COMMENT '字典值',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-启用,0-禁用)',
    `remark` VARCHAR(200) COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

CREATE TABLE IF NOT EXISTS `sys_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `user_id` BIGINT COMMENT '用户ID',
    `username` VARCHAR(50) COMMENT '用户名',
    `operation` VARCHAR(100) COMMENT '操作内容',
    `module` VARCHAR(50) COMMENT '操作模块',
    `method` VARCHAR(100) COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `result` TEXT COMMENT '返回结果',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-成功,0-失败)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

CREATE TABLE IF NOT EXISTS `rec_position` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '职位ID',
    `name` VARCHAR(100) NOT NULL COMMENT '职位名称',
    `department_id` BIGINT COMMENT '部门ID',
    `department_name` VARCHAR(100) COMMENT '部门名称',
    `location` VARCHAR(100) COMMENT '工作地点',
    `education` VARCHAR(50) COMMENT '学历要求',
    `experience` INT COMMENT '经验要求(年)',
    `recruitment_count` INT DEFAULT 1 COMMENT '招聘人数',
    `job_description` TEXT COMMENT '岗位职责',
    `requirements` TEXT COMMENT '任职要求',
    `tags` VARCHAR(500) COMMENT '标签',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-上架,0-下架)',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

CREATE TABLE IF NOT EXISTS `rec_resume` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '简历ID',
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `education` VARCHAR(50) COMMENT '学历',
    `experience` INT COMMENT '工作经验(年)',
    `skills` VARCHAR(500) COMMENT '技能',
    `tags` VARCHAR(500) COMMENT '标签',
    `file_path` VARCHAR(500) COMMENT '文件路径',
    `source` VARCHAR(50) COMMENT '来源渠道',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-有效,0-无效)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表';

CREATE TABLE IF NOT EXISTS `rec_candidate` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '候选人ID',
    `resume_id` BIGINT COMMENT '简历ID',
    `position_id` BIGINT COMMENT '职位ID',
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `education` VARCHAR(50) COMMENT '学历',
    `experience` INT COMMENT '工作经验(年)',
    `skills` VARCHAR(500) COMMENT '技能',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-待筛选,2-面试中,3-录用,4-拒绝)',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_resume_id` (`resume_id`),
    KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候选人表';

CREATE TABLE IF NOT EXISTS `rec_interview` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '面试ID',
    `candidate_id` BIGINT NOT NULL COMMENT '候选人ID',
    `position_id` BIGINT COMMENT '职位ID',
    `interviewer_id` BIGINT COMMENT '面试官ID',
    `interviewer_name` VARCHAR(50) COMMENT '面试官姓名',
    `round` INT DEFAULT 1 COMMENT '面试轮次',
    `type` VARCHAR(50) COMMENT '面试类型(现场/视频/电话)',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `location` VARCHAR(200) COMMENT '面试地点',
    `status` VARCHAR(20) DEFAULT 'scheduled' COMMENT '状态(scheduled-已安排,completed-已完成,canceled-已取消)',
    `result` VARCHAR(20) COMMENT '结果(pass-通过,fail-未通过,pending-待定)',
    `evaluation` TEXT COMMENT '评价',
    `score` INT COMMENT '评分',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_candidate_id` (`candidate_id`),
    KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试表';

CREATE TABLE IF NOT EXISTS `rec_offer` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'OfferID',
    `candidate_id` BIGINT NOT NULL COMMENT '候选人ID',
    `position_id` BIGINT COMMENT '职位ID',
    `salary` DECIMAL(10,2) COMMENT '薪资',
    `salary_type` VARCHAR(20) COMMENT '薪资类型(月薪/年薪)',
    `welfare` TEXT COMMENT '福利待遇',
    `probation_period` VARCHAR(50) COMMENT '试用期',
    `start_date` DATETIME COMMENT '入职日期',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态(pending-待发送,sent-已发送,accepted-已接受,rejected-已拒绝)',
    `send_time` DATETIME COMMENT '发送时间',
    `reply_time` DATETIME COMMENT '回复时间',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_candidate_id` (`candidate_id`),
    KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Offer表';

CREATE TABLE IF NOT EXISTS `rec_employee` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '员工ID',
    `candidate_id` BIGINT COMMENT '候选人ID',
    `offer_id` BIGINT COMMENT 'OfferID',
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `id_card` VARCHAR(20) COMMENT '身份证号',
    `address` VARCHAR(500) COMMENT '住址',
    `emergency_contact` VARCHAR(50) COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) COMMENT '紧急联系电话',
    `bank_account` VARCHAR(50) COMMENT '银行账号',
    `bank_name` VARCHAR(100) COMMENT '开户行',
    `education` VARCHAR(50) COMMENT '学历',
    `graduate_school` VARCHAR(200) COMMENT '毕业院校',
    `major` VARCHAR(100) COMMENT '专业',
    `entry_date` DATETIME COMMENT '入职日期',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1-在职,0-离职)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY `idx_candidate_id` (`candidate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工档案表';
