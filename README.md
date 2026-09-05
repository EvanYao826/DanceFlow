<div align="center">

# DanceFlow

基于 Spring Boot + Vue 的高校街舞社团综合服务平台

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?style=flat-square&logo=springboot)
![Vue](https://img.shields.io/badge/Vue-3-4FC08D?style=flat-square&logo=vuedotjs)
![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?style=flat-square&logo=typescript)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=flat-square&logo=mysql)

</div>

---

## 项目简介

DanceFlow 面向高校街舞社团，提供社团信息、活动报名、课程学习、作品发布、社区交流和后台管理等功能。项目采用前后端分离架构，包含用户端和管理端两套使用场景。

## 功能模块

### 用户端

- 社团档案：查看社团介绍、成员信息和社团动态
- 活动中心：浏览活动详情、在线报名和管理报名记录
- 课程学习：浏览课程、查看课程章节和学习进度
- 作品社区：浏览公开作品、发布作品和互动评论
- 论坛公告：查看社区帖子与社团公告
- 个人中心：维护个人资料、查看我的作品、报名和学习记录

### 管理端

- 数据工作台：查看成员、活动、课程、作品和用户数据统计
- 成员审核：管理社团成员申请及成员信息
- 活动管理：维护活动信息、报名名单和活动状态
- 课程管理：维护课程、章节和学习内容
- 作品审核：审核用户发布的作品并管理公开状态
- 用户管理：管理系统用户、角色和账号状态
- 操作日志：查看后台关键操作记录

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 后端基础 | Java 17、Spring Boot 3.5.5、Maven |
| 安全认证 | Spring Security、JWT |
| 数据访问 | MyBatis-Plus、MySQL 8 |
| 缓存与文档 | Redis、SpringDoc OpenAPI |
| 前端基础 | Vue 3、Vite、TypeScript |
| 前端状态与路由 | Pinia、Vue Router |
| 前端组件与请求 | Element Plus、Axios |
| 数据可视化 | ECharts |

## 系统架构

```text
浏览器
  |
  |  Vue 3 + Vite + TypeScript
  |  Pinia + Vue Router + Element Plus
  v
前端应用  -- /api 代理 -->  Spring Boot 后端
                              |
                              |-- Spring Security + JWT
                              |-- Controller / Service / Mapper
                              |-- MyBatis-Plus
                              v
                         MySQL 8 + Redis
```

## 项目结构

```text
DanceFlow/
├─ backend/                         # Spring Boot 后端服务
│  ├─ src/main/java/com/danceflow/
│  │  ├─ controller/                # 接口层
│  │  ├─ service/                   # 业务层
│  │  ├─ mapper/                    # 数据访问层
│  │  ├─ entity/ dto/ vo/           # 数据模型
│  │  ├─ config/                    # 安全、数据库及 OpenAPI 配置
│  │  └─ exception/                 # 异常处理
│  └─ src/main/resources/            # 配置文件和 MyBatis XML
├─ frontend/                        # Vue 3 前端应用
│  └─ src/
│     ├─ api/                       # 接口请求封装
│     ├─ components/                # 通用组件
│     ├─ layouts/                   # 用户端和管理端布局
│     ├─ router/                    # 路由配置
│     ├─ stores/                    # Pinia 状态
│     └─ views/                     # 页面视图
├─ database/                        # 数据库初始化脚本
│  ├─ schema.sql
│  └─ data.sql
├─ PlanDocs/                        # 项目阶段计划
├─ LICENSE
└─ README.md
```

## 快速开始

### 环境要求

- JDK 17
- Maven 3.8+
- Node.js 20+
- MySQL 8
- Redis

### 数据库初始化

1. 创建 `danceflow` 数据库。
2. 执行 [`database/schema.sql`](database/schema.sql) 创建表结构。
3. 执行 [`database/data.sql`](database/data.sql) 导入测试数据。
4. 根据本地环境修改 `backend/src/main/resources/application-dev.yml` 中的数据库和 Redis 配置。

### 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端默认运行在 `http://localhost:8083`。

- 健康检查：`http://localhost:8083/api/health`
- 接口文档：`http://localhost:8083/swagger-ui.html`

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

Vite 配置的默认端口为 `5173`，如果该端口被占用会自动切换端口。当前常用访问地址为 `http://localhost:5174/`。前端 `/api` 请求会代理到 `http://localhost:8083`。

## 默认账号

| 类型 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin_demo` | `password` |
| 普通用户 | `dance_demo` | `password` |
| 普通用户 | `member_demo` | `password` |

登录后系统会根据账号角色进入对应端，管理端和用户端的页面及路由相互隔离。

## License

本项目基于 [MIT License](LICENSE) 开源。
