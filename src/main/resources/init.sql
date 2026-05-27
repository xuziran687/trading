CREATE DATABASE IF NOT EXISTS `longan_apex` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `longan_apex`;

-- ----------------------------
-- 商品分类表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `goods_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父分类ID（一级为0）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序值',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0禁用 1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';

-- ----------------------------
-- 聊天会话表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `chat_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_id` bigint NOT NULL COMMENT '对方ID',
  `goods_id` bigint NOT NULL COMMENT '关联商品ID',
  `last_msg` text COMMENT '最后一条消息',
  `unread` int NOT NULL DEFAULT '0' COMMENT '未读消息数',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target_goods` (`user_id`,`target_id`,`goods_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天会话表';

-- ----------------------------
-- 聊天消息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `goods_id` bigint NOT NULL COMMENT '关联商品ID',
  `content` text NOT NULL COMMENT '消息内容',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '1文字 2图片 3系统消息',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_sender_receiver` (`sender_id`,`receiver_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天消息表';

-- ----------------------------
-- 商品表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `goods` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '商品标题',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `user_id` bigint NOT NULL COMMENT '卖家ID',
  `quality` tinyint NOT NULL COMMENT '1全新 299新 395新 4九成新 5七成新及以下',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0下架 1上架 2已卖出 3锁定中',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0未删 1已删',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `collect_count` int NOT NULL DEFAULT '0' COMMENT '收藏量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

-- ----------------------------
-- 商品收藏表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `goods_collect` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goods` (`user_id`,`goods_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品收藏表';

-- ----------------------------
-- 商品图片表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `goods_image` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品图片表';

-- ----------------------------
-- 商品标签表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `goods_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品标签表';

-- ----------------------------
-- 订单表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `seller_id` bigint NOT NULL COMMENT '卖家ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `price` decimal(10,2) NOT NULL COMMENT '成交价格',
  `point_amount` decimal(10,2) NOT NULL COMMENT '平台币支付金额',
  `status` tinyint NOT NULL COMMENT '0待付款 1待发货 2待收货 3已完成 4已取消 5退款中',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_no` varchar(64) DEFAULT NULL COMMENT '物流单号',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `receiver` varchar(50) DEFAULT NULL COMMENT '收件人',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- ----------------------------
-- 支付记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `pay_no` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `pay_type` tinyint NOT NULL COMMENT '1平台币 2微信 3支付宝 4银行卡 5其他',
  `amount` decimal(12,2) NOT NULL COMMENT '支付金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0待支付 1成功 2失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付记录表';

-- ----------------------------
-- 平台配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `platform_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` varchar(255) NOT NULL COMMENT '配置值',
  `desc` varchar(255) DEFAULT NULL COMMENT '说明',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台配置表';

-- ----------------------------
-- 退款表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_refund` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `amount` decimal(12,2) NOT NULL COMMENT '退款金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0申请中 1同意 2拒绝 3已退款',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款表';

-- ----------------------------
-- 评价表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `buyer_id` bigint NOT NULL COMMENT '评价人ID',
  `seller_id` bigint NOT NULL COMMENT '被评价人ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `score` tinyint NOT NULL COMMENT '1-5分',
  `content` text COMMENT '评价内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评价表';

-- ----------------------------
-- 评价图片表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_review_image` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `review_id` bigint NOT NULL COMMENT '评价ID',
  `url` varchar(255) NOT NULL COMMENT '图片URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_review_id` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评价图片表';

-- ----------------------------
-- 系统消息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `chat_sys_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `type` tinyint NOT NULL COMMENT '1订单 2系统 3活动',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统消息表';

-- ----------------------------
-- 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `password` varchar(100) NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0禁用 1正常',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0未删 1已删',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- 用户行为表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_behavior` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `goods_id` bigint NOT NULL COMMENT '商品ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `behavior` tinyint NOT NULL COMMENT '1浏览 2收藏 3加购 4下单 5搜索',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为表';

-- ----------------------------
-- 用户信用表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_credit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `score` int NOT NULL DEFAULT '100' COMMENT '信用分',
  `level` tinyint NOT NULL DEFAULT '1' COMMENT '信用等级',
  `good_num` int NOT NULL DEFAULT '0' COMMENT '好评数',
  `bad_num` int NOT NULL DEFAULT '0' COMMENT '差评数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信用表';

-- ----------------------------
-- 用户详情表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `gender` tinyint DEFAULT '0' COMMENT '0未知 1男 2女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `address` varchar(255) DEFAULT NULL COMMENT '常用地址',
  `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户详情表';

-- ----------------------------
-- 用户钱包表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '平台币余额',
  `freeze` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `total_income` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总收入',
  `total_outcome` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总支出',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户钱包表';

-- ----------------------------
-- 平台币流水表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_wallet_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '1收入 2支出',
  `business_type` tinyint NOT NULL COMMENT '1充值 2消费 3退款 4佣金 5签到 6活动',
  `amount` decimal(12,2) NOT NULL COMMENT '变动金额',
  `balance` decimal(12,2) NOT NULL COMMENT '变动后余额',
  `order_no` varchar(64) DEFAULT NULL COMMENT '关联订单号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_business_type` (`business_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台币流水表';

-- ----------------------------
-- 初始测试数据
-- ----------------------------
INSERT IGNORE INTO `goods_category` (`id`, `name`, `parent_id`, `sort`, `status`) VALUES
(1, '电子产品', 0, 1, 1),
(2, '服饰鞋包', 0, 2, 1),
(3, '家具家电', 0, 3, 1),
(4, '书籍文具', 0, 4, 1),
(5, '运动户外', 0, 5, 1),
(6, '美妆个护', 0, 6, 1),
(7, '母婴玩具', 0, 7, 1),
(8, '游戏娱乐', 0, 8, 1),
(9, '生活服务', 0, 9, 1),
(101, '手机', 1, 1, 1),
(102, '电脑', 1, 2, 1),
(103, '平板', 1, 3, 1),
(104, '摄影摄像', 1, 4, 1),
(105, '耳机/音箱', 1, 5, 1),
(201, '男装', 2, 1, 1),
(202, '女装', 2, 2, 1),
(203, '鞋靴', 2, 3, 1),
(204, '箱包', 2, 4, 1),
(301, '家具', 3, 1, 1),
(302, '家电', 3, 2, 1),
(401, '书籍', 4, 1, 1),
(402, '文具/办公', 4, 2, 1),
(501, '运动器材', 5, 1, 1),
(502, '户外装备', 5, 2, 1),
(601, '护肤', 6, 1, 1),
(602, '彩妆', 6, 2, 1),
(701, '母婴用品', 7, 1, 1),
(702, '玩具', 7, 2, 1),
(801, '游戏主机', 8, 1, 1),
(802, '游戏周边', 8, 2, 1),
(901, '生活服务', 9, 1, 1);

INSERT IGNORE INTO `user` (`id`, `username`, `email`, `password`, `nickname`, `avatar`, `status`) VALUES
(1001, 'user1001', '123456789', '000000', '小明', 'https://picsum.photos/seed/avatar1001/100/100', 1),
(1002, 'user1002', '987654321', '000000', '小王', 'https://picsum.photos/seed/avatar1002/100/100', 1),
(2019293046126219265, 'admin', 'longan@gmail.com', '123456', '新用户955025f3b0374909', NULL, 1);

INSERT IGNORE INTO `user_profile` (`id`, `user_id`, `gender`, `birthday`, `address`, `signature`) VALUES
(1, 1001, 1, '2000-01-01', '北京市海淀区', '热爱生活'),
(2, 1002, 1, '1999-05-20', '上海市浦东新区', '诚信交易');

INSERT IGNORE INTO `user_credit` (`id`, `user_id`, `score`, `level`, `good_num`, `bad_num`) VALUES
(1, 1001, 95, 3, 20, 1),
(2, 1002, 98, 4, 35, 0);

INSERT IGNORE INTO `user_behavior` (`id`, `user_id`, `goods_id`, `category_id`, `behavior`) VALUES
(1, 1001, 1001, 101, 1),
(2, 1001, 1002, 101, 2);

INSERT IGNORE INTO `user_wallet` (`id`, `user_id`, `balance`, `freeze`, `total_income`, `total_outcome`) VALUES
(1, 1001, 1500.00, 200.00, 2000.00, 500.00),
(2, 1002, 3000.00, 0.00, 5000.00, 2000.00);

INSERT IGNORE INTO `goods` (`id`, `title`, `description`, `price`, `original_price`, `category_id`, `user_id`, `quality`, `status`, `view_count`, `collect_count`) VALUES
-- 用户1002的上架商品
(1001, 'iPhone 13 128G 蓝色', '95新，无划痕，功能完好', 3500.00, 5999.00, 101, 1002, 3, 1, 120, 15),
(1002, 'MacBook Pro 13寸 M1', '99新，几乎没用过', 6800.00, 9999.00, 102, 1002, 2, 1, 80, 10),
(1003, 'iPad Air 4 64G', '全原装，电池健康89%，带原装充电器', 2800.00, 4799.00, 103, 1002, 3, 1, 65, 8),
(1004, '戴森V8无绳吸尘器', '去年双十一购买，实际使用不到10次', 1500.00, 2999.00, 302, 1002, 1, 1, 210, 25),
(1005, '索尼WH-1000XM4头戴式降噪耳机', '佩戴舒适，降噪效果极佳，箱说全', 1200.00, 2499.00, 105, 1002, 2, 1, 340, 42),
(1006, '全新Kindle Paperwhite 5', '朋友送的，全新未拆封，6.8寸屏幕', 600.00, 999.00, 401, 1002, 1, 1, 88, 12),
(1007, 'Switch OLED + 塞尔达王国之泪', 'OLED版，送塞尔达王国之泪卡带和Pro手柄', 2000.00, 2600.00, 801, 1002, 2, 1, 520, 68),
(1008, 'Canon EOS R6 全画幅微单', '快门数不到5000，99新，带24-105套头', 12000.00, 15999.00, 104, 1002, 2, 1, 175, 22),
(1009, 'Dell U2723QX 4K显示器', 'LG IPS Black面板，Type-C 90W反向充电', 2800.00, 4299.00, 102, 1002, 3, 1, 94, 11),
(1010, '罗技G Pro X Superlight鼠标', '白色，轻量化设计仅63g，适合FPS玩家', 350.00, 799.00, 802, 1002, 1, 1, 440, 55),
(1011, 'DJI Mini 4 Pro 畅飞套装', '带三块电池和充电管家，送包和滤镜', 4500.00, 7388.00, 104, 1002, 2, 1, 310, 38),
(1012, '华为FreeBuds Pro 3', '麒麟芯片加持，降噪效果一流', 800.00, 1499.00, 105, 1002, 3, 1, 67, 9),
-- 用户1001的上架商品
(1013, 'Nike Air Force 1 白色 42码', '穿了两次，鞋底干净，鞋面无折痕', 400.00, 799.00, 203, 1001, 2, 1, 820, 95),
(1014, 'PS5 国行光驱版+双手柄', '买了玩了几次，吃灰中，送三款游戏光盘', 3200.00, 3899.00, 801, 1001, 2, 1, 430, 52),
(1015, '雅诗兰黛小棕瓶精华100ml', '朋友代购带回，全新未开封', 600.00, 1099.00, 601, 1001, 1, 1, 150, 18),
(1016, '乐高兰博基尼Sián 42161', '全新未拆，正品带盒说', 2200.00, 3499.00, 702, 1001, 1, 1, 78, 14),
(1017, '小米Air 2 Pro降噪耳机', '用过几次，想换华为就出了', 350.00, 699.00, 105, 1001, 3, 1, 255, 28),
(1018, '三星1TB 980 Pro SSD', '写入量不到2TB，速度飞快', 550.00, 899.00, 102, 1001, 3, 1, 112, 13),
(1019, '飞利浦电动牙刷HX9352', '钻石亮白款，全新仅拆封，送刷头', 200.00, 499.00, 601, 1001, 1, 1, 190, 21),
(1020, '富士instax mini90拍立得', '复古造型，买来只用了一盒相纸', 500.00, 899.00, 104, 1001, 2, 1, 310, 36),
(1021, '小米米家加湿器', '用了一个秋天，制热效果不错', 100.00, 199.00, 302, 1001, 3, 1, 135, 16),
(1022, '日本星巴克樱花城市杯', '去年樱花季限量版，收藏用，全新', 100.00, 200.00, 901, 1001, 1, 1, 580, 88),
(1023, '凯叔讲故事年卡会员', '给孩子买的，已经用不上，官方可查有效期', 200.00, 398.00, 701, 1001, 1, 1, 25, 3),
(1024, '任天堂Labo Variety Kit', '只拼了钢琴，其他未拆，陪孩子玩的', 180.00, 499.00, 802, 1001, 3, 1, 44, 6),
-- 已下架的商品(status=0)
(1025, '东野圭吾小说全集20册', '正版，9成新，无缺页无损', 80.00, 200.00, 401, 1002, 4, 0, 320, 40),
(1026, '李宁韦德之道9篮球鞋 43码', '实战10场左右，水晶底有磨损', 350.00, 999.00, 501, 1001, 4, 0, 240, 27),
(1027, '小米手环8 Pro', '换新手环了，旧的出掉', 200.00, 399.00, 105, 1002, 3, 0, 160, 18),
(1028, '芭比娃娃梦幻城堡套装', '孩子长大了不玩了，配件齐全', 150.00, 599.00, 702, 1002, 4, 0, 85, 9),
(1029, '始祖鸟Beta AR冲锋衣 M码', '穿过几次，轻微使用痕迹，正品有吊牌', 2500.00, 4500.00, 502, 1001, 3, 0, 420, 48),
(1030, '华为MatePad 11 128G', '屏幕有细微划痕，不影响使用', 1600.00, 2799.00, 103, 1001, 4, 0, 95, 11),
-- 已卖出的商品(status=2)
(1031, '飞利浦空气炸锅HD9270', '超大容量5.6L，用得很爱惜', 250.00, 499.00, 302, 1001, 2, 2, 480, 55),
(1032, '兰蔻小黑瓶二代50ml', '在保，包装盒发票齐全', 450.00, 799.00, 601, 1002, 2, 2, 290, 33);

INSERT IGNORE INTO `goods_image` (`id`, `goods_id`, `url`, `sort`) VALUES
(1, 1001, 'https://picsum.photos/seed/goods1001/400/400', 1),
(2, 1001, 'https://picsum.photos/seed/goods1001b/400/400', 2),
(3, 1002, 'https://picsum.photos/seed/goods1002/400/400', 1),
(4, 1003, 'https://picsum.photos/seed/goods1003/400/400', 1),
(5, 1004, 'https://picsum.photos/seed/goods1004/400/400', 1),
(6, 1005, 'https://picsum.photos/seed/goods1005/400/400', 1),
(7, 1006, 'https://picsum.photos/seed/goods1006/400/400', 1),
(8, 1007, 'https://picsum.photos/seed/goods1007/400/400', 1),
(9, 1008, 'https://picsum.photos/seed/goods1008/400/400', 1),
(10, 1009, 'https://picsum.photos/seed/goods1009/400/400', 1),
(11, 1010, 'https://picsum.photos/seed/goods1010/400/400', 1),
(12, 1011, 'https://picsum.photos/seed/goods1011/400/400', 1),
(13, 1012, 'https://picsum.photos/seed/goods1012/400/400', 1),
(14, 1013, 'https://picsum.photos/seed/goods1013/400/400', 1),
(15, 1014, 'https://picsum.photos/seed/goods1014/400/400', 1),
(16, 1015, 'https://picsum.photos/seed/goods1015/400/400', 1),
(17, 1016, 'https://picsum.photos/seed/goods1016/400/400', 1),
(18, 1017, 'https://picsum.photos/seed/goods1017/400/400', 1),
(19, 1018, 'https://picsum.photos/seed/goods1018/400/400', 1),
(20, 1019, 'https://picsum.photos/seed/goods1019/400/400', 1),
(21, 1020, 'https://picsum.photos/seed/goods1020/400/400', 1),
(22, 1021, 'https://picsum.photos/seed/goods1021/400/400', 1),
(23, 1022, 'https://picsum.photos/seed/goods1022/400/400', 1),
(24, 1023, 'https://picsum.photos/seed/goods1023/400/400', 1),
(25, 1024, 'https://picsum.photos/seed/goods1024/400/400', 1),
(26, 1025, 'https://picsum.photos/seed/goods1025/400/400', 1),
(27, 1026, 'https://picsum.photos/seed/goods1026/400/400', 1),
(28, 1027, 'https://picsum.photos/seed/goods1027/400/400', 1),
(29, 1028, 'https://picsum.photos/seed/goods1028/400/400', 1),
(30, 1029, 'https://picsum.photos/seed/goods1029/400/400', 1),
(31, 1030, 'https://picsum.photos/seed/goods1030/400/400', 1),
(32, 1031, 'https://picsum.photos/seed/goods1031/400/400', 1),
(33, 1032, 'https://picsum.photos/seed/goods1032/400/400', 1);

INSERT IGNORE INTO `goods_tag` (`id`, `goods_id`, `tag_name`) VALUES
(1, 1001, 'iPhone'),
(2, 1001, '蓝色'),
(3, 1002, 'MacBook'),
(4, 1003, 'iPad'),
(5, 1004, '戴森'),
(6, 1005, '索尼'),
(7, 1006, 'Kindle'),
(8, 1007, 'Switch'),
(9, 1008, '佳能'),
(10, 1009, '戴尔'),
(11, 1010, '罗技'),
(12, 1011, '大疆'),
(13, 1012, '华为'),
(14, 1013, 'Nike'),
(15, 1014, 'PS5'),
(16, 1015, '雅诗兰黛'),
(17, 1016, '乐高'),
(18, 1017, '小米'),
(19, 1018, '三星'),
(20, 1019, '飞利浦'),
(21, 1020, '富士'),
(22, 1021, '小米'),
(23, 1022, '星巴克'),
(24, 1023, '凯叔'),
(25, 1024, '任天堂'),
(26, 1025, '东野圭吾'),
(27, 1026, '李宁'),
(28, 1027, '小米'),
(29, 1028, '芭比'),
(30, 1029, '始祖鸟');

INSERT IGNORE INTO `goods_collect` (`id`, `user_id`, `goods_id`) VALUES
(1, 1001, 1001),
(2, 1001, 1002);

INSERT IGNORE INTO `chat_conversation` (`id`, `user_id`, `target_id`, `goods_id`, `last_msg`, `unread`) VALUES
(3001, 1001, 1002, 1001, '你好，商品还在吗？', 2);

INSERT IGNORE INTO `chat_message` (`id`, `sender_id`, `receiver_id`, `goods_id`, `content`, `type`, `status`) VALUES
(4001, 1001, 1002, 1001, '你好，商品还在吗？', 1, 1),
(4002, 1002, 1001, 1001, '在的，随时可以拍', 1, 0);

INSERT IGNORE INTO `order` (`id`, `order_no`, `buyer_id`, `seller_id`, `goods_id`, `price`, `point_amount`, `status`, `receiver`, `phone`, `address`) VALUES
(2001, 'O202501010001', 1001, 1002, 1001, 3500.00, 3500.00, 0, '小明', '13800138000', '北京市海淀区');

INSERT IGNORE INTO `chat_sys_message` (`id`, `user_id`, `title`, `content`, `type`) VALUES
(1, 1001, '订单创建成功', '您的订单 O202501010001 已创建，请尽快支付', 1);

INSERT IGNORE INTO `user_wallet_log` (`id`, `user_id`, `type`, `business_type`, `amount`, `balance`, `order_no`, `remark`) VALUES
(1, 1001, 1, 1, 100.00, 1500.00, 'R202501010001', '充值'),
(2, 1001, 2, 2, 200.00, 1300.00, 'O202501010001', '订单支付');

INSERT IGNORE INTO `platform_config` (`id`, `config_key`, `config_value`, `desc`) VALUES
(1, 'point_rate', '10', '1元=10平台币'),
(2, 'fee_rate', '0.02', '交易手续费2%'),
(3, 'withdraw_min', '10', '最低提现10元'),
(4, 'register_point', '100', '注册送100平台币'),
(5, 'sign_point', '5', '签到送5平台币');

-- ----------------------------
-- 用户收货地址表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver` varchar(50) NOT NULL COMMENT '收件人',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `address` varchar(255) NOT NULL COMMENT '详细地址',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收货地址表';
