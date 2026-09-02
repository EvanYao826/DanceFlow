# DanceFlow

基于 Spring Boot + Vue 的高校街舞社团综合服务平台（毕业设计项目）。

提供社员管理、活动组织、舞蹈课程、作品展示、社区交流和数据统计等能力，前后端分离，分为用户端和管理端。

## 技术栈

| 端 | 技术 |
| --- | --- |
| 后端 | Java 17 · Spring Boot 3 · Spring Security + JWT · MyBatis-Plus · MySQL 8 · Redis · SpringDoc(Swagger) |
| 前端 | Vue 3 · Vite · TypeScript · Pinia · Vue Router · Element Plus · Axios · ECharts |

## 目录结构

```text
backend/          后端服务（Spring Boot）
  src/main/java/com/danceflow/
    common/       统一响应、分页等通用对象
    config/       配置类（安全、MyBatis-Plus、Jackson、OpenAPI）
    controller/   接口层
    service/      业务层（impl 为实现）
    mapper/       数据访问层
    entity/ dto/ vo/
    exception/    业务异常与全局异常处理
  src/main/resources/
    application*.yml
    mapper/       MyBatis XML
frontend/         前端应用（Vue 3 + Vite）
  src/
    api/          请求封装（request.ts 含统一拦截器）
    components/   通用组件
    layouts/      布局
    router/       路由
    stores/       Pinia 状态
    types/        全局类型声明
    views/        页面
PlanDocs/         阶段开发计划（按 01~07 顺序推进）
database/         数据库脚本（阶段 1 起添加 schema.sql / data.sql）
```

## 快速开始

环境要求：JDK 17、Maven 3.8+、Node.js 20+、MySQL 8、Redis。

### 后端

```bash
cd backend
# 1. 修改 application-dev.yml 中的数据库/Redis 连接（或设置对应环境变量）
# 2. 启动
mvn spring-boot:run
```

启动后：

- 健康检查：<http://localhost:8080/api/health>
- 接口文档：<http://localhost:8080/swagger-ui.html>

### 前端

```bash
cd frontend
npm install
npm run dev
```

启动后访问 <http://localhost:5173>，`/api` 请求由 Vite 代理转发到本地 8080 后端。

## 开发规范

- 统一响应结构：`{ code, message, data, timestamp }`，分页参数 `page` / `pageSize`。
- 所有写操作使用 DTO，查询返回 VO，禁止直接暴露 Entity。
- 每个阶段以 `PlanDocs/` 对应文档为任务边界，验收条件全部满足后才进入下一阶段。
- 提交信息使用 `feat` / `fix` / `refactor` / `test` / `docs` 前缀。

详细约定见 [PlanDocs/00-开发总览与执行规范.md](PlanDocs/00-开发总览与执行规范.md)，整体设计见 [DanceFlow项目开发文档.md](DanceFlow项目开发文档.md)。
