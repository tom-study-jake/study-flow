# StudyFlow 自习室预约系统

校园自习室在线预约系统，采用 Spring Cloud Alibaba 微服务架构，支持座位预约、签到、信用分管理。

## 技术栈

- **框架**: Spring Boot 3.4.7 + Spring Cloud Alibaba 2023.0.3.2
- **注册中心**: Nacos
- **数据库**: MySQL + MyBatis-Plus
- **缓存**: Redis
- **消息队列**: RabbitMQ
- **分布式锁**: Redisson
- **实时通信**: WebSocket
- **鉴权**: JWT

## 项目结构

```
study-flow-parent/
├── study-flow-common        # 公共模块（实体类、DTO、工具类）
├── study-flow-user          # 用户服务（注册、登录、信用分）
├── study-flow-reservation   # 预约服务（座位、预约、签到）
└── study-flow-gateway       # 网关服务（路由、鉴权）
```

## 核心功能

### 座位预约
- 基于 Redisson 分布式锁防止座位超卖
- Redis 缓存座位状态，SETNX 原子操作保证并发安全

### 消息队列异步处理
- RabbitMQ 延迟队列：预约超时自动取消（TTL + 死信队列）
- 签到成功异步更新信用分

### 实时座位推送
- WebSocket 向同自习室用户广播座位状态变更
- ConcurrentHashMap 管理在线会话

### 信用分系统
- 签到/爽约动态调整信用分
- 低信用用户限制预约权限
- Feign 服务间调用实现信用分管理

### JWT 鉴权
- 拦截器统一登录校验
- ThreadLocal 存储用户上下文

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis
- RabbitMQ
- Nacos

### 启动步骤

1. 启动 Nacos、Redis、RabbitMQ、MySQL
2. 导入数据库脚本
3. 复制各服务的 `application.yml.example` 为 `application.yml`，修改配置
4. 依次启动：
   - study-flow-user
   - study-flow-reservation
   - study-flow-gateway
5. 访问 `http://localhost:88`

### 配置说明

各服务的 `application.yml` 需要配置：
- MySQL 连接信息（用户名、密码）
- Redis 连接信息（地址、密码）
- RabbitMQ 连接信息（地址、用户名、密码）
- Nacos 地址
- JWT 密钥（通过 `JWT_SECRET` 环境变量配置）

## API 接口

### 用户服务 (8081)
- `POST /api/auth/register` - 注册
- `POST /api/auth/login` - 登录
- `GET /api/users/credit` - 查询信用分

### 预约服务 (8082)
- `POST /api/reservations` - 预约座位
- `DELETE /api/reservations/{id}` - 取消预约
- `GET /api/reservations/my` - 我的预约
- `GET /api/reservations/{id}` - 预约详情
- `POST /api/check-In` - 签到
- `POST /api/check-Out` - 签退
- `GET /api/rooms` - 自习室列表
- `GET /api/rooms/{id}/periods` - 可预约时段
- `GET /api/rooms/{id}/seats` - 座位列表
- `WS /api/ws/seats/{roomId}` - 座位状态实时推送

### 网关 (88)
- 所有请求通过网关路由到对应服务
