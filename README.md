# 校园流浪猫图鉴与救助打卡平台

基于 Spring Boot + uni-app 构建的校园流浪猫科学救助与投喂管理平台，支持猫咪档案管理、健康异常上报、投喂打卡、位置标记等功能。

---

## 项目结构

```
Cat/
├── backend/                          # 后端 Spring Boot 项目
│   ├── src/main/java/org/example/catcarebusiness/
│   │   ├── config/
│   │   │   └── SecurityConfig.java   # Spring Security 安全配置
│   │   ├── controller/
│   │   │   ├── CatController.java    # 猫咪档案接口
│   │   │   ├── FileController.java   # 图片上传接口（阿里云OSS）
│   │   │   └── HealthReportController.java  # 健康异常上报接口
│   │   ├── entity/
│   │   │   ├── Cat.java              # 猫咪档案实体
│   │   │   └── HealthReport.java     # 健康异常上报实体
│   │   ├── mapper/
│   │   │   ├── CatMapper.java        # 猫咪数据访问层
│   │   │   └── HealthReportMapper.java  # 健康上报数据访问层
│   │   ├── service/
│   │   │   ├── CatService.java       # 猫咪业务接口
│   │   │   ├── HealthReportService.java  # 健康上报业务接口
│   │   │   └── impl/
│   │   │       ├── CatServiceImpl.java
│   │   │       └── HealthReportServiceImpl.java
│   │   └── CatCareBusinessApplication.java  # 启动入口
│   └── pom.xml                       # Maven 依赖配置
│
└── frontend/                         # 前端 uni-app 项目
    └── src/
        ├── pages/
        │   ├── index/                # 首页（封面+导航）
        │   ├── catList/              # 猫咪图鉴列表
        │   ├── catDetail/            # 猫咪详情+投喂记录
        │   ├── feed/                 # 投喂打卡（地图选点）
        │   ├── mine/                 # 个人中心+投喂历史
        │   └── about/                # 关于我们+投喂须知
        ├── static/                   # 静态资源（图片、图标）
        ├── App.vue                   # 应用根组件
        ├── main.js                   # 应用入口
        ├── pages.json                # 页面路由与配置
        └── uni.scss                  # 全局样式变量
```

---

## 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Spring Boot 3.5 | 核心框架 |
| MyBatis-Plus 3.5 | ORM 框架，简化单表 CRUD |
| MySQL | 关系型数据库 |
| Redis | 缓存与 Session 管理 |
| Spring Security | 安全框架，接口鉴权 |
| JWT (jjwt 0.11) | 用户身份令牌 |
| Aliyun OSS | 图片云存储 |
| Knife4j 4.1 | API 文档生成与调试 |
| Lombok | 简化实体类代码 |
| Validation | 请求参数校验 |

### 前端

| 技术 | 说明 |
|------|------|
| uni-app 3.0 (Vue 3) | 跨端开发框架 |
| Vite | 构建工具 |
| 微信小程序原生 map | 地图定位与打点 |

---

## 功能模块

### 1. 猫咪档案管理
- 录入校园流浪猫基本信息（昵称、品种、性别、毛色、出没地点等）
- 更新猫咪绝育/TNR 状态（未绝育 → 已绝育 → 已剪耳标）
- 删除无效或错误档案数据
- API: `POST /api/cat/add` | `PUT /api/cat/tnr/{id}` | `DELETE /api/cat/delete/{id}`

### 2. 健康异常上报
- 提交猫咪健康异常信息（生病、受伤等）
- 支持上传异常照片至阿里云 OSS
- 标记异常发现位置（经纬度定位）
- 管理员接收通知并处理
- API: `POST /api/report/submit` | `POST /api/file/upload`

### 3. 猫咪图鉴浏览
- 以卡片列表形式展示所有猫咪
- 按绝育状态标记（绿色-已绝育 / 红色-未绝育）
- 点击卡片进入猫咪详情页查看完整信息

### 4. 猫咪详情
- 展示猫咪基本信息、绝育状态
- 展示历史投喂记录（投喂人、时间、地点）
- 底部固定"立即投喂"按钮直达打卡页

### 5. 投喂打卡
- 下拉选择目标猫咪
- 地图自由打点标记投喂位置
- 支持填写投喂备注（可选）
- 防过量投喂机制：单猫每日限制 3 次

### 6. 个人中心
- 显示用户基本信息与志愿者身份
- 展开查看个人历史投喂记录
- 关于小程序介绍入口

---

## 数据库表设计

### t_cat (猫咪档案表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK) | 猫咪 ID，自增 |
| name | VARCHAR | 昵称 |
| avatar_url | VARCHAR | 头像 URL |
| breed | VARCHAR | 品种 |
| gender | VARCHAR | 性别 (MALE/FEMALE/UNKNOWN) |
| age_estimate | VARCHAR | 预估年龄 |
| color | VARCHAR | 毛色 |
| appearance_features | VARCHAR | 外貌特征 |
| tnr_status | INT | TNR 状态 (0-未绝育/1-已绝育/2-已剪耳标) |
| frequent_locations | VARCHAR | 常出没地点 |
| default_latitude | DECIMAL | 默认纬度 |
| default_longitude | DECIMAL | 默认经度 |
| health_status | VARCHAR | 健康状态 |
| creator_id | BIGINT | 创建者用户 ID |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### t_health_report (健康异常上报表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK) | 上报 ID，自增 |
| cat_id | BIGINT | 关联猫咪 ID |
| reporter_id | BIGINT | 上报者用户 ID |
| report_type | VARCHAR | 异常类型 (SICK/INJURED 等) |
| description | VARCHAR | 异常详细描述 |
| image_urls | VARCHAR | 图片 URL 列表（逗号分隔） |
| latitude | DECIMAL | 发现地点纬度 |
| longitude | DECIMAL | 发现地点经度 |
| status | INT | 处理状态 (0-待处理/1-处理中等) |
| handler_id | BIGINT | 处理人 ID |
| handle_remark | VARCHAR | 处理备注 |
| report_time | DATETIME | 上报时间 |
| handle_time | DATETIME | 处理时间 |

---

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+
- 阿里云 OSS 账号（用于图片上传）

### 后端启动

1. 配置数据库

创建 MySQL 数据库，并执行建表语句（参考上方表结构）。

2. 配置 `application.properties`

在 `backend/src/main/resources/` 下创建 `application.properties`，填入以下配置：

```properties
# 数据库
spring.datasource.url=jdbc:mysql://localhost:3306/cat_care?useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的密码

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# 阿里云 OSS
aliyun.oss.endpoint=oss-cn-beijing.aliyuncs.com
aliyun.oss.accessKeyId=你的AccessKey
aliyun.oss.accessKeySecret=你的AccessKeySecret
aliyun.oss.bucketName=你的Bucket名称
```

3. 启动后端

```bash
cd backend
./mvnw spring-boot:run        # macOS / Linux
mvnw.cmd spring-boot:run       # Windows
```

4. 访问 API 文档

启动后访问 Knife4j 文档页面：

```
http://localhost:8080/doc.html
```

### 前端启动

1. 安装依赖

```bash
cd frontend
npm install
```

2. 启动开发服务器

```bash
# H5 模式（浏览器预览）
npm run dev:h5

# 微信小程序模式
npm run dev:mp-weixin
```

3. 运行平台支持

uni-app 支持编译到以下平台：

| 命令 | 目标平台 |
|------|----------|
| `npm run dev:h5` | H5 网页 |
| `npm run dev:mp-weixin` | 微信小程序 |
| `npm run dev:mp-alipay` | 支付宝小程序 |
| `npm run dev:mp-baidu` | 百度小程序 |
| `npm run dev:mp-qq` | QQ 小程序 |
| `npm run dev:mp-toutiao` | 抖音小程序 |

---

## 安全配置

项目通过 Spring Security 配置了接口级别的访问控制：

- `/api/**` 路径对所有用户开放（**permitAll**）
- 其余请求需要认证
- 已关闭 CSRF 防御（适配前后端分离 API）
- 使用 JWT 进行用户身份验证

---

## 许可证

本项目仅用于学习与校园公益用途。
