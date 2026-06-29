USE recruitment_system;

INSERT INTO `sys_role` (`id`, `name`, `code`, `description`) VALUES
(1, '超级管理员', 'admin', '系统超级管理员，拥有所有权限'),
(2, 'HR', 'hr', '人力资源专员，负责招聘流程管理'),
(3, '面试官', 'yonghu', '面试官，负责面试安排和评价');

INSERT INTO `sys_department` (`id`, `name`, `parent_id`, `description`) VALUES
(1, '技术部', 0, '负责技术研发工作'),
(2, '产品部', 0, '负责产品设计和规划'),
(3, '人力资源部', 0, '负责招聘和人事管理'),
(4, '前端开发组', 1, '前端技术开发'),
(5, '后端开发组', 1, '后端技术开发');

INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `department_id`, `role_id`, `status`) VALUES
(1, 'admin', '$2a$10$K2mmHg8IEGLTUD70Sl/Diesr54HkY9eaShTCTUyXscrbyIh8XdxqK', '系统管理员', 'admin@example.com', '13800138001', 3, 1, 1),
(2, 'hr001', '$2a$10$K2mmHg8IEGLTUD70Sl/Diesr54HkY9eaShTCTUyXscrbyIh8XdxqK', '张HR', 'hr@example.com', '13800138002', 3, 2, 1),
(3, 'yonghu', '$2a$10$K2mmHg8IEGLTUD70Sl/Diesr54HkY9eaShTCTUyXscrbyIh8XdxqK', '李面试官', 'interviewer@example.com', '13800138003', 1, 3, 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `icon`, `sort`, `type`, `permission`) VALUES
(1, 0, '仪表盘', '/dashboard', 'Dashboard', 'icon-home', 1, 0, 'dashboard:view'),
(2, 0, '职位管理', '/positions', 'Position', 'icon-briefcase', 2, 0, 'position:view'),
(3, 0, '简历管理', '/resumes', 'Resume', 'icon-file-text', 3, 0, 'resume:view'),
(4, 0, '候选人管理', '/candidates', 'Candidate', 'icon-users', 4, 0, 'candidate:view'),
(5, 0, '面试管理', '/interviews', 'Interview', 'icon-calendar', 5, 0, 'interview:view'),
(6, 0, 'Offer管理', '/offers', 'Offer', 'icon-gift', 6, 0, 'offer:view'),
(7, 0, '系统设置', '/settings', 'Settings', 'icon-settings', 7, 0, 'setting:view'),
(8, 7, '用户管理', '/settings/users', 'User', 'icon-user', 1, 0, 'user:view'),
(9, 7, '部门管理', '/settings/departments', 'Department', 'icon-building', 2, 0, 'department:view'),
(10, 7, '数据字典', '/settings/dict', 'Dict', 'icon-book-open', 3, 0, 'dict:view'),
(11, 7, '操作日志', '/settings/logs', 'Log', 'icon-file-log', 4, 0, 'log:view'),
(12, 0, 'AI匹配', '/match', 'Match', 'icon-brain', 8, 0, 'match:view'),
(13, 0, '流程看板', '/tracking', 'Tracking', 'icon-timeline', 9, 0, 'tracking:view');

INSERT INTO `sys_dict` (`id`, `type`, `code`, `label`, `value`, `sort`) VALUES
(1, 'education', 'high_school', '高中', '高中', 1),
(2, 'education', 'junior_college', '大专', '大专', 2),
(3, 'education', 'bachelor', '本科', '本科', 3),
(4, 'education', 'master', '硕士', '硕士', 4),
(5, 'education', 'doctor', '博士', '博士', 5),
(6, 'position_status', 'active', '上架', '1', 1),
(7, 'position_status', 'inactive', '下架', '0', 2),
(8, 'candidate_status', 'pending', '待筛选', '1', 1),
(9, 'candidate_status', 'interviewing', '面试中', '2', 2),
(10, 'candidate_status', 'hired', '录用', '3', 3),
(11, 'candidate_status', 'rejected', '拒绝', '4', 4),
(12, 'interview_type', 'on_site', '现场面试', '现场', 1),
(13, 'interview_type', 'video', '视频面试', '视频', 2),
(14, 'interview_type', 'phone', '电话面试', '电话', 3),
(15, 'interview_status', 'scheduled', '已安排', 'scheduled', 1),
(16, 'interview_status', 'completed', '已完成', 'completed', 2),
(17, 'interview_status', 'canceled', '已取消', 'canceled', 3),
(18, 'offer_status', 'pending', '待发送', 'pending', 1),
(19, 'offer_status', 'sent', '已发送', 'sent', 2),
(20, 'offer_status', 'accepted', '已接受', 'accepted', 3),
(21, 'offer_status', 'rejected', '已拒绝', 'rejected', 4);

INSERT INTO `rec_position` (`id`, `name`, `department_id`, `department_name`, `location`, `education`, `experience`, `recruitment_count`, `job_description`, `requirements`, `tags`, `status`) VALUES
(1, 'Java高级开发工程师', 5, '后端开发组', '北京', '本科', 5, 3, '负责后端系统架构设计和开发', 'Java基础扎实，熟悉SpringBoot框架，有分布式系统开发经验', 'Java,SpringBoot,分布式,微服务', 1),
(2, '前端开发工程师', 4, '前端开发组', '上海', '本科', 3, 2, '负责前端页面开发和优化', '熟悉Vue/React框架，有良好的代码规范意识', 'Vue,React,TypeScript,Webpack', 1),
(3, '产品经理', 2, '产品部', '深圳', '本科', 3, 1, '负责产品规划和需求分析', '有互联网产品经验，善于沟通协调', '产品设计,需求分析,原型设计', 1),
(4, '测试工程师', 1, '技术部', '广州', '大专', 2, 2, '负责软件测试和质量保障', '熟悉测试流程，有自动化测试经验优先', '软件测试,自动化测试,质量保障', 1);

INSERT INTO `rec_resume` (`id`, `name`, `phone`, `email`, `education`, `experience`, `skills`, `source`, `status`) VALUES
(1, '张三', '13800138001', 'zhangsan@example.com', '本科', 3, 'Java,Spring,MySQL', '智联招聘', 1),
(2, '李四', '13800138002', 'lisi@example.com', '硕士', 5, 'Vue,React,TypeScript', 'BOSS直聘', 1),
(3, '王五', '13800138003', 'wangwu@example.com', '本科', 4, 'Python,Django,PostgreSQL', '拉勾网', 1),
(4, '赵六', '13800138004', 'zhaoliu@example.com', '大专', 2, '软件测试,自动化测试', '前程无忧', 1),
(5, '钱七', '13800138005', 'qianqi@example.com', '本科', 6, 'Java,Redis,Kafka', '内推', 1);

INSERT INTO `rec_candidate` (`id`, `resume_id`, `position_id`, `name`, `phone`, `email`, `education`, `experience`, `skills`, `status`) VALUES
(1, 1, 1, '张三', '13800138001', 'zhangsan@example.com', '本科', 3, 'Java,Spring,MySQL', 1),
(2, 2, 2, '李四', '13800138002', 'lisi@example.com', '硕士', 5, 'Vue,React,TypeScript', 2),
(3, 3, 3, '王五', '13800138003', 'wangwu@example.com', '本科', 4, 'Python,Django,PostgreSQL', 1),
(4, 4, 4, '赵六', '13800138004', 'zhaoliu@example.com', '大专', 2, '软件测试,自动化测试', 3),
(5, 5, 1, '钱七', '13800138005', 'qianqi@example.com', '本科', 6, 'Java,Redis,Kafka', 2);

INSERT INTO `rec_interview` (`id`, `candidate_id`, `position_id`, `interviewer_id`, `interviewer_name`, `round`, `type`, `start_time`, `location`, `status`, `result`) VALUES
(1, 1, 1, 3, '李面试官', 1, '视频', '2026-06-15 10:00:00', '腾讯会议', 'scheduled', NULL),
(2, 2, 2, 3, '李面试官', 2, '现场', '2026-06-14 14:00:00', '北京总部会议室A', 'completed', 'pass'),
(3, 4, 4, 3, '李面试官', 1, '电话', '2026-06-13 09:00:00', '电话面试', 'completed', 'pass'),
(4, 5, 1, 3, '李面试官', 1, '视频', '2026-06-16 15:00:00', '钉钉会议', 'scheduled', NULL);

INSERT INTO `rec_offer` (`id`, `candidate_id`, `position_id`, `salary`, `salary_type`, `probation_period`, `start_date`, `status`) VALUES
(1, 2, 2, 25000, '月薪', '3个月', '2026-07-01', 'sent'),
(2, 4, 4, 15000, '月薪', '3个月', '2026-06-20', 'accepted');
