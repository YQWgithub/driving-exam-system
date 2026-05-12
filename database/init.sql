CREATE DATABASE IF NOT EXISTS driving_exam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE driving_exam;

DROP TABLE IF EXISTS `answer_detail`;
DROP TABLE IF EXISTS `exam_record`;
DROP TABLE IF EXISTS `wrong_question`;
DROP TABLE IF EXISTS `paper_question`;
DROP TABLE IF EXISTS `exam_paper`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `system_config`;

CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '角色：0-学员，1-管理员',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `parent_id` INT(11) DEFAULT NULL COMMENT '父分类ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试题分类表';

CREATE TABLE `question` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `category_id` INT(11) NOT NULL COMMENT '分类ID',
  `type` VARCHAR(20) NOT NULL COMMENT '题型：单选题、多选题、判断题',
  `content` TEXT NOT NULL COMMENT '题目内容',
  `image_url` VARCHAR(255) DEFAULT NULL COMMENT '图片URL',
  `options` TEXT DEFAULT NULL COMMENT '选项（JSON格式）',
  `answer` VARCHAR(50) NOT NULL COMMENT '正确答案',
  `analysis` TEXT DEFAULT NULL COMMENT '解析',
  `difficulty` TINYINT(1) DEFAULT '3' COMMENT '难度：1-5',
  `chapter` VARCHAR(50) DEFAULT NULL COMMENT '章节',
  `knowledge_point` VARCHAR(100) DEFAULT NULL COMMENT '知识点',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_type` (`type`),
  KEY `idx_difficulty` (`difficulty`),
  KEY `idx_chapter` (`chapter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试题表';

CREATE TABLE `exam_paper` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL COMMENT '试卷名称',
  `duration` INT(11) NOT NULL COMMENT '考试时长（分钟）',
  `total_score` INT(11) NOT NULL DEFAULT '100' COMMENT '总分',
  `pass_score` INT(11) NOT NULL DEFAULT '90' COMMENT '及格分',
  `question_count` INT(11) NOT NULL COMMENT '题目数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

CREATE TABLE `paper_question` (
  `paper_id` INT(11) NOT NULL COMMENT '试卷ID',
  `question_id` INT(11) NOT NULL COMMENT '试题ID',
  `question_order` INT(11) NOT NULL COMMENT '题目序号',
  PRIMARY KEY (`paper_id`, `question_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷试题关联表';

CREATE TABLE `exam_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `paper_id` INT(11) NOT NULL COMMENT '试卷ID',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `score` INT(11) DEFAULT NULL COMMENT '得分',
  `total` INT(11) DEFAULT 100 COMMENT '总分',
  `is_pass` TINYINT(1) DEFAULT NULL COMMENT '是否及格：0-不及格，1-及格',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0-进行中，1-已交卷，2-超时',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_paper_id` (`paper_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';

CREATE TABLE `answer_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `exam_record_id` INT(11) NOT NULL COMMENT '考试记录ID',
  `question_id` INT(11) NOT NULL COMMENT '试题ID',
  `user_answer` VARCHAR(50) DEFAULT NULL COMMENT '用户答案',
  `is_correct` TINYINT(1) DEFAULT NULL COMMENT '是否正确：0-错误，1-正确',
  `answer_time` INT(11) DEFAULT '0' COMMENT '答题用时（秒）',
  PRIMARY KEY (`id`),
  KEY `idx_exam_record_id` (`exam_record_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题详情表';

CREATE TABLE `wrong_question` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `question_id` INT(11) NOT NULL COMMENT '试题ID',
  `wrong_answer` VARCHAR(10) DEFAULT NULL COMMENT '错误答案',
  `correct_answer` VARCHAR(10) DEFAULT NULL COMMENT '正确答案',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_question` (`user_id`, `question_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本表';

CREATE TABLE `system_config` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `param_key` VARCHAR(50) NOT NULL COMMENT '参数键',
  `param_value` VARCHAR(200) NOT NULL COMMENT '参数值',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';

INSERT INTO `user` (`username`, `password`, `phone`, `email`, `role`, `status`) VALUES
('admin', 'admin123', '13800138000', 'admin@example.com', 1, 1),
('student1', '123456', '13800138001', 'student1@example.com', 0, 1);

INSERT INTO `category` (`name`, `parent_id`) VALUES
('科目一', NULL),
('科目四', NULL),
('道路交通安全法律法规', 1),
('道路交通信号', 1),
('安全行车、文明驾驶', 1),
('机动车驾驶操作相关基础知识', 1),
('恶劣气象和复杂道路条件下的驾驶常识', 2),
('紧急情况避险', 2),
('交通事故处置', 2);

INSERT INTO `exam_paper` (`title`, `duration`, `total_score`, `pass_score`, `question_count`) VALUES
('科目一模拟试卷', 45, 100, 90, 100),
('科目四模拟试卷', 30, 100, 90, 50);

INSERT INTO `system_config` (`param_key`, `param_value`, `description`) VALUES
('exam_duration_subject1', '45', '科目一考试时长（分钟）'),
('exam_duration_subject4', '30', '科目四考试时长（分钟）'),
('pass_score_subject1', '90', '科目一及格分数'),
('pass_score_subject4', '90', '科目四及格分数'),
('question_count_subject1', '100', '科目一题目数量'),
('question_count_subject4', '50', '科目四题目数量');
