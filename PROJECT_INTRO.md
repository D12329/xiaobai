# 企业招聘后台管理系统

## 项目简介

本项目是一个基于 Spring Boot + Vue 3 的企业招聘后台管理系统，提供完整的招聘流程管理功能，包括职位发布、简历管理、候选人追踪、AI智能匹配等核心功能。

## 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 后端框架 |
| Java | 17 | 编程语言 |
| MyBatis-Plus | 3.5.6 | ORM框架 |
| Spring Security | 6.x | 安全框架 |
| JWT | 0.12.5 | 身份认证 |
| MySQL | 8.x | 数据库 |
| SpringDoc OpenAPI | 2.3.0 | API文档 |
| Apache POI | 5.2.5 | Excel处理 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| Element Plus | 2.6.3 | UI组件库 |
| Vite | 5.4.0 | 构建工具 |
| Vue Router | 4.3.2 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Axios | 1.6.7 | HTTP客户端 |
| ECharts | 5.5.0 | 图表库 |
| Sass | 1.71.1 | CSS预处理 |

## 项目结构

### 后端结构

```
src/main/java/org/example/demo01/
├── common/           # 通用组件
│   ├── Result.java           # 统一响应封装
│   ├── PageResult.java       # 分页响应封装
│   ├── PageRequest.java      # 分页请求封装
│   ├── BusinessException.java # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理
├── config/           # 配置类
│   ├── CorsConfig.java       # 跨域配置
│   ├── SecurityConfig.java   # 安全配置
│   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   └── OpenApiConfig.java    # Swagger配置
├── controller/       # 控制层
│   ├── AuthController.java       # 认证接口
│   ├── CandidateController.java  # 候选人管理
│   ├── PositionController.java   # 职位管理
│   ├── ResumeController.java     # 简历管理
│   ├── InterviewController.java  # 面试管理
│   ├── OfferController.java      # Offer管理
│   ├── DashboardController.java  # 数据看板
│   ├── ResumeMatchController.java # AI匹配
│   ├── DictController.java       # 数据字典
│   ├── DepartmentController.java # 部门管理
│   ├── RoleController.java       # 角色管理
│   └── UserController.java       # 用户管理
├── service/          # 服务层
│   ├── impl/                    # 服务实现
│   ├── CandidateService.java
│   ├── PositionService.java
│   ├── ResumeService.java
│   ├── InterviewService.java
│   ├── OfferService.java
│   ├── DashboardService.java
│   ├── ResumeMatchService.java
│   └── UserService.java
├── mapper/           # 数据访问层
├── entity/           # 实体类
│   ├── Candidate.java    # 候选人
│   ├── Position.java     # 职位
│   ├── Resume.java       # 简历
│   ├── Interview.java    # 面试
│   ├── Offer.java        # Offer
│   ├── Dict.java         # 数据字典
│   ├── Department.java   # 部门
│   ├── Role.java         # 角色
│   └── User.java         # 用户
├── security/         # 安全模块
│   ├── JwtService.java             # JWT服务
│   └── JwtAuthenticationFilter.java # JWT过滤器
└── RecruitmentApplication.java  # 启动类
```

### 前端结构

```
front/src/
├── views/            # 页面组件
│   ├── Login.vue           # 登录页
│   ├── Dashboard.vue       # 数据看板
│   ├── Match.vue           # AI智能匹配
│   ├── Tracking.vue        # 流程追踪
│   ├── Candidate.vue       # 候选人管理
│   ├── Position.vue        # 职位管理
│   ├── Resume.vue          # 简历管理
│   ├── Interview.vue       # 面试管理
│   ├── Offer.vue           # Offer管理
│   └── settings/           # 设置页面
│       ├── DictManagement.vue   # 数据字典
│       ├── Department.vue       # 部门管理
│       └── RoleManagement.vue   # 角色管理
├── api/              # API接口封装
│   └── index.js
├── router/           # 路由配置
├── stores/           # Pinia状态管理
├── components/       # 公共组件
└── style/            # 全局样式
```

## 核心功能模块

### 1. 认证模块
- 用户登录/登出
- JWT令牌认证
- 权限控制

### 2. 职位管理
- 职位列表展示
- 职位发布/编辑/删除
- 职位状态管理（上架/下架）
- 职位搜索筛选

### 3. 简历管理
- 简历上传与解析
- 简历列表展示
- 简历筛选与搜索

### 4. 候选人管理
- 候选人信息管理
- 候选人状态流转
- 候选人搜索与筛选

### 5. AI智能匹配
- 简历与职位自动匹配
- 匹配度评分算法
- 匹配结果分析

### 6. 面试管理
- 面试安排
- 面试记录
- 面试评价

### 7. Offer管理
- Offer发送
- Offer接受/拒绝
- 入职流程管理

### 8. 数据看板
- 招聘统计数据
- 招聘流程追踪
- 实时数据刷新

### 9. 数据字典
- 字典类型管理
- 字典项维护
- 数据分类管理

### 10. 系统设置
- 部门管理
- 角色管理
- 用户管理

## 数据库表结构

| 表名 | 说明 |
|------|------|
| `rec_user` | 用户表 |
| `rec_role` | 角色表 |
| `rec_department` | 部门表 |
| `rec_position` | 职位表 |
| `rec_resume` | 简历表 |
| `rec_candidate` | 候选人表 |
| `rec_interview` | 面试表 |
| `rec_offer` | Offer表 |
| `rec_employee` | 员工表 |
| `sys_dict` | 数据字典表 |
| `sys_menu` | 菜单表 |
| `sys_operation_log` | 操作日志表 |

## 部署说明

### 环境要求

- JDK 17+
- MySQL 8.0+
- Node.js 18+

### 后端启动

```bash
# 进入项目目录
cd demo01

# 使用Maven Wrapper启动
./mvnw.cmd spring-boot:run

# 或打包后运行
./mvnw.cmd clean package
java -jar target/demo01-0.0.1-SNAPSHOT.jar
```

### 前端启动

```bash
# 进入前端目录
cd front

# 安装依赖
npm install

# 开发模式启动
npm run dev

# 构建生产版本
npm run build
```

### 配置说明

后端配置文件位于 `src/main/resources/application.yml`，主要配置项：

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/recruitment_system}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
```

## API文档

启动后端服务后，访问以下地址查看API文档：

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## 前端预览

前端服务启动后，访问：http://localhost:5173

## 许可证

MIT License

---

**项目版本**: 0.0.1-SNAPSHOT  
**开发环境**: Windows 10/11  
**最后更新**: 2026-06-29