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
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0下架 1上架 2已卖出',
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
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
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
-- 订单地址表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `receiver` varchar(50) NOT NULL COMMENT '收件人',
  `phone` varchar(20) NOT NULL COMMENT '电话',
  `address` varchar(255) NOT NULL COMMENT '地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单地址表';

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
INSERT INTO `goods_category` (`id`, `name`, `parent_id`, `sort`, `status`) VALUES
(1, '电子产品', 0, 1, 1),
(2, '服饰鞋包', 0, 2, 1),
(101, '手机', 1, 1, 1),
(102, '电脑', 1, 2, 1);

INSERT INTO `user` (`id`, `username`, `email`, `password`, `nickname`, `avatar`, `status`) VALUES
(1001, 'user1001', '123456789', '000000', '小明', 'https://avatar.png', 1),
(1002, 'user1002', '987654321', '000000', '小王', 'https://avatar2.png', 1),
(2019293046126219265, 'admin', 'longan@gmail.com', '123456', '新用户955025f3b0374909', NULL, 1);

INSERT INTO `user_profile` (`id`, `user_id`, `gender`, `birthday`, `address`, `signature`) VALUES
(1, 1001, 1, '2000-01-01', '北京市海淀区', '热爱生活'),
(2, 1002, 1, '1999-05-20', '上海市浦东新区', '诚信交易');

INSERT INTO `user_credit` (`id`, `user_id`, `score`, `level`, `good_num`, `bad_num`) VALUES
(1, 1001, 95, 3, 20, 1),
(2, 1002, 98, 4, 35, 0);

INSERT INTO `user_behavior` (`id`, `user_id`, `goods_id`, `category_id`, `behavior`) VALUES
(1, 1001, 1001, 101, 1),
(2, 1001, 1002, 101, 2);

INSERT INTO `user_wallet` (`id`, `user_id`, `balance`, `freeze`, `total_income`, `total_outcome`) VALUES
(1, 1001, 1500.00, 200.00, 2000.00, 500.00),
(2, 1002, 3000.00, 0.00, 5000.00, 2000.00);

INSERT INTO `goods` (`id`, `title`, `desc`, `price`, `original_price`, `category_id`, `user_id`, `quality`, `status`, `view_count`, `collect_count`) VALUES
(1001, 'iPhone 13 128G 蓝色', '95新，无划痕，功能完好', 3500.00, 5999.00, 101, 1002, 3, 1, 120, 15),
(1002, 'MacBook Pro 13寸 M1', '99新，几乎没用过', 6800.00, 9999.00, 102, 1002, 2, 1, 80, 10);

INSERT INTO `goods_image` (`id`, `goods_id`, `url`, `sort`) VALUES
(1, 1001, 'https://img1.png', 1),
(2, 1001, 'https://img2.png', 2),
(3, 1002, 'https://img3.png', 1);

INSERT INTO `goods_tag` (`id`, `goods_id`, `tag_name`) VALUES
(1, 1001, 'iPhone'),
(2, 1001, '蓝色'),
(3, 1002, 'MacBook');

INSERT INTO `goods_collect` (`id`, `user_id`, `goods_id`) VALUES
(1, 1001, 1001),
(2, 1001, 1002);

INSERT INTO `chat_conversation` (`id`, `user_id`, `target_id`, `goods_id`, `last_msg`, `unread`) VALUES
(3001, 1001, 1002, 1001, '你好，商品还在吗？', 2);

INSERT INTO `chat_message` (`id`, `sender_id`, `receiver_id`, `goods_id`, `content`, `type`, `status`) VALUES
(4001, 1001, 1002, 1001, '你好，商品还在吗？', 1, 1),
(4002, 1002, 1001, 1001, '在的，随时可以拍', 1, 0);

INSERT INTO `order` (`id`, `order_no`, `buyer_id`, `seller_id`, `goods_id`, `price`, `point_amount`, `status`) VALUES
(2001, 'O202501010001', 1001, 1002, 1001, 3500.00, 3500.00, 0);

INSERT INTO `order_address` (`id`, `order_id`, `receiver`, `phone`, `address`) VALUES
(1, 2001, '小明', '13800138000', '北京市海淀区');

INSERT INTO `chat_sys_message` (`id`, `user_id`, `title`, `content`, `type`) VALUES
(1, 1001, '订单创建成功', '您的订单 O202501010001 已创建，请尽快支付', 1);

INSERT INTO `user_wallet_log` (`id`, `user_id`, `type`, `business_type`, `amount`, `balance`, `order_no`, `remark`) VALUES
(1, 1001, 1, 1, 100.00, 1500.00, 'R202501010001', '充值'),
(2, 1001, 2, 2, 200.00, 1300.00, 'O202501010001', '订单支付');

INSERT INTO `platform_config` (`id`, `config_key`, `config_value`, `desc`) VALUES
(1, 'point_rate', '10', '1元=10平台币'),
(2, 'fee_rate', '0.02', '交易手续费2%'),
(3, 'withdraw_min', '10', '最低提现10元'),
(4, 'register_point', '100', '注册送100平台币'),
(5, 'sign_point', '5', '签到送5平台币');
