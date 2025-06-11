CREATE DATABASE docnest_db;

use docnest_db;

CREATE TABLE `hib_user` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                            `username` VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名（唯一）',
                            `email` VARCHAR(100) UNIQUE COMMENT '电子邮箱（可选唯一）',
                            `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
                            `avatar_url` VARCHAR(255) COMMENT '用户头像 URL',
                            `status` VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '用户状态（ACTIVE / BANNED / DELETED）',
                            `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
    COMMENT='用户表';

CREATE TABLE `hib_organization` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                    `name` VARCHAR(255) NOT NULL UNIQUE COMMENT '组织名称（唯一）',
                                    `description` TEXT COMMENT '组织描述',
                                    `owner_id` BIGINT NOT NULL COMMENT '组织拥有者（用户ID）',
                                    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
    COMMENT='组织/团队表';

CREATE TABLE `hib_knowledge_base` (
                                      `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                      `name` VARCHAR(255) NOT NULL COMMENT '知识库名称',
                                      `description` TEXT COMMENT '知识库描述',
                                      `type` VARCHAR(50) DEFAULT 'PRIVATE' COMMENT '知识库类型（PRIVATE / PUBLIC）',
                                      `owner_id` BIGINT NOT NULL COMMENT '所有者用户ID',
                                      `organization_id` BIGINT COMMENT '所属组织ID',
                                      `cover_url` VARCHAR(255) COMMENT '封面图 URL',
                                      `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
    COMMENT='知识库表';

CREATE TABLE `hib_knowledge_base_collaborator` (
                                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                                   `knowledge_base_id` BIGINT NOT NULL COMMENT '所属知识库ID',
                                                   `user_id` BIGINT NOT NULL COMMENT '协作者用户ID',
                                                   `role` VARCHAR(50) DEFAULT 'EDITOR' COMMENT '角色（VIEWER / EDITOR / ADMIN）',
                                                   `joined_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                                                   UNIQUE KEY `uk_kb_user` (`knowledge_base_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识库协作者表';

CREATE TABLE `hib_document` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                `knowledge_base_id` BIGINT NOT NULL COMMENT '所属知识库ID',
                                `parent_id` BIGINT DEFAULT NULL COMMENT '父文档ID（目录结构）',
                                `title` VARCHAR(255) NOT NULL COMMENT '文档标题',
                                `type` VARCHAR(50) DEFAULT 'DOC' COMMENT '类型（DOC / FOLDER）',
                                `owner_id` BIGINT NOT NULL COMMENT '文档所有者ID',
                                `order` INT DEFAULT 0 COMMENT '文档顺序排序值',
                                `status` VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '文档状态（ACTIVE / DELETED）',
                                `version` INT DEFAULT 1 COMMENT '当前版本号',
                                `metadata` JSON COMMENT '扩展元数据',
                                `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档与目录表';

CREATE TABLE `hib_document_version` (
                                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                        `document_id` BIGINT NOT NULL COMMENT '文档ID',
                                        `version` INT NOT NULL COMMENT '版本号',
                                        `content` LONGTEXT COMMENT '文档内容（富文本或 Markdown）',
                                        `editor_id` BIGINT COMMENT '编辑人ID',
                                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档版本表';

CREATE TABLE `hib_document_comment` (
                                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                        `document_id` BIGINT NOT NULL COMMENT '文档ID',
                                        `user_id` BIGINT NOT NULL COMMENT '评论者ID',
                                        `content` TEXT NOT NULL COMMENT '评论内容',
                                        `anchor` JSON COMMENT '锚点位置（块索引、偏移等）',
                                        `status` VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '状态（ACTIVE / RESOLVED / DELETED）',
                                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档批注表';

CREATE TABLE `hib_operation_log` (
                                     `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                     `type` VARCHAR(100) NOT NULL COMMENT '操作类型（如：LOGIN、DOC_EDIT）',
                                     `description` TEXT COMMENT '操作描述',
                                     `user_id` BIGINT NOT NULL COMMENT '执行用户ID',
                                     `operator` VARCHAR(100) NOT NULL COMMENT '操作人标识（用户名或IP）',
                                     `success` BOOLEAN NOT NULL COMMENT '是否成功',
                                     `params` TEXT COMMENT '输入参数快照',
                                     `result` TEXT COMMENT '输出结果快照',
                                     `timestamp` DATETIME NOT NULL COMMENT '操作时间',
                                     `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
                                     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

CREATE TABLE `hib_document_favorite` (
                                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                         `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                         `document_id` BIGINT NOT NULL COMMENT '被收藏的文档ID',
                                         `favorited_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                                         UNIQUE KEY `uk_user_doc` (`user_id`, `document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户文档收藏表';

CREATE TABLE `hib_notification` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                    `recipient_id` BIGINT NOT NULL COMMENT '接收用户ID',
                                    `type` VARCHAR(100) NOT NULL COMMENT '通知类型（SYSTEM / COMMENT / INVITE / UPDATE）',
                                    `title` VARCHAR(255) COMMENT '通知标题',
                                    `content` TEXT COMMENT '通知内容',
                                    `read` BOOLEAN DEFAULT FALSE COMMENT '是否已读',
                                    `link_url` VARCHAR(255) COMMENT '跳转链接',
                                    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户通知表';

CREATE TABLE `hib_tag` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                           `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '标签名称',
                           `color` VARCHAR(20) DEFAULT '#CCCCCC' COMMENT '标签颜色（Hex）',
                           `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

CREATE TABLE `hib_document_tag` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                    `document_id` BIGINT NOT NULL COMMENT '文档ID',
                                    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
                                    UNIQUE KEY `uk_doc_tag` (`document_id`, `tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档标签关联表';
CREATE TABLE `hib_document_template` (
                                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                         `title` VARCHAR(255) NOT NULL COMMENT '模板标题',
                                         `description` TEXT COMMENT '模板描述',
                                         `content` LONGTEXT NOT NULL COMMENT '模板内容',
                                         `creator_id` BIGINT NOT NULL COMMENT '创建人ID',
                                         `scope` VARCHAR(50) DEFAULT 'PRIVATE' COMMENT '模板可见性（SYSTEM / PUBLIC / PRIVATE）',
                                         `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档模板表';

CREATE TABLE `hib_document_permission` (
                                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                           `document_id` BIGINT NOT NULL COMMENT '文档ID',
                                           `user_id` BIGINT NOT NULL COMMENT '被授权用户ID',
                                           `permission` VARCHAR(50) NOT NULL COMMENT '权限类型（VIEW / EDIT / COMMENT / ADMIN）',
                                           `granted_by` BIGINT COMMENT '授权人ID',
                                           `granted_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
                                           UNIQUE KEY `uk_doc_user` (`document_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档细粒度权限控制表';

CREATE TABLE `hib_webhook` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                               `organization_id` BIGINT COMMENT '所属组织ID',
                               `event_type` VARCHAR(100) NOT NULL COMMENT '事件类型（如：DOCUMENT_UPDATED）',
                               `target_url` VARCHAR(255) NOT NULL COMMENT 'Webhook接收地址',
                               `enabled` BOOLEAN DEFAULT TRUE COMMENT '是否启用',
                               `secret` VARCHAR(255) COMMENT '签名密钥（用于校验）',
                               `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Webhook 配置表';