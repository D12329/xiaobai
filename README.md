<p align="center">
  <h1 align="center">🏢 Enterprise Recruitment Backend Management System</h1>
  <p align="center">
    <strong>一站式企业招聘全流程管理平台</strong>
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk" alt="Java 17" />
    <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=springboot" alt="Spring Boot 3.2.5" />
    <img src="https://img.shields.io/badge/Vue-3.4.21-42b883?style=flat-square&logo=vuedotjs" alt="Vue 3" />
    <img src="https://img.shields.io/badge/MySQL-8.0-4479a1?style=flat-square&logo=mysql" alt="MySQL 8.0" />
    <img src="https://img.shields.io/badge/Element%20Plus-2.6.3-409eff?style=flat-square" alt="Element Plus" />
    <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="MIT License" />
  </p>
</p>

---

## 📋 项目简介

Enterprise Recruitment Backend Management System 是一套面向企业人力资源部门设计的 **招聘全流程后台管理系统**，涵盖从职位发布、简历收集、候选人管理、面试安排到 Offer 管理的完整业务闭环。系统采用前后端分离架构，后端基于 Spring Boot 3 + MyBatis-Plus 构建，前端使用 Vue 3 + Element Plus 打造现代化管理界面，并集成智能简历-岗位匹配算法，助力 HR 高效筛选人才。

## ✨ 核心功能

### 🔐 用户与权限管理
- JWT Token 认证机制，支持登录 / 注册 / 密码修改 / 管理员重置
- 基于角色的访问控制（超级管理员、HR、面试官）
- 动态菜单与权限标识体系
- 部门层级树结构管理
<img width="1092" height="349" alt="image" src="https://github.com/user-attachments/assets/a4797319-cbf4-4469-82e5-bbfdc942f5dc" />

### 💼 职位管理
- 职位信息的增删改查与分页检索
- 支持按关键词、状态、部门多维度筛选
- 职位状态一键切换（启用 / 停用）
<img width="1083" height="413" alt="image" src="https://github.com/user-attachments/assets/51b40b45-b0bb-41e6-89a7-d8a6e2110de1" />

### 📄 简历管理
- 简历信息录入与文件上传（支持最大 50MB）
- UUID 文件名防冲突，删除时自动清理关联文件
- 未转化简历智能筛选（未关联候选人的简历）
<img width="1085" height="416" alt="image" src="https://github.com/user-attachments/assets/590ab341-0a42-4afb-a669-576062e489b1" />

### 👤 候选人管理
- 候选人全生命周期管理
- **六阶段状态流转**：待处理 → 筛选中 → 面试中 → 已发 Offer → 已入职 / 已淘汰
- 候选人跟进记录追加
<img width="1087" height="376" alt="image" src="https://github.com/user-attachments/assets/23887a7a-8f37-40e2-9911-8494b9a642a3" />


### 🎯 面试管理
- 面试日程安排与多轮面试支持
- 面试类型：电话面试、视频面试、现场面试、群面
- 面试评估与评分录入
- 面试状态流转：已安排 → 已完成 / 已取消
<img width="1100" height="420" alt="image" src="https://github.com/user-attachments/assets/27b1e0c9-9459-4430-9ea7-31812a5a99d7" />

### 📝 Offer 管理
- Offer 创建、编辑与发送
- Offer 状态全流程追踪：草稿 → 待发送 → 已发送 → 已接受 / 已拒绝 / 已取消
- 薪资、福利、试用期等信息管理
<img width="1105" height="515" alt="image" src="https://github.com/user-attachments/assets/37dc648d-43b4-40cc-a361-69cfa306d307" />

### 📊 数据看板（Dashboard）
- 全局统计概览：候选人、职位、简历、面试等核心指标
- 岗位需求热度分析
- 简历来源渠道分布
- 近 6 个月招聘趋势分析
- 候选人 Pipeline 各阶段统计
- 招聘周期各阶段耗时分析
- 超期预警（候选人滞留 > 7 天、面试超时提醒）
<img width="1272" height="688" alt="image" src="https://github.com/user-attachments/assets/9204b48e-3042-4e98-b585-e492f31eabaa" />

### 🤖 智能简历匹配
- 基于加权评分算法的简历-岗位匹配引擎
- 四维度评估体系：
  - 学历匹配（20 分）
  - 经验匹配（30 分）
  - 技能匹配（30 分）
  - 项目匹配（20 分）
- 匹配结果分级：优秀（80+）/ 良好（60+）/ 一般（40+）/ 较差（< 40）

### 📚 系统基础
- 数据字典管理（学历、面试类型、各类状态等可配置项）
- 部门层级管理
- 操作日志审计
- Swagger / OpenAPI 在线文档

## 🛠 技术架构

### 后端

| 技术 | 版本 | 说明 |
|:---:|:---:|:---|
| Java | 17 | 编程语言 |
| Spring Boot | 3.2.5 | 应用框架 |
| Spring Security | 6.x | 安全认证框架 |
| MyBatis-Plus | 3.5.6 | ORM 框架（含分页、自动填充） |
| MySQL | 8.x | 关系型数据库 |
| HikariCP | 内置 | 高性能数据库连接池 |
| JJWT | 0.12.5 | JWT Token 生成与校验 |
| SpringDoc OpenAPI | 2.3.0 | Swagger API 文档 |
| Apache POI | 5.2.5 | Excel 文件处理 |
| Lombok | 1.18.32 | 代码简化工具 |

### 前端

| 技术 | 版本 | 说明 |
|:---:|:---:|:---|
| Vue | 3.4.21 | 渐进式 JS 框架 |
| Element Plus | 2.6.3 | UI 组件库 |
| Vite | 5.4.0 | 构建工具 |
| Vue Router | 4.3.2 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Axios | 1.6.7 | HTTP 客户端 |
| ECharts | 5.5.0 | 数据可视化图表 |
| Sass | 1.71.1 | CSS 预处理器 |

## 📁 项目结构

```
demo01/
├── pom.xml                              # Maven 构建配置
├── mvnw / mvnw.cmd                      # Maven Wrapper
├── start_servers.bat                    # 一键启动脚本（Windows）
├── stop_servers.bat                     # 一键停止脚本（Windows）
├── update_password.sql                  # 密码重置 SQL 脚本
│
├── front/                               # 🖥 前端工程
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── api/                         # Axios 接口封装
│       ├── views/                       # 页面组件
│       ├── layout/                      # 布局组件
│       ├── router/                      # 路由配置
│       ├── stores/                      # Pinia 状态管理
│       ├── styles/                      # 全局样式
│       └── utils/                       # 工具函数
│
└── src/main/java/org/example/demo01/    # ⚙️ 后端工程
    ├── RecruitmentApplication.java      # 启动入口
    ├── common/                          # 通用模块（统一响应、分页、异常处理）
    ├── config/                          # 配置类（Security、CORS、MyBatis、Swagger）
    ├── controller/                      # REST 控制器（12 个）
    ├── entity/                          # 数据库实体（12 个）
    ├── enums/                           # 状态枚举
    ├── mapper/                          # MyBatis-Plus Mapper 接口
    ├── security/                        # JWT 认证模块
    └── service/                         # 业务逻辑层
```

## 🚀 快速开始

### 环境要求

- **JDK** 17+
- **MySQL** 8.0+
- **Maven** 3.8+（或使用项目内置的 Maven Wrapper）
- **Node.js** 16+ & **npm** / **pnpm**

### 数据库准备

创建数据库 `recruitment_system`，系统启动时会自动执行 `schema.sql` 和 `data.sql` 初始化表结构与示例数据：

```sql
CREATE DATABASE IF NOT EXISTS recruitment_system
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;
```

### 后端启动

```bash
# 进入项目根目录
cd demo01

# 安装依赖并启动（使用 Maven Wrapper）
mvnw.cmd spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`。

### 前端启动

```bash
# 进入前端目录
cd front

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端开发服务器默认运行在 `http://localhost:5173`。

### 一键启动（Windows）

项目根目录提供了便捷脚本：

```cmd
start_servers.bat    # 同时启动前后端
stop_servers.bat     # 停止所有服务
```

## 🔑 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|:---:|:---:|:---:|:---|
| 超级管理员 | `admin` | `请查看 data.sql` | 全部权限 |
| HR | `hr001` | `请查看 data.sql` | 招聘管理 |
| 面试官 | `yonghu` | `请查看 data.sql` | 面试评估 |

## 📡 API 概览

系统共提供 **12 组 RESTful API**，完整文档可通过启动后端后访问 Swagger UI 查看：

```
http://localhost:8080/swagger-ui.html
```

| 模块 | 路径前缀 | 说明 |
|:---|:---|:---|
| 认证模块 | `/api/auth` | 登录、注册、用户信息、密码管理 |
| 职位管理 | `/api/positions` | 职位 CRUD、状态切换 |
| 简历管理 | `/api/resumes` | 简历 CRUD、文件上传、未转化简历查询 |
| 候选人管理 | `/api/candidates` | 候选人 CRUD、状态流转、跟进记录 |
| 面试管理 | `/api/interviews` | 面试安排、状态更新、评估打分 |
| Offer 管理 | `/api/offers` | Offer CRUD、状态管理 |
| 数据看板 | `/api/dashboard` | 统计分析、趋势图表、预警、Pipeline |
| 智能匹配 | `/api/match` | 简历解析、岗位匹配、匹配结果查询 |
| 数据字典 | `/api/dict` | 字典 CRUD（学历、状态等下拉数据） |
| 部门管理 | `/api/departments` | 部门 CRUD |
| 角色管理 | `/api/roles` | 角色查询 |
| 用户管理 | `/api/users` | 用户 CRUD、启用/禁用 |

## 🗄 数据库设计

系统基于 MySQL 8.0，使用 InnoDB 引擎、UTF-8mb4 字符集，共 **12 张数据表**：

| 表名 | 说明 |
|:---|:---|
| `sys_user` | 系统用户 |
| `sys_role` | 角色信息 |
| `sys_department` | 部门信息 |
| `sys_dict` | 数据字典 |
| `sys_menu` | 菜单权限 |
| `sys_log` | 操作日志 |
| `rec_position` | 招聘职位 |
| `rec_resume` | 简历信息 |
| `rec_candidate` | 候选人 |
| `rec_interview` | 面试记录 |
| `rec_offer` | Offer 信息 |
| `rec_employee` | 入职员工 |

> 📌 候选人状态流转：`待处理(1)` → `筛选中(2)` → `面试中(3)` → `已发Offer(4)` → `已入职(5)`，任意阶段可 `淘汰(6)`

## ⚙️ 配置说明

核心配置位于 `src/main/resources/application.yml`：

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/recruitment_system}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}

jwt:
  secret: ${JWT_SECRET:}
  expiration: 86400000
```

**生产环境注意事项：**
- 修改数据库密码为高强度密码
- 替换 JWT Secret 为安全的随机密钥
- 收紧 CORS 配置，限制允许的 Origin
- 启用 Spring Security 路由鉴权（当前开发模式为 `permitAll`）

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来改进本项目。在提交 PR 之前，请确保：

1. 代码遵循项目现有的编码风格
2. 新功能包含相应的测试用例
3. 提交信息清晰描述变更内容

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源。

---

<p align="center">
  Made with ❤️ for HR Teams
</p>
