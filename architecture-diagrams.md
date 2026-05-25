# Longan Apex 系统架构文档

## 1. 系统架构图 (System Architecture Diagram)

```mermaid
graph TB
    subgraph "客户端层 (Client Layer)"
        CLI["Web浏览器<br/>Swagger UI"]
        MOB["移动端<br/>H5/App"]
    end

    subgraph "网关层 (Gateway Layer)"
        MVC["Spring Web MVC<br/>DispatcherServlet"]
        JWT["JWT 认证拦截器<br/>JwtTokenInterceptor"]
    end

    subgraph "控制层 (Controller Layer)"
        UC["UserController<br/>/api/user/**"]
        GC["GoodsController<br/>/api/goods/**"]
        OC["OrderController<br/>/api/order/**"]
        CC["CommonController<br/>/api/common/**"]
        CaC["CategoryController<br/>/api/category/**"]
    end

    subgraph "业务层 (Service Layer)"
        US["UserService"]
        UPS["UserProfileService"]
        UWS["UserWalletService"]
        UCS["UserCreditService"]
        UBS["UserBehaviorService"]
        GS["GoodsService"]
        GIS["GoodsImageService"]
        GCS["GoodsCollectService"]
        CaS["CategoryService"]
        OS["OrderService"]
        PS["PaymentService"]
        RS["RefundService"]
        RVS["ReviewService"]
        ChS["ChatService"]
        SMS["SysMessageService"]
        PCS["PlatformConfigService"]
    end

    subgraph "数据访问层 (DAO/Mapper Layer)"
        UM["UserMapper"]
        UPM["UserProfileMapper"]
        UWM["UserWalletMapper"]
        WLM["WalletLogMapper"]
        UCM["UserCreditMapper"]
        UBM["UserBehaviorMapper"]
        GM["GoodsMapper"]
        GIM["GoodsImageMapper"]
        GCM["GoodsCollectMapper"]
        GTM["GoodsTagMapper"]
        CaM["CategoryMapper"]
        OM["OrderMapper"]
        OAM["OrderAddressMapper"]
        PM["PaymentMapper"]
        RFM["RefundMapper"]
        RVM["ReviewMapper"]
        RIM["ReviewImageMapper"]
        CCM["ChatConversationMapper"]
        CMM["ChatMessageMapper"]
        SMM["SysMessageMapper"]
        PCM["PlatformConfigMapper"]
    end

    subgraph "基础设施层 (Infrastructure)"
        DB[("MySQL<br/>longan_apex")]
        FS[("文件存储<br/>/opt/longan/images/")]
        LOG["日志<br/>Logback"]
    end

    subgraph "公共组件 (Common Components)"
        R["统一响应<br/>Result / PageResult"]
        EX["全局异常处理<br/>GlobalExceptionHandler"]
        C["配置类<br/>OpenApi/MyBatis-Plus/WebMvc"]
        U["工具类<br/>UserContext / MyMetaObjectHandler"]
    end

    CLI --> MVC
    MOB --> MVC
    MVC --> JWT

    JWT --> UC
    JWT --> GC
    JWT --> OC
    JWT --> CC
    JWT --> CaC

    UC --> US
    UC --> UPS
    UC --> UWS
    GC --> GS
    GC --> GIS
    GC --> GCS
    GC --> CaS
    OC --> OS
    OC --> PS
    OC --> RS
    OC --> RVS
    CC --> PCS

    US --> UM
    UPS --> UPM
    UWS --> UWM
    UWS --> WLM
    UCS --> UCM
    UBS --> UBM
    GS --> GM
    GIS --> GIM
    GCS --> GCM
    GCS --> GTM
    CaS --> CaM
    OS --> OM
    PS --> PM
    RS --> RFM
    RVS --> RVM
    RVS --> RIM
    ChS --> CCM
    ChS --> CMM
    SMS --> SMM
    PCS --> PCM

    UM --> DB
    UPM --> DB
    UWM --> DB
    WLM --> DB
    UCM --> DB
    UBM --> DB
    GM --> DB
    GIM --> DB
    GCM --> DB
    GTM --> DB
    CaM --> DB
    OM --> DB
    OAM --> DB
    PM --> DB
    RFM --> DB
    RVM --> DB
    RIM --> DB
    CCM --> DB
    CMM --> DB
    SMM --> DB
    PCM --> DB

    CC --> FS

    JWT -.-> C
    UC -.-> R
    GC -.-> R
    OC -.-> R
    CC -.-> R
    US -.-> EX
    GS -.-> EX
    OS -.-> EX

    classDef client fill:#e1f5fe,stroke:#0288d1
    classDef gateway fill:#fff3e0,stroke:#f57c00
    classDef controller fill:#e8f5e9,stroke:#388e3c
    classDef service fill:#fce4ec,stroke:#d81b60
    classDef mapper fill:#f3e5f5,stroke:#7b1fa2
    classDef infra fill:#efebe9,stroke:#5d4037
    classDef common fill:#fff8e1,stroke:#fbc02d

    class CLI,MOB client
    class MVC,JWT gateway
    class UC,GC,OC,CC,CaC controller
    class US,UPS,UWS,UCS,UBS,GS,GIS,GCS,CaS,OS,PS,RS,RVS,ChS,SMS,PCS service
    class UM,UPM,UWM,WLM,UCM,UBM,GM,GIM,GCM,GTM,CaM,OM,OAM,PM,RFM,RVM,RIM,CCM,CMM,SMM,PCM mapper
    class DB,FS,LOG infra
    class R,EX,C,U common
```

---

## 2. 系统流程图 (System Flowchart)

### 2.1 用户注册流程

```mermaid
flowchart TD
    A["用户填写注册信息<br/>邮箱、密码、昵称"] --> B{"验证输入合法性"}
    B -->|"非法"| C["返回错误信息"]
    C --> A
    B -->|"合法"| D{"邮箱是否已注册?"}
    D -->|"是"| E["返回: 邮箱已存在"]
    E --> A
    D -->|"否"| F["密码加密 (BCrypt)"]
    F --> G["创建用户记录<br/>(User)"]
    G --> H["初始化用户钱包<br/>(UserWallet 余额0)"]
    H --> I["初始化用户信用<br/>(UserCredit 积分0)"]
    I --> J["创建用户信息<br/>(UserProfile)"]
    J --> K["生成 JWT Token"]
    K --> L["返回 Token + 用户信息"]
```

### 2.2 用户登录流程

```mermaid
flowchart TD
    A["用户输入邮箱 + 密码"] --> B{"验证输入"}
    B -->|"非法"| C["返回错误信息"]
    C --> A
    B -->|"合法"| D{"邮箱是否存在?"}
    D -->|"否"| E["返回: 用户不存在"]
    E --> A
    D -->|"是"| F{"密码是否正确?"}
    F -->|"否"| G["返回: 密码错误"]
    G --> A
    F -->|"是"| H["生成 JWT Token<br/>(含 userId)"]
    H --> I["记录用户行为<br/>(UserBehavior)"]
    I --> J["返回 Token + 用户信息"]
```

### 2.3 商品发布流程

```mermaid
flowchart TD
    A["用户登录<br/>(携带JWT)"] --> B{"JWT 验证"}
    B -->|"无效"| C["返回 401 未认证"]
    B -->|"有效"| D["用户填写商品信息<br/>标题、价格、描述、分类、标签"]
    D --> E["上传商品图片"]
    E --> F{"文件大小 < 10MB?"}
    F -->|"否"| G["返回: 文件过大"]
    G --> E
    F -->|"是"| H["保存图片到<br/>/opt/longan/images/"]
    H --> I["创建商品记录<br/>(Goods, 状态=1:上架)"]
    I --> J["批量保存商品图片<br/>(GoodsImage)"]
    J --> K["保存商品标签<br/>(GoodsTag)"]
    K --> L["返回商品ID + 成功"]
```

### 2.4 订单创建与交易流程

```mermaid
flowchart TD
    A["买家浏览商品"] --> B["买家点击 立即购买"]
    B --> C{"商品是否上架?"}
    C -->|"否"| D["返回: 商品已下架"]
    C -->|"是"| E["买家填写收货地址"]
    E --> F["创建订单<br/>订单状态=1(待付款)"]
    F --> G{"选择支付方式"}
    G -->|"余额支付"| H{"钱包余额充足?"}
    H -->|"否"| I["返回: 余额不足"]
    H -->|"是"| J["扣减钱包余额 + 冻结"]
    J --> K["记录钱包流水"]
    K --> L["创建支付记录<br/>状态=1(已支付)"]
    G -->|"微信/支付宝"| M["跳转第三方支付"]
    M --> L
    L --> N["订单状态=2(待发货)"]
    N --> O["卖家发货"]
    O --> P["订单状态=3(已发货)"]
    P --> Q["买家确认收货"]
    Q --> R["解冻资金 -> 卖家钱包"]
    R --> S["记录卖家钱包流水"]
    S --> T["订单状态=4(已完成)"]
    T --> U["买家可评价商品"]
    U --> V["更新卖家信用"]
```

### 2.5 退款流程

```mermaid
flowchart TD
    A["买家发起退款申请"] --> B{"订单状态是否为<br/>待发货或已发货?"}
    B -->|"否"| C["返回: 无法退款"]
    B -->|"是"| D["创建退款记录<br/>状态=0(待审核)"]
    D --> E{"卖家是否同意?"}
    E -->|"否"| F["退款关闭<br/>订单继续"]
    E -->|"是"| G["退款处理中"]
    G --> H{"支付方式?"}
    H -->|"余额"| I["退款至买家钱包"]
    H -->|"第三方"| J["原路退回"]
    I --> K["退款状态=1(已完成)"]
    J --> K
    K --> L["订单状态=5(已退款)"]
    K --> M["记录钱包流水"]
```

---

## 3. 数据流图 (Data Flow Diagram)

### 3.0 图例

```mermaid
graph LR
    E["外部实体<br/>External Entity"]
    P["处理过程<br/>Process"]
    DS["数据存储<br/>Data Store"]
    F["数据流<br/>Data Flow"]
```

### 3.1 第0层数据流图（上下文图）

```mermaid
graph TD
    subgraph "Longan Apex 二手交易平台"
        CORE["核心系统"]
    end

    BUYER(("买家"))
    SELLER(("卖家"))
    ADMIN(("管理员"))
    THIRD(("第三方支付"))

    BUYER -->|"注册信息/登录信息/<br/>商品浏览/下单/<br/>支付/评价"| CORE
    SELLER -->|"注册信息/登录信息/<br/>商品发布/订单管理"| CORE
    ADMIN -->|"平台配置/系统管理"| CORE
    CORE -->|"商品信息/订单状态/<br/>聊天消息/钱包记录"| BUYER
    CORE -->|"商品信息/订单通知/<br/>聊天消息/收款"| SELLER
    CORE -->|"操作结果/统计数据"| ADMIN
    CORE -->|"支付请求"| THIRD
    THIRD -->|"支付结果回调"| CORE
```

### 3.2 第1层数据流图

```mermaid
graph TD
    BUYER(("买家"))
    SELLER(("卖家"))

    subgraph "认证管理"
        AUTH["1. 用户认证"]
    end

    subgraph "用户管理"
        UM["2. 用户管理"]
    end

    subgraph "商品管理"
        GM["3. 商品管理"]
    end

    subgraph "订单管理"
        OM["4. 订单管理"]
    end

    subgraph "支付管理"
        PAY["5. 支付管理"]
    end

    subgraph "聊天管理"
        CHAT["6. 聊天管理"]
    end

    subgraph "系统管理"
        SYS["7. 系统管理"]
    end

    DS_USER[(D1 用户数据)]
    DS_GOODS[(D2 商品数据)]
    DS_ORDER[(D3 订单数据)]
    DS_PAY[(D4 支付数据)]
    DS_CHAT[(D5 聊天数据)]
    DS_CONFIG[(D6 配置数据)]
    DS_FILE[(D7 文件存储)]

    BUYER -->|"注册/登录请求"| AUTH
    SELLER -->|"注册/登录请求"| AUTH
    AUTH -->|"用户信息"| DS_USER

    BUYER -->|"获取个人信息"| UM
    SELLER -->|"获取/修改个人信息"| UM
    UM -->|"读写用户资料"| DS_USER

    SELLER -->|"发布/管理商品"| GM
    BUYER -->|"浏览/搜索/收藏"| GM
    GM -->|"读写商品数据"| DS_GOODS
    GM -->|"读写图片文件"| DS_FILE

    BUYER -->|"创建订单/确认收货"| OM
    SELLER -->|"发货/退款处理"| OM
    OM -->|"读写订单数据"| DS_ORDER
    OM -->|"读取商品数据"| DS_GOODS
    OM -->|"读取用户数据"| DS_USER

    OM -->|"发起支付"| PAY
    PAY -->|"读写支付记录"| DS_PAY
    PAY -->|"更新钱包余额"| DS_USER
    PAY -->|"更新订单状态"| DS_ORDER

    BUYER -->|"发送/接收消息"| CHAT
    SELLER -->|"发送/接收消息"| CHAT
    CHAT -->|"读写聊天数据"| DS_CHAT
    CHAT -->|"通知用户"| DS_USER

    ADMIN -->|"管理平台配置"| SYS
    SYS -->|"读写配置"| DS_CONFIG
```

### 3.3 第2层数据流图 - 订单处理

```mermaid
graph TD
    BUYER(("买家"))
    SELLER(("卖家"))
    THIRD(("第三方支付"))

    subgraph "订单处理"
        CHECK["2.1 校验库存/状态"]
        CREATE["2.2 创建订单"]
        ADDR["2.3 保存收货地址"]
        PAY_REQ["2.4 发起支付"]
        PAY_CB["2.5 支付回调处理"]
        SHIP["2.6 发货处理"]
        CONFIRM["2.7 确认收货"]
        REFUND["2.8 退款处理"]
    end

    DS_GOODS[(D2 商品数据)]
    DS_ORDER[(D3 订单数据)]
    DS_PAY[(D4 支付数据)]
    DS_USER[(D1 用户数据)]

    BUYER -->|"购买请求"| CHECK
    CHECK -->|"商品信息"| DS_GOODS
    CHECK -->|"验证通过"| CREATE
    CREATE -->|"写入订单"| DS_ORDER
    CREATE -->|"写入地址"| ADDR
    ADDR -->|"收货地址"| DS_ORDER

    CREATE -->|"支付信息"| PAY_REQ
    PAY_REQ -->|"支付记录"| DS_PAY
    PAY_REQ -->|"扣减余额"| DS_USER
    PAY_REQ -->|"支付请求"| THIRD
    THIRD -->|"回调通知"| PAY_CB
    PAY_CB -->|"更新支付状态"| DS_PAY
    PAY_CB -->|"更新订单状态"| DS_ORDER

    SELLER -->|"发货请求"| SHIP
    SHIP -->|"更新订单状态"| DS_ORDER

    BUYER -->|"确认收货"| CONFIRM
    CONFIRM -->|"解冻资金"| DS_USER
    CONFIRM -->|"更新订单状态"| DS_ORDER

    BUYER -->|"退款申请"| REFUND
    SELLER -->|"退款审核"| REFUND
    REFUND -->|"退款记录"| DS_ORDER
    REFUND -->|"退还资金"| DS_USER
```

---

## 4. E-R 图 (Entity-Relationship Diagram)

```mermaid
erDiagram
    %% ==================== 用户模块 ====================
    user {
        bigint id PK "主键ID"
        varchar email "邮箱(登录账号)"
        varchar password "密码(BCrypt加密)"
        varchar nickname "昵称"
        varchar avatar "头像URL"
        tinyint role "角色:1买家2卖家"
        tinyint status "状态:1正常2禁用"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    user_profile {
        bigint id PK
        bigint user_id FK "用户ID"
        tinyint gender "性别:0未知1男2女"
        date birthday "生日"
        varchar phone "手机号"
        varchar address "地址"
        varchar signature "个性签名"
        datetime create_time
        datetime update_time
    }

    user_wallet {
        bigint id PK
        bigint user_id FK "用户ID"
        decimal balance "余额"
        decimal freeze "冻结金额"
        decimal total_income "累计收入"
        decimal total_outcome "累计支出"
        datetime create_time
        datetime update_time
    }

    user_wallet_log {
        bigint id PK
        bigint user_id FK "用户ID"
        bigint wallet_id FK "钱包ID"
        decimal amount "变动金额(±)"
        varchar type "类型:收入/支出/退款/冻结"
        varchar biz_type "业务类型"
        varchar remark "备注"
        datetime create_time
    }

    user_credit {
        bigint id PK
        bigint user_id FK "用户ID"
        int score "信用积分"
        int level "信用等级"
        int good_reviews "好评数"
        int bad_reviews "差评数"
        datetime create_time
        datetime update_time
    }

    user_behavior {
        bigint id PK
        bigint user_id FK "用户ID"
        varchar behavior_type "行为类型"
        bigint target_id "目标ID"
        varchar remark "备注"
        datetime create_time
    }

    %% ==================== 商品模块 ====================
    goods {
        bigint id PK
        bigint seller_id FK "卖家ID"
        bigint category_id FK "分类ID"
        varchar title "商品标题"
        text description "商品描述"
        decimal price "价格"
        varchar quality "成色"
        tinyint status "状态:1上架2下架3售出"
        int view_count "浏览量"
        int collect_count "收藏量"
        datetime create_time
        datetime update_time
    }

    goods_category {
        bigint id PK
        varchar name "分类名称"
        bigint parent_id "父分类ID(0=顶级)"
        int sort "排序"
        tinyint status "状态"
        datetime create_time
    }

    goods_image {
        bigint id PK
        bigint goods_id FK "商品ID"
        varchar image_url "图片URL"
        int sort "排序"
        datetime create_time
    }

    goods_collect {
        bigint id PK
        bigint user_id FK "用户ID"
        bigint goods_id FK "商品ID"
        datetime create_time
    }

    goods_tag {
        bigint id PK
        bigint goods_id FK "商品ID"
        varchar tag_name "标签名"
        datetime create_time
    }

    %% ==================== 订单模块 ====================
    order {
        bigint id PK
        varchar order_no "订单号"
        bigint buyer_id FK "买家ID"
        bigint seller_id FK "卖家ID"
        bigint goods_id FK "商品ID"
        decimal goods_price "商品价格"
        decimal total_price "总价"
        decimal freight "运费"
        tinyint status "状态:1待付款2待发货3已发货4已完成5已退款6已关闭"
        varchar buyer_remark "买家备注"
        datetime create_time
        datetime update_time
    }

    order_address {
        bigint id PK
        bigint order_id FK "订单ID"
        varchar consignee "收货人"
        varchar phone "电话"
        varchar province "省"
        varchar city "市"
        varchar district "区"
        varchar detail_address "详细地址"
        datetime create_time
    }

    order_payment {
        bigint id PK
        bigint order_id FK "订单ID"
        bigint user_id FK "用户ID"
        decimal amount "支付金额"
        varchar pay_type "支付方式:coin/wechat/alipay"
        tinyint status "状态:0未支付1已支付2已退款"
        varchar trade_no "第三方支付流水号"
        datetime create_time
        datetime update_time
    }

    order_refund {
        bigint id PK
        bigint order_id FK "订单ID"
        bigint user_id FK "用户ID"
        decimal amount "退款金额"
        varchar reason "退款原因"
        tinyint status "状态:0待审核1已完成2已拒绝"
        datetime create_time
        datetime update_time
    }

    order_review {
        bigint id PK
        bigint order_id FK "订单ID"
        bigint user_id FK "用户ID"
        bigint goods_id FK "商品ID"
        tinyint score "评分(1-5)"
        text content "评价内容"
        datetime create_time
        datetime update_time
    }

    order_review_image {
        bigint id PK
        bigint review_id FK "评价ID"
        varchar image_url "图片URL"
        datetime create_time
    }

    %% ==================== 聊天模块 ====================
    chat_conversation {
        bigint id PK
        bigint user1_id FK "用户1"
        bigint user2_id FK "用户2"
        varchar last_message "最后一条消息"
        datetime last_time "最后消息时间"
        datetime create_time
        datetime update_time
    }

    chat_message {
        bigint id PK
        bigint conversation_id FK "会话ID"
        bigint sender_id FK "发送方"
        bigint receiver_id FK "接收方"
        text content "消息内容"
        tinyint msg_type "消息类型:1文本2图片3系统"
        tinyint is_read "是否已读"
        datetime create_time
    }

    chat_sys_message {
        bigint id PK
        bigint user_id FK "用户ID"
        varchar title "标题"
        text content "内容"
        tinyint msg_type "消息类型"
        tinyint is_read "是否已读"
        datetime create_time
    }

    %% ==================== 公共模块 ====================
    platform_config {
        bigint id PK
        varchar config_key "配置键"
        text config_value "配置值"
        varchar remark "备注"
        datetime create_time
        datetime update_time
    }

    %% ==================== 关系定义 ====================
    %% 用户模块关系
    user ||--o| user_profile : "has"
    user ||--o| user_wallet : "has"
    user ||--o| user_credit : "has"
    user ||--o{ user_behavior : "has"
    user_wallet ||--o{ user_wallet_log : "has"

    %% 商品模块关系
    user ||--o{ goods : "发布"
    goods_category ||--o{ goods : "属于"
    goods ||--o{ goods_image : "拥有"
    goods ||--o{ goods_tag : "拥有"
    user ||--o{ goods_collect : "收藏"
    goods ||--o{ goods_collect : "被收藏"

    %% 订单模块关系
    user ||--o{ order : "买家(下单)"
    user ||--o{ order : "卖家(接单)"
    goods ||--o{ order : "关联"
    order ||--o| order_address : "配送至"
    order ||--o{ order_payment : "支付记录"
    order ||--o{ order_refund : "退款记录"
    order ||--o{ order_review : "评价"
    order_review ||--o{ order_review_image : "评价图片"

    %% 聊天模块关系
    user ||--o{ chat_conversation : "参与"
    chat_conversation ||--o{ chat_message : "包含"
    user ||--o{ chat_message : "发送"
    user ||--o{ chat_sys_message : "接收"
```

---

## 5. 模块依赖关系图

```mermaid
graph TD
    subgraph "用户模块 (user)"
        U_ENTITY["User / UserProfile<br/>UserWallet / WalletLog<br/>UserCredit / UserBehavior"]
    end
    subgraph "商品模块 (goods)"
        G_ENTITY["Goods / Category<br/>GoodsImage / GoodsCollect<br/>GoodsTag"]
    end
    subgraph "订单模块 (order)"
        O_ENTITY["Order / OrderAddress<br/>Payment / Refund<br/>Review / ReviewImage"]
    end
    subgraph "聊天模块 (chat)"
        C_ENTITY["ChatConversation<br/>ChatMessage<br/>SysMessage"]
    end
    subgraph "公共模块 (common)"
        CO_ENTITY["PlatformConfig<br/>文件上传"]
    end
    subgraph "基础设施模块"
        JWT["JWT 认证"]
        CONFIG["配置管理"]
        RESULT["统一响应"]
        EXCEPTION["全局异常"]
        UTIL["工具类"]
    end

    G_ENTITY -.->|"订单引用商品"| O_ENTITY
    U_ENTITY -.->|"用户发布商品"| G_ENTITY
    U_ENTITY -.->|"用户创建订单"| O_ENTITY
    G_ENTITY -.->|"商品被收藏"| U_ENTITY
    O_ENTITY -.->|"订单含用户"| U_ENTITY
    C_ENTITY -.->|"聊天涉及用户"| U_ENTITY

    JWT -.->|"认证拦截"| U_ENTITY
    JWT -.->|"认证拦截"| G_ENTITY
    JWT -.->|"认证拦截"| O_ENTITY
    JWT -.->|"认证拦截"| C_ENTITY

    RESULT -.->|"统一包装"| U_ENTITY
    RESULT -.->|"统一包装"| G_ENTITY
    RESULT -.->|"统一包装"| O_ENTITY
    RESULT -.->|"统一包装"| C_ENTITY
    RESULT -.->|"统一包装"| CO_ENTITY
```

---

## 6. 数据库表关系总览

```mermaid
flowchart LR
    USER["user<br/>用户表"]
    PROFILE["user_profile<br/>用户详情表"]
    WALLET["user_wallet<br/>钱包表"]
    WALLET_LOG["user_wallet_log<br/>钱包流水表"]
    CREDIT["user_credit<br/>信用表"]
    BEHAVIOR["user_behavior<br/>行为表"]

    USER --- PROFILE
    USER --- WALLET
    USER --- CREDIT
    USER --- BEHAVIOR
    WALLET --- WALLET_LOG

    CATEGORY["goods_category<br/>分类表"]
    GOODS["goods<br/>商品表"]
    IMAGE["goods_image<br/>商品图片表"]
    COLLECT["goods_collect<br/>收藏表"]
    TAG["goods_tag<br/>标签表"]

    CATEGORY --- GOODS
    GOODS --- IMAGE
    GOODS --- COLLECT
    GOODS --- TAG
    USER --- COLLECT
    USER --- GOODS

    ORDER["order<br/>订单表"]
    ADDRESS["order_address<br/>地址表"]
    PAYMENT["order_payment<br/>支付表"]
    REFUND["order_refund<br/>退款表"]
    REVIEW["order_review<br/>评价表"]
    REVIEW_IMG["order_review_image<br/>评价图片表"]

    USER --- ORDER
    GOODS --- ORDER
    ORDER --- ADDRESS
    ORDER --- PAYMENT
    ORDER --- REFUND
    ORDER --- REVIEW
    REVIEW --- REVIEW_IMG

    CONVERSATION["chat_conversation<br/>会话表"]
    MESSAGE["chat_message<br/>消息表"]
    SYS_MSG["chat_sys_message<br/>系统消息表"]

    USER --- CONVERSATION
    CONVERSATION --- MESSAGE
    USER --- SYS_MSG

    CONFIG["platform_config<br/>配置表"]
