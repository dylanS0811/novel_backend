# NovelGrain DDD Backend (Spring Boot + MySQL)

**分层：**

- `domain/` 领域模型与仓库接口（端口）
- `application/` 用例服务（编排业务）
- `infrastructure/` JPA 实体、仓库适配器、SQL 排行榜、Flyway
- `interfaces/` REST 控制器（按你的接口契约）

**特性：**

- Flyway 建表与种子数据
- OpenAPI 契约：`openapi.yaml`
- 统一响应：`{ code, message, data }`
- 完整接口：鉴权/用户、书目、标签、评论、点赞收藏、排行榜、通知
- 支持 `PATCH /books/{id}`、`DELETE /books/{id}`

## 启动

1. MySQL：`CREATE DATABASE novelgrain DEFAULT CHARACTER SET utf8mb4;`
2. 配置 `src/main/resources/application.yml` 数据库账号密码
3. 运行：`mvn spring-boot:run`
4. Swagger：`http://localhost:8080/swagger-ui`

> 目前 Security 允许匿名访问方便联调；后续接入 JWT 后把 `userId` 从 Token 解析即可。
